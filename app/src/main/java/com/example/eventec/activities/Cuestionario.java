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

public class Cuestionario extends AppCompatActivity {

    private EventModel evento;
    private SingleFirebase singleFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");

        singleFirebase = SingleFirebase.getInstance();
        evento = singleFirebase.getEventById(eventId);

        TextView eventoTitle = findViewById(R.id.title);
        eventoTitle.setText(evento.getTitulo());

        List<ActivityModel> activities = evento.getActivities();

        ArrayList<QuestionModel> questionModelArrayList = new ArrayList<>();
        questionModelArrayList.add(new QuestionModel("Evento", "¿Qué te pareció el evento?"));

        for (ActivityModel activity : activities) {
            questionModelArrayList.add(new QuestionModel(activity.getTitle(), "¿Qué te pareció la actividad?"));
        }

        RecyclerView activityRatings = findViewById(R.id.activityRatings);

        QuestionAdapter questionAdapter = new QuestionAdapter(this, questionModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Cuestionario.this, LinearLayoutManager.VERTICAL, false);

        activityRatings.setLayoutManager(linearLayoutManager);
        activityRatings.setAdapter(questionAdapter);
    }


    public void sendReview(View view){
        String eventId = evento.getEventId();

        // Create a SimpleDateFormat with the dd/MM/yyyy HH:mm:ss format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        // Get the current date and time
        Date fecha = new Date();

        // Format the date and time as a string
        String fechaString = sdf.format(fecha);
        EditText comentarioText = findViewById(R.id.comentario);
        Comment comment = new Comment(comentarioText.getText().toString(), eventId, fechaString, singleFirebase.getCurrentUsername());

        RecyclerView activityRatings = findViewById(R.id.activityRatings);
        QuestionAdapter questionAdapter = (QuestionAdapter) activityRatings.getAdapter();
        RecyclerView.LayoutManager layoutManager = activityRatings.getLayoutManager();

        List<QuestionModel> questionModelArrayList = questionAdapter.getQuestionModelArrayList();
        DatabaseReference myRef = singleFirebase.getMyRef();

        myRef.child("ratings").child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    HashMap<String, HashMap<String, Long>> ratings = new HashMap<>();
                    if (task.getResult().exists()){
                        ratings = (HashMap<String, HashMap<String, Long>>) task.getResult().getValue();
                        for(int i = 0; i < questionModelArrayList.size(); i++) {
                            QuestionModel question = questionModelArrayList.get(i);
                            View questionView = layoutManager.findViewByPosition(i);

                            Spinner ratings_spinner = questionView.findViewById(R.id.ratings_spinner);
                            long rating = Integer.parseInt(ratings_spinner.getSelectedItem().toString());
                            HashMap<String, Long> activityInfo = ratings.get(question.getTitle());
                            long cantidad = activityInfo.get("cantidad") + 1;
                            activityInfo.put("cantidad", cantidad);
                            activityInfo.put("suma", activityInfo.get("suma") + rating);
                        }
                    } else {
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
                    Log.d("TEST", ratings.toString());
                    myRef.child("ratings").child(eventId).setValue(ratings);
                    String commentKey = myRef.child("comments").child(eventId).push().getKey();
                    myRef.child("comments").child(eventId).child(commentKey).setValue(comment);
                    Toast.makeText(Cuestionario.this, "Gracias por tu comentario", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


    }

}