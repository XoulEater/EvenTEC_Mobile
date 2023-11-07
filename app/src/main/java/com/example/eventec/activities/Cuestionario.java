package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eventec.R;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.QuestionAdapter;
import com.example.eventec.entities.QuestionModel;
import com.example.eventec.entities.SingleFirebase;

import java.util.ArrayList;
import java.util.List;

public class Cuestionario extends AppCompatActivity {

    private EventModel evento;
    private SingleFirebase singleFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");

        singleFirebase = SingleFirebase.getInstance();
        evento = singleFirebase.getEventById(eventId);

        TextView eventoTitle = findViewById(R.id.title);
        eventoTitle.setText(evento.getTitulo());

        List<ActivityModel> activities = evento.getActivities();

        ArrayList<QuestionModel> questionModelArrayList = new ArrayList<>();
        questionModelArrayList.add(new QuestionModel("Evento", "¿Qué te pareció el evento?"));

        for (ActivityModel activity : activities) {
            questionModelArrayList.add(new QuestionModel(activity.getTitle(), "¿Qué te pareció la actividad?"));
        }

        RecyclerView activityRatings = findViewById(R.id.activityRatings);

        QuestionAdapter questionAdapter = new QuestionAdapter(this, questionModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Cuestionario.this, LinearLayoutManager.VERTICAL, false);

        activityRatings.setLayoutManager(linearLayoutManager);
        activityRatings.setAdapter(questionAdapter);
    }
}