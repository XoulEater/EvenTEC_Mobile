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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_alert, container, false);
        RecyclerView eventRV = view.findViewById(R.id.RVAlerts);

        SingleFirebase single = SingleFirebase.getInstance();
        ArrayList<AlertModel> alertModelArrayList = single.getAlertModelArrayList();

        AlertAdapter alertAdapter = new AlertAdapter(requireContext(), alertModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        eventRV.setLayoutManager(linearLayoutManager);
        eventRV.setAdapter(alertAdapter);
        return view;
    }

    public void update(){

    }

}