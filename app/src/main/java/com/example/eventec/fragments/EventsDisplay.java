package com.example.eventec.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventec.R;
import com.example.eventec.entities.EventAdapter;
import com.example.eventec.entities.EventModel;

import java.util.ArrayList;


public class EventsDisplay extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_display, container, false);
        RecyclerView eventRV = view.findViewById(R.id.RVEvents);

        ArrayList<EventModel> eventModelArrayList = new ArrayList<EventModel>();

        eventModelArrayList.add(new EventModel("Semana ATI", "30 de Sep", "Asocia de ATI", 500, R.drawable.des));
        eventModelArrayList.add(new EventModel("Semana ATI", "30 de Sep", "Asocia de ATI", 500, R.drawable.des));

        EventAdapter eventAdapter = new EventAdapter(requireContext(), eventModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        eventRV.setLayoutManager(linearLayoutManager);
        eventRV.setAdapter(eventAdapter);
        return view;
    }

}