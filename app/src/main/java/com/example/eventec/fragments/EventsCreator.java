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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventec.R;
import com.example.eventec.activities.MainScreen;
import com.example.eventec.activities.Registro;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.CollabModel;
import com.example.eventec.entities.EmptyActivityAdapter;
import com.example.eventec.entities.EmptyCollabAdapter;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.SingleFirebase;

import java.util.ArrayList;
import java.util.List;


public class EventsCreator extends Fragment {
    private final int GALLERY_REQ_CODE = 1000;
    private Uri selectedImageUri;
    private ImageView eventImage;
    private TextView btnSubmit;
    private ImageButton btnGallery;
    private ImageButton btnAddAct;
    private ImageButton btnDelAct;
    private ImageButton btnAddCollab;
    private ImageButton btnDelCollab;
    private RecyclerView actRV;
    private RecyclerView collabRV;
    private ArrayList<ActivityModel> activities;
    private ArrayList<CollabModel> collabs;
    private EmptyActivityAdapter actAdapter;
    private EmptyCollabAdapter collabAdapter;
    private View mainView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_events_creator, container, false);
        // Inflate the layout for this fragment

        eventImage = mainView.findViewById(R.id.eventImage);
        btnGallery = mainView.findViewById(R.id.btnGallery);
        btnAddAct = mainView.findViewById(R.id.imageButton4);
        btnDelAct = mainView.findViewById(R.id.imageButton3);
        btnAddCollab = mainView.findViewById(R.id.imageButton5);
        btnDelCollab = mainView.findViewById(R.id.imageButton6);
        btnSubmit = mainView.findViewById(R.id.submit);

        actRV = mainView.findViewById(R.id.recyclerView);
        activities = new ArrayList<ActivityModel>(); // Inicializa la lista de actividades
        actAdapter = new EmptyActivityAdapter(getContext(), activities);
        actRV.setLayoutManager(new LinearLayoutManager(getContext()));
        actRV.setAdapter(actAdapter);

        collabRV = mainView.findViewById(R.id.recyclerView2);
        collabs = new ArrayList<CollabModel>(); // Inicializa la lista de colaboradores
        collabAdapter = new EmptyCollabAdapter(getContext(), collabs);
        collabRV.setLayoutManager(new LinearLayoutManager(getContext()));
        collabRV.setAdapter(collabAdapter);

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
        btnAddCollab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollabModel collab = new CollabModel("Nombre", "Cargo", R.drawable.no_image);
                collabAdapter.addCollab(collab);
            }
        });
        btnDelCollab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collabAdapter.removeCollab();
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
                for (int i = 0; i < collabs.size(); i++) {
                    View cardView = collabRV.getLayoutManager().findViewByPosition(i); // Encuentra la vista de la tarjeta en la posición i
                    if (cardView != null) {
                        EditText nameET = cardView.findViewById(R.id.name);
                        EditText jobET = cardView.findViewById(R.id.job);

                        // Obtiene los valores editados
                        String nombre = nameET.getText().toString();
                        String cargo = jobET.getText().toString();

                        // Actualiza los valores en la actividad correspondiente
                        CollabModel collab = collabs.get(i);
                        collab.setJob(cargo);
                        collab.setName(nombre);
                    }
                }
                EditText title = mainView.findViewById(R.id.editTextText);
                EditText description = mainView.findViewById(R.id.editTextTextMultiLine);
                EditText requirements = mainView.findViewById(R.id.editTextTextMultiLine2);
                EditText cat1 = mainView.findViewById(R.id.editTextText3);
                EditText cat2 = mainView.findViewById(R.id.editTextText4);
                EditText cat3 = mainView.findViewById(R.id.editTextText5);
                EditText startDate = mainView.findViewById(R.id.editTextDate5);
                EditText endDate = mainView.findViewById(R.id.editTextDate2);
                EditText capacity = mainView.findViewById(R.id.editTextNumber);
                EditText places = mainView.findViewById(R.id.editTextTextMultiLineLugares);

                try {
                    String titulo = title.getText().toString();
                    String descripcion = description.getText().toString();
                    String requisitos = requirements.getText().toString();
                    List<String> categorias = new ArrayList<String>();
                    categorias.add(cat1.getText().toString());
                    categorias.add(cat2.getText().toString());
                    categorias.add(cat3.getText().toString());
                    String fechaInicio = startDate.getText().toString();
                    String fechaFin = endDate.getText().toString();
                    int imagen = R.drawable.events; // Integer.parseInt(selectedImageUri.toString());
                    int capacidad = Integer.parseInt(capacity.getText().toString());
                    String lugares = places.getText().toString();
                    SingleFirebase singleFirebase = SingleFirebase.getInstance();

                    // Upload event to Firebase
                    if (!titulo.isEmpty() && !descripcion.isEmpty() && !requisitos.isEmpty() && !categorias.isEmpty() &&
                            !fechaInicio.isEmpty() && !fechaFin.isEmpty() && !lugares.isEmpty() && !activities.isEmpty()
                            && !collabs.isEmpty() && capacidad > 0) {
                        singleFirebase.uploadEvent(mainView, getContext(),titulo, capacidad, imagen, categorias, descripcion, requisitos, fechaInicio, fechaFin, lugares, activities, collabs);
                    } else {
                        Toast.makeText(getContext(), "Llene todos los campos", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Llene todos los campos", Toast.LENGTH_LONG).show();
                }
            }

        });

        return mainView;
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