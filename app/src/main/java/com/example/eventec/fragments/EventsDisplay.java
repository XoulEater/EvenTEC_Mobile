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
    private EventAdapter eventAdapter;
    private RecyclerView eventRV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_display, container, false);
        eventRV = view.findViewById(R.id.RVEvents);

        SingleFirebase single = SingleFirebase.getInstance();
        single.refreshEventList();

        single.setEventsListener(new SingleFirebase.EventsListener() {
            @Override
            public void onEventsLoaded(ArrayList<EventModel> eventModelArrayList) {
                eventAdapter = new EventAdapter(requireContext(), single.getEventModelArrayList());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

                eventRV.setLayoutManager(linearLayoutManager);
                eventRV.setAdapter(eventAdapter);
            }
        });

        return view;

    }


}