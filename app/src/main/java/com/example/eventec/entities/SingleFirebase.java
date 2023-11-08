package com.example.eventec.entities;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventec.R;
import com.example.eventec.activities.Login;
import com.example.eventec.activities.MainScreen;
import com.example.eventec.activities.Registro;
import com.example.eventec.activities.StartActivity;
import com.example.eventec.fragments.EventsCreator;
import com.example.eventec.fragments.EventsDisplay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Clase que implementa el patrón Singleton para tener una única instancia de la clase
public class SingleFirebase {
    // Atributo privado para almacenar la única instancia de la clase
    private static SingleFirebase instance;
    private ArrayList<EventModel> eventModelArrayList; // lista de eventos
    private HashMap<String, EventModel> eventModelHashMap;
    private int currentUserType;
    private String currentUserCarnet;
    private String currentUsername;
    private String currentUserEmail;
    private String currentAsoName = "N/A";
    private String currentAsoUser = "N/A";

    private FirebaseDatabase database; // instancia de la base de datos

    private DatabaseReference myRef; // referencia a la base de datos

    private MainScreen mainScreen; // referencia a la pantalla principal

    // Constructor privado para evitar que se puedan crear instancias desde otras
    private SingleFirebase() {
        currentUserCarnet = null;
        currentUserType = 1;
        currentAsoName = null;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        eventModelArrayList = new ArrayList<EventModel>();
        eventModelHashMap = new HashMap<String, EventModel>();
        refreshEventList();
    }

    // Getters y setters
    public int getCurrentUserType() {
        return currentUserType;
    }

    public void setCurrentUserType(int currentUserType) {
        this.currentUserType = currentUserType;
    }

    public String getCurrentUserCarnet() {
        return currentUserCarnet;
    }

    public void setCurrentUserCarnet(String currentUserCarnet) {
        this.currentUserCarnet = currentUserCarnet;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public String getCurrentAsoName() {
        return currentAsoName;
    }

    public void setCurrentAsoName(String currentAsoName) {
        this.currentAsoName = currentAsoName;
    }

    public String getCurrentAsoUser() {
        return currentAsoUser;
    }

    public void setCurrentAsoUser(String currentAsoUser) {
        this.currentAsoUser = currentAsoUser;
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    public ArrayList<EventModel> getEventModelArrayList() {
        return eventModelArrayList;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public static SingleFirebase getInstance() {
        // Método para obtener la instancia única de la clase.
        if (instance == null) {
            instance = new SingleFirebase();

        }
        return instance;
    }

    // método para cerrar sesión
    public void logout(Context context) {
        currentUserCarnet = null;
        currentUserType = 1;
        currentAsoName = null;
        currentAsoUser = null;
        Intent siguiente = new Intent(context, StartActivity.class);
        context.startActivity(siguiente);
    }

    // descarga lista de eventos de firebase
    public void refreshEventList() {
        // descarga lista de eventos de firebase
        eventModelArrayList = new ArrayList<EventModel>();
        eventModelHashMap = new HashMap<String, EventModel>();

        // TODO: comenten esta obra arquitectonica de la ingenieria de software
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("eventos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    HashMap<String, HashMap<?, ?>> events = (HashMap<String, HashMap<?, ?>>) task.getResult()
                            .getValue();
                    Set<String> eventIds = events.keySet();
                    for (String eventId : eventIds) {
                        HashMap<?, ?> event = events.get(eventId);
                        String titulo = event.get("titulo").toString();

                        String nombreAsociacion = event.get("nombreAsociacion").toString();
                        int capacidad = Integer.parseInt(event.get("capacidad").toString());
                        int imagenSrc = Integer.parseInt(event.get("imagenSrc").toString());
                        List<String> categorias = (List<String>) event.get("categorias");
                        String date = event.get("date").toString();
                        String descripcion = event.get("descripcion").toString();
                        String requerimientos = event.get("requerimientos").toString();
                        String fechaInicio = event.get("fechaInicio").toString();
                        String fechaFin = event.get("fechaFin").toString();
                        String lugares = event.get("lugares").toString();
                        int clicks = Integer.parseInt(event.get("clicks").toString());
                        int cupos = Integer.parseInt(event.get("cupos").toString());
                        String userAsociacion = event.get("userAsociacion").toString();
                        List<HashMap<String, ?>> activitiesList = (List<HashMap<String, ?>>) event.get("activities");
                        List<HashMap<String, ?>> colabsList = (List<HashMap<String, ?>>) event.get("colabs");
                        List<ActivityModel> activities = new ArrayList<ActivityModel>();
                        List<CollabModel> colabs = new ArrayList<CollabModel>();
                        ActivityModel activity;

                        for (HashMap<String, ?> activityMap : activitiesList) {
                            String dateActivity = activityMap.get("date").toString();
                            String time = activityMap.get("time").toString();
                            String title = activityMap.get("title").toString();
                            String moder = activityMap.get("moder").toString();
                            activity = new ActivityModel(dateActivity, time, title, moder);
                            activities.add(activity);

                        }
                        CollabModel collab;
                        for (HashMap<String, ?> colabMap : colabsList) {
                            String job = colabMap.get("job").toString();
                            String name = colabMap.get("name").toString();
                            Integer profileImage = Integer.parseInt(colabMap.get("profileImage").toString());
                            collab = new CollabModel(name, job, profileImage);
                            colabs.add(collab);
                        }

                        EventModel eventModel = new EventModel(eventId, titulo, date, nombreAsociacion, capacidad,
                                imagenSrc, categorias, descripcion, requerimientos, fechaInicio, fechaFin, lugares,
                                activities, colabs, clicks, cupos, userAsociacion);
                        eventModelArrayList.add(eventModel);
                        eventModelHashMap.put(eventId, eventModel);
                    }
                    Log.d("Firebase", eventModelHashMap.toString());
                }
            }
        });
    }

