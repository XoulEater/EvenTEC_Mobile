package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.eventec.R;
import com.example.eventec.entities.ActivityAdapter;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.CollabAdapter;
import com.example.eventec.entities.CollabModel;

import java.util.ArrayList;

public class Event extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        LoadActivities();
        LoadCollabs();
        

    }
    private void LoadCollabs(){
        RecyclerView collabsRV = findViewById(R.id.collabsRV);;

        ArrayList<CollabModel> collabModelArrayList = new ArrayList<CollabModel>();

        collabModelArrayList.add(new CollabModel("ATI", "Asociacion", R.drawable.no_image));
        collabModelArrayList.add(new CollabModel("Joaquin", "Colaborador", R.drawable.no_image));

        CollabAdapter collabAdapter = new CollabAdapter(this, collabModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        collabsRV.setLayoutManager(linearLayoutManager);
        collabsRV.setAdapter(collabAdapter);
    }

    private void LoadActivities(){
        RecyclerView activitiesRV = findViewById(R.id.activitiesRV);;

        ArrayList<ActivityModel> activityModelArrayList = new ArrayList<ActivityModel>();

        activityModelArrayList.add(new ActivityModel("20/10/2023", "10:30 am", "Bingo", "Raul"));
        activityModelArrayList.add(new ActivityModel("21/10/2023", "11:00 pm", "Baile", "Mina"));

        ActivityAdapter activityAdapter = new ActivityAdapter(this, activityModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        activitiesRV.setLayoutManager(linearLayoutManager);
        activitiesRV.setAdapter(activityAdapter);
    }
}