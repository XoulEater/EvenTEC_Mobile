package com.example.eventec.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

// Clase que muestra la información de una propuesta
public class Prop extends AppCompatActivity {
    private EventModel model; // Modelo de la propuesta
    private SingleFirebase singleFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event); // Cargar layout

        // Eliminar el botón de reservar
        TextView btnReservar = findViewById(R.id.reservarBtn);
        ViewGroup parent = (ViewGroup) btnReservar.getParent();
        parent.removeView(btnReservar);
        // Eliminar el botón de evaluar
        TextView btnEvaluar = findViewById(R.id.reviewBtn);
        parent = (ViewGroup) btnEvaluar.getParent();
        parent.removeView(btnEvaluar);

        // Obtener el índice del evento seleccionado
        Intent intent = getIntent();
        int eventIndex = intent.getIntExtra("index", -1);

        // Obtener el modelo del evento
        singleFirebase = SingleFirebase.getInstance();
        model = singleFirebase.getPropsModelArrayList().get(eventIndex);

        // Cargar la información del evento
        loadEventData();
        loadActivities();
        loadCollabs();

        // Verificar si el usuario está inscrito en el evento
        // TODO: comenten esta obra arquitectonica de la ingenieria de software
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
        TextView asociaNameTV = findViewById(R.id.asociaName);
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
        asociaNameTV.setText(model.getNombreAsociacion());
        descriptionTV.setText(model.getDescripcion());
        requirementsTV.setText(model.getRequerimientos());
        lugaresTV.setText(model.getLugares());
        startDateTV.setText(model.getFechaInicio());
        endDateTV.setText(model.getFechaFin());
        capacityTV.setText(model.getCupos() + "/" + model.getCapacidad());
    }
}