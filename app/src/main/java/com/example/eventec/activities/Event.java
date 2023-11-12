package com.example.eventec.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventec.R;
import com.example.eventec.email.SendMail;
import com.example.eventec.entities.ActivityAdapter;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.AlertModel;
import com.example.eventec.entities.CollabAdapter;
import com.example.eventec.entities.CollabModel;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.SingleFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

// Clase que muestra la información de un evento
public class Event extends AppCompatActivity {
    private EventModel model; // Modelo del evento
    private SingleFirebase singleFirebase;
    private boolean userInscrito;
    private boolean asociacionOwner;
    private TextView reservarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event); // Cargar layout

        // Obtener el índice del evento seleccionado
        Intent intent = getIntent();
        int eventIndex = intent.getIntExtra("index", -1);

        // Obtener el modelo del evento
        singleFirebase = SingleFirebase.getInstance();
        model = singleFirebase.getEventModelArrayList().get(eventIndex);

        // Cargar la información del evento
        loadEventData();
        loadActivities();
        loadCollabs();

        singleFirebase.incrementarClicksEvento(model); // Incrementar el número de clicks del evento
        reservarBtn = findViewById(R.id.reservarBtn); // Obtener el botón de reservar

        // Verificar si el usuario está inscrito en el evento
        // TODO: comenten esta obra arquitectonica de la ingenieria de software
        if (singleFirebase.getCurrentUserType() == 0) {

            String currentCarnet = singleFirebase.getCurrentUserCarnet();
            DatabaseReference myRef = singleFirebase.getMyRef();
            myRef.child("userEventos").child(currentCarnet).child(model.getEventId()).get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                if (task.getResult().exists() && task.getResult().getValue().equals(true)) {
                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                    reservarBtn.setText("Cancelar");
                                    userInscrito = true;
                                } else {
                                    if (model.getCupos() == model.getCapacidad()) {
                                        reservarBtn.setEnabled(false);
                                        reservarBtn.setText("Cupos llenos");
                                    } else {
                                        reservarBtn.setEnabled(true);
                                        reservarBtn.setText("Reservar");
                                    }
                                    userInscrito = false;
                                }
                            }
                        }
                    });
        } else if (singleFirebase.getCurrentUserType() == 2) {
            TextView reviewBtn = findViewById(R.id.reviewBtn);
            reviewBtn.setVisibility(View.INVISIBLE);

            if (model.getUserAsociacion().equals(singleFirebase.getCurrentAsoUser())) {
                reservarBtn.setText("Ver asistentes");
                asociacionOwner = true;
            } else {
                asociacionOwner = false;
                ViewGroup parentView = (ViewGroup) reservarBtn.getParent();
                parentView.removeView(reservarBtn);
            }
        }
    }

    // Cargar los colaboradores del evento
    private void loadCollabs() {
        RecyclerView collabsRV = findViewById(R.id.collabsRV);
        ; // Obtener el RecyclerView
        List<CollabModel> collabModelArrayList = model.getColabs(); // Obtener la lista de colaboradores
        CollabAdapter collabAdapter = new CollabAdapter(this, collabModelArrayList); // Crear el adaptador

        // Crear el layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Asignar el layout manager y el adaptador al RecyclerView
        collabsRV.setLayoutManager(linearLayoutManager);
        collabsRV.setAdapter(collabAdapter);
    }

    // Cargar las actividades del evento
    private void loadActivities() {
        RecyclerView activitiesRV = findViewById(R.id.activitiesRV); // Obtener el RecyclerView
        List<ActivityModel> activityModelArrayList = model.getActivities(); // Obtener la lista de actividades
        ActivityAdapter activityAdapter = new ActivityAdapter(this, activityModelArrayList); // Crear el adaptador

        // Crear el layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Asignar el layout manager y el adaptador al RecyclerView
        activitiesRV.setLayoutManager(linearLayoutManager);
        activitiesRV.setAdapter(activityAdapter);
    }

    // Cargar la información del evento
    private void loadEventData() {
        // Obtener los elementos del layout
        ImageView eventImageIV = findViewById(R.id.eventImage);
        TextView cat1TV = findViewById(R.id.cat1);
        TextView cat2TV = findViewById(R.id.cat2);
        TextView cat3TV = findViewById(R.id.cat3);
        TextView titleTV = findViewById(R.id.title);
        TextView descriptionTV = findViewById(R.id.description);
        TextView requirementsTV = findViewById(R.id.requirements);
        TextView lugaresTV = findViewById(R.id.lugares);
        TextView startDateTV = findViewById(R.id.startDate);
        TextView endDateTV = findViewById(R.id.endDate);
        TextView capacityTV = findViewById(R.id.capacity);

        // Asignar la información del evento a los elementos del layout
        eventImageIV.setImageResource(R.drawable.events);
        cat1TV.setText(model.getCategorias().get(0));
        cat2TV.setText(model.getCategorias().get(1));
        cat3TV.setText(model.getCategorias().get(2));
        titleTV.setText(model.getTitulo());
        descriptionTV.setText(model.getDescripcion());
        requirementsTV.setText(model.getRequerimientos());
        lugaresTV.setText(model.getLugares());
        startDateTV.setText(model.getFechaInicio());
        endDateTV.setText(model.getFechaFin());
        capacityTV.setText(model.getCupos() + "/" + model.getCapacidad());
    }

    // Método que se ejecuta al presionar el botón de reservar
    public void reservar(View view) {
        // Si es un usuario
        if (singleFirebase.getCurrentUserType() == 0) {
            // Obtener el carnet del usuario
            String currentCarnet = singleFirebase.getCurrentUserCarnet();
            DatabaseReference myRef = singleFirebase.getMyRef();
            // Si el usuario ya está inscrito en el evento, cancelar la inscripción
            if (userInscrito) {
                // Se actualiza la cancelación en userEventos e inscritos
                myRef.child("userEventos").child(currentCarnet).child(model.getEventId()).setValue(false);
                myRef.child("inscritos").child(model.getEventId()).child(currentCarnet).setValue(false);
                HashMap<String, Object> updates = new HashMap<>();

                // Se decrementa el cupo del evento
                updates.put("eventos/" + model.getEventId() + "/cupos", ServerValue.increment(-1));
                myRef.updateChildren(updates);
                model.updateCupos(-1);
                userInscrito = false;
                reservarBtn.setText("Reservar");
            } else {
                // Si el usuario no está inscrito, se agrega su inscripción.
                myRef.child("userEventos").child(currentCarnet).child(model.getEventId()).setValue(true);
                myRef.child("inscritos").child(model.getEventId()).child(currentCarnet).setValue(true);
                HashMap<String, Object> updates = new HashMap<>();

                // Se incrementan los cupos
                updates.put("eventos/" + model.getEventId() + "/cupos", ServerValue.increment(1));
                myRef.updateChildren(updates);
                model.updateCupos(1);
                userInscrito = true;
                reservarBtn.setText("Cancelar");

                // Instanciar SendMail, se envía correo de confirmación con QR.
                SendMail sm = new SendMail(singleFirebase.getCurrentUserEmail(), "Reserva de evento",
                        "Se ha reservado el evento " + model.getTitulo() + " con éxito.");
                sm.setQRData(model.getEventId() + " " + currentCarnet);
                sm.execute(true);

                // TODO: Revisar si se llenó para enviar correo de cupos llenos.
                if (model.getCupos() == model.getCapacidad()) {
                    String subject = "Cupos llenos";
                    String message = "Se ha llenado el cupo del evento " + model.getTitulo() + ".";
                    myRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            } else {
                                // Se recorren los users
                                HashMap<String, HashMap<?, ?>> users = (HashMap<String, HashMap<?, ?>>) task.getResult()
                                        .getValue();
                                for (String user : users.keySet()) {
                                    HashMap<String, ?> userMap = (HashMap<String, ?>) users.get(user); // Se obtiene el
                                                                                                       // user
                                    // Se envía el correo
                                    SendMail sendMail = new SendMail(userMap.get("email").toString(), subject, message);
                                    sendMail.execute(false);
                                }
                            }

                        }
                    });
                    Date fecha = new Date();
                    AlertModel alertModel = new AlertModel(subject, message, fecha.toString(),   1);
                    // subir alerta a la base de datos
                    myRef.child("alertas").child(model.getEventId()).setValue(alertModel);
                }

            }
            TextView capacityTV = findViewById(R.id.capacity);
            // Se actualiza el texto de los cupos.
            capacityTV.setText(model.getCupos() + "/" + model.getCapacidad());
        } else if (singleFirebase.getCurrentUserType() == 2) {
            // Si es una asociación, se pasa a la actividad del informe, o análisis de
            // resultados y estadísticas.
            Intent intent = new Intent(this, Informe.class);
            intent.putExtra("eventId", model.getEventId());
            startActivity(intent);
        }
    }

    // Función que pasa a las encuestas y retroalimentación.
    public void goReview(View view) {
        Intent intent = new Intent(this, Cuestionario.class);
        intent.putExtra("eventId", model.getEventId());
        startActivity(intent);
    }
}