    // carga lista de eventos de firebase
    public void uploadEvent(View mainView, Context context, String titulo, int capacidad, int imagenSrc,
            List<String> categorias, String descripcion, String requerimientos, String fechaInicio, String fechaFin,
            String lugares, List<ActivityModel> activities, List<CollabModel> colabs) {
        try {
            // TODO: comenten esta obra arquitectonica de la ingenieria de software
            myRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        boolean notExists = true;
                        HashMap<String, HashMap<?, ?>> users = (HashMap<String, HashMap<?, ?>>) task.getResult()
                                .getValue();
                        Set<String> carnetUsers = users.keySet();
                        for (CollabModel colab : colabs) {
                            for (String carnet : carnetUsers) {
                                if (users.get(carnet).get("name").equals(colab.getName())) {
                                    notExists = false;
                                    break;
                                }
                            }

                        }
                        if (notExists) {
                            Toast.makeText(context, "ERROR: Alguno de los colaboradores no existe.", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            String path = (currentUserType == 1) ? "eventos" : "propuestas";
                            String displayName = (currentUserType == 1) ? currentAsoName : currentUsername;
                            String currentUser = (currentUserType == 1) ? currentAsoUser : currentUserCarnet;

                            DatabaseReference eventNode = myRef.child(path).push();

                            String eventId = eventNode.getKey();
                            EventModel event = new EventModel(eventId, titulo, currentUser, displayName, capacidad,
                                    imagenSrc, categorias, descripcion, requerimientos, fechaInicio, fechaFin, lugares,
                                    activities, colabs, 0, 0);
                            eventModelArrayList.add(event);
                            eventModelHashMap.put(eventId, event);
                            eventNode.setValue(event);
                            Toast.makeText(context, "Evento creado con éxito.", Toast.LENGTH_LONG).show();

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
                            title.setText("Título");
                            description.setText("");
                            requirements.setText("");
                            cat1.setText("");
                            cat2.setText("");
                            cat3.setText("");
                            startDate.setText("");
                            endDate.setText("");
                            capacity.setText("");
                            places.setText("");
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, "ERROR creando evento.", Toast.LENGTH_LONG).show();
        }
    }

    // incrementa el numero de clicks de un evento
    public void incrementarClicksEvento(EventModel event) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("eventos/" + event.getEventId() + "/clicks", ServerValue.increment(1));
        myRef.updateChildren(updates);
        event.incrementClicks();
    }

    // Consigue un evento por su id del hashMap de eventos
    public EventModel getEventById(String eventId){
        return eventModelHashMap.get(eventId);
    }
}