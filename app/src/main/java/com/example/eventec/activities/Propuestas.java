package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.eventec.R;
import com.example.eventec.entities.EventAdapter;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.PropAdapter;
import com.example.eventec.entities.SingleFirebase;

import java.util.ArrayList;

public class Propuestas extends AppCompatActivity {
    private PropAdapter propAdapter;
    private RecyclerView propsRV;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events_display);
        propsRV = findViewById(R.id.RVEvents);

        SingleFirebase single = SingleFirebase.getInstance();
        propAdapter = new PropAdapter(context, single.getPropsModelArrayList());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        propsRV.setLayoutManager(linearLayoutManager);
        propsRV.setAdapter(propAdapter);

    }
}