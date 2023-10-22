package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventec.R;
import com.example.eventec.entities.ActivityAdapter;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.CollabAdapter;
import com.example.eventec.entities.CollabModel;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.SingleFirebase;

import java.util.ArrayList;

public class Event extends AppCompatActivity {
    private EventModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        int eventIndex = intent.getIntExtra("index", -1);

        SingleFirebase single = SingleFirebase.getInstance();
        model = single.getEventModelArrayList().get(eventIndex);

        loadEventData();
        loadActivities();
        loadCollabs();


    }
    private void loadCollabs(){
        RecyclerView collabsRV = findViewById(R.id.collabsRV);;

        ArrayList<CollabModel> collabModelArrayList = model.getCollabModelArrayList();

        CollabAdapter collabAdapter = new CollabAdapter(this, collabModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        collabsRV.setLayoutManager(linearLayoutManager);
        collabsRV.setAdapter(collabAdapter);
    }

    private void loadActivities(){
        RecyclerView activitiesRV = findViewById(R.id.activitiesRV);;

        ArrayList<ActivityModel> activityModelArrayList = model.getActivityModelArrayList();

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
        TextView startDateTV = findViewById(R.id.startDate);
        TextView endDateTV = findViewById(R.id.endDate);
        TextView capacityTV = findViewById(R.id.capacity);

        eventImageIV.setImageResource(model.getEventImage());
        cat1TV.setText(model.getCategories().get(0));
        cat2TV.setText(model.getCategories().get(1));
        cat3TV.setText(model.getCategories().get(2));
        titleTV.setText(model.getEventName());
        descriptionTV.setText(model.getDescription());
        requirementsTV.setText(model.getRequirements());
        startDateTV.setText(model.getStartDate());
        endDateTV.setText(model.getEndDate());
        capacityTV.setText(model.getPlaces() + "/" + model.getCapacity());
    }
}