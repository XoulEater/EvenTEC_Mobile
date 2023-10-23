package com.example.eventec.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventec.R;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.EmptyActivityAdapter;

import java.util.ArrayList;


public class EventsCreator extends Fragment {
    private final int GALLERY_REQ_CODE = 1000;
    private Uri selectedImageUri;
    private ImageView eventImage;
    private TextView btnSubmit;
    private ImageButton btnGallery;
    private ImageButton btnAddAct;
    private ImageButton btnDelAct;
    private RecyclerView actRV;
    private ArrayList<ActivityModel> activities;
    private EmptyActivityAdapter actAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_creator, container, false);
        // Inflate the layout for this fragment

        eventImage = view.findViewById(R.id.eventImage);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnAddAct = view.findViewById(R.id.imageButton4);
        btnDelAct = view.findViewById(R.id.imageButton3);
        btnSubmit = view.findViewById(R.id.submit);

        actRV = view.findViewById(R.id.recyclerView);
        activities = new ArrayList<ActivityModel>(); // Inicializa la lista de actividades
        actAdapter = new EmptyActivityAdapter(getContext(), activities);
        actRV.setLayoutManager(new LinearLayoutManager(getContext()));
        actRV.setAdapter(actAdapter);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        btnAddAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityModel activity = new ActivityModel("Fecha", "Hora", "Título", "Moderador");
                actAdapter.addActivity(activity);
            }
        });

        btnDelAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actAdapter.removeActivity();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < activities.size(); i++) {
                    View cardView = actRV.getLayoutManager().findViewByPosition(i); // Encuentra la vista de la tarjeta en la posición i
                    if (cardView != null) {
                        EditText editTextDate = cardView.findViewById(R.id.date);
                        EditText editTextTime = cardView.findViewById(R.id.time);
                        EditText editTextTitulo = cardView.findViewById(R.id.title);
                        EditText editTextModerador = cardView.findViewById(R.id.mod);

                        // Obtiene los valores editados
                        String fecha = editTextDate.getText().toString();
                        String hora = editTextTime.getText().toString();
                        String titulo = editTextTitulo.getText().toString();
                        String moderador = editTextModerador.getText().toString();

                        // Actualiza los valores en la actividad correspondiente
                        ActivityModel activity = activities.get(i);
                        activity.setDate(fecha);
                        activity.setTime(hora);
                        activity.setTitle(titulo);
                        activity.setModer(moderador);
                    }
                }
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK){
            selectedImageUri = data.getData();
            eventImage.setImageURI(selectedImageUri);
        }
    }

}