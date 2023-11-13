package com.example.eventec.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventec.R;
import com.example.eventec.entities.AlertAdapter;
import com.example.eventec.entities.AlertModel;
import com.example.eventec.entities.EventAdapter;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.SingleFirebase;

import java.util.ArrayList;


public class EventsDisplay extends Fragment {
    private EventAdapter eventAdapter; // Adaptador para el RecyclerView
    private RecyclerView eventRV; // RecyclerView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_display, container, false);
        eventRV = view.findViewById(R.id.RVEvents);

        SingleFirebase single = SingleFirebase.getInstance();
        single.refreshEventList(); // Actualizar la lista de eventos

        // Cargar los eventos
        single.setEventsListener(new SingleFirebase.EventsListener() { // Listener para cuando se cargan los eventos
            @Override
            // Función que se ejecuta cuando se cargan los eventos
            public void onEventsLoaded(ArrayList<EventModel> eventModelArrayList) {
                ArrayList<EventModel> events = single.getEventModelArrayList();
                // si single.getEventModelArrayList() es vacío, no hay eventos
                if (events.size() == 0) {
                    Toast.makeText(requireContext(), "No hay eventos", Toast.LENGTH_SHORT).show();
                    return;
                }
                eventAdapter = new EventAdapter(requireContext(), events);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

                // Configuración del RecyclerView
                eventRV.setLayoutManager(linearLayoutManager);
                eventRV.setAdapter(eventAdapter);
            }
        });

        return view;

    }


}