package com.example.eventec.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.eventec.R;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.Comment;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.QuestionAdapter;
import com.example.eventec.entities.QuestionModel;
import com.example.eventec.entities.SingleFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

// Actividad para los cuestionarios y retroalimentación
public class Cuestionario extends AppCompatActivity {

    private EventModel evento;
    private SingleFirebase singleFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        Intent intent = getIntent(); // Lee el eventId
        String eventId = intent.getStringExtra("eventId");

        singleFirebase = SingleFirebase.getInstance();
        evento = singleFirebase.getEventById(eventId); // Obtiene el evento

        TextView eventoTitle = findViewById(R.id.title);
        eventoTitle.setText(evento.getTitulo());

        List<ActivityModel> activities = evento.getActivities();

        // Lista con las preguntas
        ArrayList<QuestionModel> questionModelArrayList = new ArrayList<>();
        questionModelArrayList.add(new QuestionModel("Evento", "¿Qué te pareció el evento?"));

        for (ActivityModel activity : activities) {
            questionModelArrayList.add(new QuestionModel(activity.getTitle(), "¿Qué te pareció la actividad?"));
        }

        // Inicializa el RecyclerView para las preguntas
        RecyclerView activityRatings = findViewById(R.id.activityRatings);

        QuestionAdapter questionAdapter = new QuestionAdapter(this, questionModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Cuestionario.this, LinearLayoutManager.VERTICAL, false);

        activityRatings.setLayoutManager(linearLayoutManager);
        activityRatings.setAdapter(questionAdapter);
    }


    // Función que guarda una evaluación
    public void sendReview(View view){
        String eventId = evento.getEventId();

        // Crear un SimpleDateFormat con dd/MM/yyyy HH:mm:ss formato
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        // Obtener la fecha y hora actual
        Date fecha = new Date();

        // Formatear la fecha como String
        String fechaString = sdf.format(fecha);

        // Obtener el comentario
        EditText comentarioText = findViewById(R.id.comentario);
        // Se crea un objeto comentario
        Comment comment = new Comment(comentarioText.getText().toString(), eventId, fechaString, singleFirebase.getCurrentUsername());

        RecyclerView activityRatings = findViewById(R.id.activityRatings);
        QuestionAdapter questionAdapter = (QuestionAdapter) activityRatings.getAdapter();
        RecyclerView.LayoutManager layoutManager = activityRatings.getLayoutManager();
        // Se obtiene la lista de respuestas del adapter
        List<QuestionModel> questionModelArrayList = questionAdapter.getQuestionModelArrayList();
        DatabaseReference myRef = singleFirebase.getMyRef();

        // Se leen los ratings que tiene el evento
        myRef.child("ratings").child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    // Se obtienen los ratings
                    HashMap<String, HashMap<String, Long>> ratings = new HashMap<>();
                    if (task.getResult().exists()){
                        // Si hay ratings, se guardan en el HashMap
                        ratings = (HashMap<String, HashMap<String, Long>>) task.getResult().getValue();
                        // Por cada rating
                        for(int i = 0; i < questionModelArrayList.size(); i++) {
                            // Se obtiene la pregunta
                            QuestionModel question = questionModelArrayList.get(i);
                            View questionView = layoutManager.findViewByPosition(i);

                            // Se lee la opción seleccionada
                            Spinner ratings_spinner = questionView.findViewById(R.id.ratings_spinner);
                            long rating = Integer.parseInt(ratings_spinner.getSelectedItem().toString());

                            HashMap<String, Long> activityInfo = ratings.get(question.getTitle());
                            // Se incrementa la cantidad y se suma el rating a la suma acumulada
                            long cantidad = activityInfo.get("cantidad") + 1;
                            activityInfo.put("cantidad", cantidad);
                            activityInfo.put("suma", activityInfo.get("suma") + rating);
                        }
                    } else {
                        // Si no hay ratings, se agrega el rating y un 1 al HashMap para empezar a contarlos.
                        for (int i = 0; i < questionModelArrayList.size(); i++) {
                            QuestionModel question = questionModelArrayList.get(i);
                            View questionView = layoutManager.findViewByPosition(i);

                            Spinner ratings_spinner = questionView.findViewById(R.id.ratings_spinner);
                            long rating = Integer.parseInt(ratings_spinner.getSelectedItem().toString());

                            HashMap<String, Long> activityInfo = new HashMap<>();
                            activityInfo.put("cantidad", 1L);
                            activityInfo.put("suma", rating);

                            ratings.put(question.getTitle(), activityInfo);
                        }
                    }

                    // Se guardan los ratings en la base de datos
                    myRef.child("ratings").child(eventId).setValue(ratings);
                    String commentKey = myRef.child("comments").child(eventId).push().getKey();
                    myRef.child("comments").child(eventId).child(commentKey).setValue(comment);
                    Toast.makeText(Cuestionario.this, "Gracias por tu comentario", Toast.LENGTH_LONG).show();
                    finish(); // Se devuelve al activity anterior
                }
            }
        });


    }

}