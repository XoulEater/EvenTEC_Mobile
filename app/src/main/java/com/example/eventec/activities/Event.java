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
import com.example.eventec.entities.ActivityAdapter;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.CollabAdapter;
import com.example.eventec.entities.CollabModel;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.SingleFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;

public class Event extends AppCompatActivity {
    private EventModel model;
    private SingleFirebase singleFirebase;
    private boolean userInscrito;
    private boolean asociacionOwner;
    private TextView reservarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        int eventIndex = intent.getIntExtra("index", -1);

        singleFirebase = SingleFirebase.getInstance();
        model = singleFirebase.getEventModelArrayList().get(eventIndex);

        loadEventData();
        loadActivities();
        loadCollabs();

        singleFirebase.incrementarClicksEvento(model);

        reservarBtn = findViewById(R.id.reservarBtn);

        if (singleFirebase.getCurrentUserType() == 0){

            String currentCarnet = singleFirebase.getCurrentUserCarnet();
            DatabaseReference myRef = singleFirebase.getMyRef();
            myRef.child("userEventos").child(currentCarnet).child(model.getEventId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        if (task.getResult().exists() && task.getResult().getValue().equals(true)){
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            reservarBtn.setText("Cancelar");
                            userInscrito = true;
                        } else {
                            if (model.getCupos() == model.getCapacidad()){
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
        } else if (singleFirebase.getCurrentUserType() == 2){
            if (model.getUserAsociacion().equals(singleFirebase.getCurrentAsoUser())){
                reservarBtn.setText("Ver asistentes");
                asociacionOwner = true;
            } else {
                asociacionOwner = false;
                ViewGroup parentView = (ViewGroup) reservarBtn.getParent();
                parentView.removeView(reservarBtn);
            }
        }
    }

    private void loadCollabs(){
        RecyclerView collabsRV = findViewById(R.id.collabsRV);;

        List<CollabModel> collabModelArrayList = model.getColabs();

        CollabAdapter collabAdapter = new CollabAdapter(this, collabModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        collabsRV.setLayoutManager(linearLayoutManager);
        collabsRV.setAdapter(collabAdapter);
    }

    private void loadActivities(){
        RecyclerView activitiesRV = findViewById(R.id.activitiesRV);;

        List<ActivityModel> activityModelArrayList = model.getActivities();

        ActivityAdapter activityAdapter = new ActivityAdapter(this, activityModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        activitiesRV.setLayoutManager(linearLayoutManager);
        activitiesRV.setAdapter(activityAdapter);
    }

    private void loadEventData(){
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

        eventImageIV.setImageResource(model.getImagenSrc());
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

    public void reservar(View view){
        if (singleFirebase.getCurrentUserType() == 0){
            String currentCarnet = singleFirebase.getCurrentUserCarnet();
            DatabaseReference myRef = singleFirebase.getMyRef();
            if (userInscrito){
                myRef.child("userEventos").child(currentCarnet).child(model.getEventId()).setValue(false);
                myRef.child("inscritos").child(model.getEventId()).child(currentCarnet).setValue(false);
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("eventos/" + model.getEventId() + "/cupos", ServerValue.increment(-1));
                myRef.updateChildren(updates);
                model.updateCupos(-1);
                userInscrito = false;
                reservarBtn.setText("Reservar");
            } else {
                myRef.child("userEventos").child(currentCarnet).child(model.getEventId()).setValue(true);
                myRef.child("inscritos").child(model.getEventId()).child(currentCarnet).setValue(true);
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("eventos/" + model.getEventId() + "/cupos", ServerValue.increment(1));
                myRef.updateChildren(updates);
                model.updateCupos(1);
                userInscrito = true;
                reservarBtn.setText("Cancelar");
            }
            TextView capacityTV = findViewById(R.id.capacity);
            capacityTV.setText(model.getCupos() + "/" + model.getCapacidad());
        } else if (singleFirebase.getCurrentUserType() == 2){
//            if (asociacionOwner){
//                Intent intent = new Intent(this, Asistentes.class);
//                intent.putExtra("eventId", model.getEventId());
//                startActivity(intent);
//            }
            Log.d("TEST", String.valueOf(asociacionOwner));
        }
    }
}