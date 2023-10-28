package com.example.eventec.entities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.eventec.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SingleFirebase {
    // Atributo privado para almacenar la única instancia de la clase
    private static SingleFirebase instance;
    private ArrayList<EventModel> eventModelArrayList;
    private HashMap<String, EventModel> eventModelHashMap;

    private int currentUserType;
    private String currentUserCarnet;
    private String currentAsoName;
    private String currentAsoUser;


    private FirebaseDatabase database;

    private DatabaseReference myRef;
    private SingleFirebase() {
        loadEventList();
        currentUserCarnet = null;
        currentUserType = 1;
        currentAsoName = null;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        eventModelArrayList = new ArrayList<EventModel>();
        eventModelHashMap = new HashMap<String, EventModel>();
    }

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

    public static SingleFirebase getInstance() {
        // Método para obtener la instancia única de la clase.
        if (instance == null) {
            instance = new SingleFirebase();

        }
        return instance;
    }

    public void refreshEventList(){
        // descarga lista de eventos de firebase
        eventModelArrayList = new ArrayList<EventModel>();
        eventModelHashMap = new HashMap<String, EventModel>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("eventos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    HashMap<String, HashMap<?, ?>> events = (HashMap<String, HashMap<?, ?>>) task.getResult().getValue();
                    Set<String> eventIds = events.keySet();
                    for (String eventId : eventIds){
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

                        for (HashMap<String, ?> activityMap : activitiesList){
                                String dateActivity = activityMap.get("date").toString();
                                String time = activityMap.get("time").toString();
                                String title = activityMap.get("title").toString();
                                String moder = activityMap.get("moder").toString();
                                activity = new ActivityModel(dateActivity, time, title, moder);
                                activities.add(activity);

                        }
                        CollabModel collab;
                        for (HashMap<String, ?> colabMap : colabsList){
                                String job = colabMap.get("job").toString();
                                String name = colabMap.get("name").toString();
                                Integer profileImage = Integer.parseInt(colabMap.get("profileImage").toString());
                                collab = new CollabModel(name, job, profileImage);
                                colabs.add(collab);
                        }

                        EventModel eventModel = new EventModel(eventId, titulo, date, nombreAsociacion, capacidad, imagenSrc, categorias, descripcion, requerimientos, fechaInicio, fechaFin, lugares, activities, colabs, clicks, cupos, userAsociacion);
                        eventModelArrayList.add(eventModel);
                        eventModelHashMap.put(eventId, eventModel);
                    }
                    Log.d("Firebase", eventModelHashMap.toString());
                }
            }
        });
    }

    public ArrayList<EventModel> getEventModelArrayList() {
        return eventModelArrayList;
    }

    private void loadEventList(){
        // descargar eventos de firebase
        refreshEventList();
//        ArrayList<String> categories = new ArrayList<String>();
//        categories.add("Musica");
//        categories.add("Premios");
//        categories.add("Tecnologia");
//
//        this.eventModelArrayList = new ArrayList<EventModel>();
//        EventModel event1 = new EventModel("Semana ATI", "Asocia de ATI", 500, R.drawable.des, categories, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "Ser estudiante", "02/09/2023", "09/09/2023");
//        EventModel event2 = new EventModel("Semana Compu", "Asocia de Compu", 500, R.drawable.no_image, categories, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "Ser estudiante", "10/09/2023", "17/09/2023");
//
//        ArrayList<CollabModel> collabs1 = new ArrayList<CollabModel>();
//        ArrayList<CollabModel> collabs2 = new ArrayList<CollabModel>();
//
//        CollabModel collab0 = new CollabModel("Asocia de ATI", "Asociación", R.drawable.no_image);
//        CollabModel collab1 = new CollabModel("Raul", "Moderador", R.drawable.no_image);
//        CollabModel collab2 = new CollabModel("Miguel", "Moderador", R.drawable.no_image);
//        collabs1.add(collab0);
//        collabs1.add(collab1);
//        collabs1.add(collab2);
//        event1.setCollabModelArrayList(collabs1);
//
//        CollabModel collab3 = new CollabModel("Asocia de Compu", "Asociación", R.drawable.no_image);
//        CollabModel collab4 = new CollabModel("Saul", "Moderador", R.drawable.no_image);
//        CollabModel collab5 = new CollabModel("Juan", "Moderador", R.drawable.no_image);
//        collabs2.add(collab3);
//        collabs2.add(collab4);
//        collabs2.add(collab5);
//        event2.setCollabModelArrayList(collabs2);
//
//        ArrayList<ActivityModel> acts1 = new ArrayList<ActivityModel>();
//        ArrayList<ActivityModel> acts2 = new ArrayList<ActivityModel>();
//
//        ActivityModel act1 = new ActivityModel("03/09/2023", "11:00 am", "Bingo", "Juan");
//        ActivityModel act2 = new ActivityModel("03/09/2023", "12:00 am", "Baile", "Juancho");
//        ActivityModel act3 = new ActivityModel("04/09/2023", "11:30 am", "Torneo", "Juanpa");
//        acts1.add(act1);
//        acts1.add(act2);
//        acts1.add(act3);
//        event1.setActivityModelArrayList(acts1);
//
//        ActivityModel act4 = new ActivityModel("13/09/2023", "11:00 am", "Bingo", "Ale");
//        ActivityModel act5 = new ActivityModel("13/09/2023", "12:00 am", "Baile", "Alex");
//        ActivityModel act6 = new ActivityModel("14/09/2023", "11:30 am", "Torneo", "Alejo");
//        acts2.add(act4);
//        acts2.add(act5);
//        acts2.add(act6);
//        event2.setActivityModelArrayList(acts2);
//
//        eventModelArrayList.add(event1);
//        eventModelArrayList.add(event2);
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public void uploadEvent(String titulo, int capacidad, int imagenSrc, List<String> categorias, String descripcion, String requerimientos, String fechaInicio, String fechaFin, String lugares, List<ActivityModel> activities, List<CollabModel> colabs){
        DatabaseReference eventNode = myRef.child("eventos").push();
        String eventId = eventNode.getKey();
        EventModel event = new EventModel(eventId, titulo, this.currentAsoUser, this.currentAsoName, capacidad, imagenSrc, categorias, descripcion, requerimientos, fechaInicio, fechaFin, lugares, activities, colabs, 0, 0);
        eventModelArrayList.add(event);
        eventModelHashMap.put(eventId, event);
        eventNode.setValue(event);
    }

}