package com.example.eventec.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventec.R;
import com.example.eventec.entities.AlertAdapter;
import com.example.eventec.entities.AlertModel;
import com.example.eventec.entities.SingleFirebase;

import java.util.ArrayList;

public class EventsAlert extends Fragment {
    private AlertAdapter alertAdapter; // Adapter para el recycler view
    private RecyclerView alertRV; // Recycler view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_alert, container, false);
        alertRV = view.findViewById(R.id.RVAlerts);

        SingleFirebase single = SingleFirebase.getInstance();
        // Cargamos los datos
        single.refreshAlertList();

        // Listener para cuando se carguen los datos
        single.setAlertsListener(new SingleFirebase.AlertsListener() {
            @Override
            // Se ejecuta cuando se cargan los datos
            public void onAlertsLoaded(ArrayList<AlertModel> alertModelArrayList) {
                alertAdapter = new AlertAdapter(requireContext(), single.getAlertModelArrayList());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

                // Seteamos el layout manager y el adapter al recycler view
                alertRV.setLayoutManager(linearLayoutManager);
                alertRV.setAdapter(alertAdapter);
            }
        });

        return view;
    }



}