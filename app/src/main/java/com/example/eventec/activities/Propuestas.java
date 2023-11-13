package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.eventec.R;
import com.example.eventec.entities.EventAdapter;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.PropAdapter;
import com.example.eventec.entities.SingleFirebase;

import java.util.ArrayList;

public class Propuestas extends AppCompatActivity {
    private PropAdapter propAdapter; // Adaptador para el RecyclerView
    private RecyclerView propsRV; // RecyclerView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_display);
        propsRV = findViewById(R.id.RVEvents);

        // Cargar las propuestas
        SingleFirebase single = SingleFirebase.getInstance();
        ArrayList<EventModel> props = single.getPropsModelArrayList();
        // Si props es vacío, no hay propuestas
        if (props.size() == 0) {
            Toast.makeText(this, "No hay propuestas", Toast.LENGTH_SHORT).show();
            return;
        }
        propAdapter = new PropAdapter(this, props);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Configuración del RecyclerView
        propsRV.setLayoutManager(linearLayoutManager);
        propsRV.setAdapter(propAdapter); //

    }
}