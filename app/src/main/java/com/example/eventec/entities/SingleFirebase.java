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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Clase que implementa el patrón Singleton para tener una única instancia de la clase
public class SingleFirebase {
    // Atributo privado para almacenar la única instancia de la clase
    private static SingleFirebase instance;

    private ArrayList<AlertModel> alertModelArrayList; // lista de alertas
    private ArrayList<EventModel> eventModelArrayList; // lista de eventos
    private ArrayList<EventModel> propsModelArrayList; // lista de propuestas
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
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        refreshEventList();
        refreshAlertList();
        refreshPropsList();
    }
    public static SingleFirebase getInstance() {
        // Método para obtener la instancia única de la clase.
        if (instance == null) {
            instance = new SingleFirebase();

        }
        return instance;
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

    public ArrayList<EventModel> getEventModelArrayList() {
        return eventModelArrayList;
    }

    public ArrayList<AlertModel> getAlertModelArrayList() {
        return alertModelArrayList;
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public ArrayList<EventModel> getPropsModelArrayList() {
        return propsModelArrayList;
    }

    // Listeners
    public interface AlertsListener { // Listener para cuando se carguen las alertas
        void onAlertsLoaded(ArrayList<AlertModel> alertModelArrayList);
    }

    // Variable para guardar el listener
    private AlertsListener alertsListener;

    // Método para establecer el listener
    public void setAlertsListener(AlertsListener alertsListener) {
        this.alertsListener = alertsListener;
    }

    public interface EventsListener { // Listener para cuando se carguen los eventos
        void onEventsLoaded(ArrayList<EventModel> eventModelArrayList);
    }

    // Variable para guardar el listener
    private EventsListener eventsListener;

    // Método para establecer el listener
    public void setEventsListener(EventsListener eventsListener) {
        this.eventsListener = eventsListener;
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

    // Descarga lista de propuestas de firebase
    public void refreshPropsList() {
        propsModelArrayList = new ArrayList<EventModel>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // Se leen las propuestas
        myRef.child("propuestas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().exists()){
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        HashMap<String, HashMap<?, ?>> events = (HashMap<String, HashMap<?, ?>>) task.getResult()
                                .getValue();
                        Set<String> eventIds = events.keySet();

                        // Por cada propuesta, se obtienen los campos y se crea un objeto EventModel y se agrega a la lista y el hashmap.
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

                            // Por cada actividad, se crea un objeto nuevo
                            for (HashMap<String, ?> activityMap : activitiesList) {
                                String dateActivity = activityMap.get("date").toString();
                                String time = activityMap.get("time").toString();
                                String title = activityMap.get("title").toString();
                                String moder = activityMap.get("moder").toString();
                                activity = new ActivityModel(dateActivity, time, title, moder);
                                activities.add(activity);

                            }

                            // Por cada colaborador, se crea un objeto nuevo.
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

                            propsModelArrayList.add(eventModel);
                        }
                        Log.d("Firebase", eventModelArrayList.toString());
                        if (eventsListener != null) {
                            eventsListener.onEventsLoaded(propsModelArrayList);
                        }
                    }
                }
            }
        });
    }

    // Descarga lista de alertas de firebase
    public void refreshAlertList(){
        alertModelArrayList = new ArrayList<AlertModel>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // Se leen las alertas
        myRef.child("alertas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().exists()){
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        HashMap<String, HashMap<?, ?>> alerts = (HashMap<String, HashMap<?, ?>>) task.getResult()
                                .getValue();
                        Set<String> alertIds = alerts.keySet();

                        // Por cada alerta, se obtienen los campos y se crea un objeto AlertModel y se agrega a la lista.
                        for (String alertId : alertIds) {
                            String subject = alerts.get(alertId).get("subject").toString();
                            String body = alerts.get(alertId).get("body").toString();
                            String date = alerts.get(alertId).get("postdate").toString();
                            int imageSrc = Integer.parseInt(alerts.get(alertId).get("alertImage").toString());
                            AlertModel alertModel = new AlertModel(subject, body, date, imageSrc);
                            alertModelArrayList.add(alertModel);
                        }
                        Log.d("Firebase", alertModelArrayList.toString());
                        // ordena la lista de alertas por fecha con formato dd/MM/yyyy, hh:mm:ss a
                        alertModelArrayList.sort((o1, o2) -> {
                            String[] date1 = o1.getPostdate().split(",")[0].split("/");
                            String[] date2 = o2.getPostdate().split(",")[0].split("/");
                            String[] time1 = o1.getPostdate().split(",")[1].trim().split(":");
                            String[] time2 = o2.getPostdate().split(",")[1].trim().split(":");
                            int year1 = Integer.parseInt(date1[2]);
                            int year2 = Integer.parseInt(date2[2]);
                            int month1 = Integer.parseInt(date1[1]);
                            int month2 = Integer.parseInt(date2[1]);
                            int day1 = Integer.parseInt(date1[0]);
                            int day2 = Integer.parseInt(date2[0]);
                            int hour1 = Integer.parseInt(time1[0]);
                            int hour2 = Integer.parseInt(time2[0]);
                            int minute1 = Integer.parseInt(time1[1]);
                            int minute2 = Integer.parseInt(time2[1]);
                            int second1 = Integer.parseInt(time1[2].split(" ")[0]);
                            int second2 = Integer.parseInt(time2[2].split(" ")[0]);
                            int a1 = (time1[2].split(" ")[1].equals("a.")) ? 0 : 1;
                            int a2 = (time2[2].split(" ")[1].equals("a.")) ? 0 : 1;
                            if (year1 != year2) {
                                return year1 - year2;
                            } else if (month1 != month2) {
                                return month1 - month2;
                            } else if (day1 != day2) {
                                return day1 - day2;
                            } else if (a1 != a2) {
                                return a1 - a2;
                            } else if (hour1 != hour2) {
                                return hour1 - hour2;
                            } else if (minute1 != minute2) {
                                return minute1 - minute2;
                            } else {
                                return second1 - second2;
                            }
                        });
                        // revierte la lista para que la alerta más reciente esté de primera
                        ArrayList<AlertModel> reversedAlerts = new ArrayList<AlertModel>();
                        for (int i = alertModelArrayList.size() - 1; i >= 0; i--) {
                            reversedAlerts.add(alertModelArrayList.get(i));
                        }
                        alertModelArrayList = reversedAlerts;

                        if (alertsListener != null) {
                            alertsListener.onAlertsLoaded(alertModelArrayList);
                        }
                    }
                }
            }
        });
    }

    // Descarga lista de eventos de firebase
    public void refreshEventList() {
        // descarga lista de eventos de firebase
        eventModelArrayList = new ArrayList<EventModel>();
        eventModelHashMap = new HashMap<String, EventModel>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // Se leen los eventos
        myRef.child("eventos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    if (task.getResult().exists()){
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        HashMap<String, HashMap<?, ?>> events = (HashMap<String, HashMap<?, ?>>) task.getResult()
                                .getValue();
                        Set<String> eventIds = events.keySet();

                        // Por cada evento, se obtienen los campos y se crea un objeto EventModel y se agrega a la lista y el hashmap.
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

                            // Por cada actividad, se crea un objeto nuevo
                            for (HashMap<String, ?> activityMap : activitiesList) {
                                String dateActivity = activityMap.get("date").toString();
                                String time = activityMap.get("time").toString();
                                String title = activityMap.get("title").toString();
                                String moder = activityMap.get("moder").toString();
                                activity = new ActivityModel(dateActivity, time, title, moder);
                                activities.add(activity);

                            }

                            // Por cada colaborador, se crea un objeto nuevo.
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
                        // ordena la lista de eventos por fecha con formato dd/mm/yy
                        eventModelArrayList.sort((o1, o2) -> {
                            String[] date1 = o1.getFechaInicio().split("/");
                            String[] date2 = o2.getFechaInicio().split("/");
                            int year1 = Integer.parseInt(date1[2]);
                            int year2 = Integer.parseInt(date2[2]);
                            int month1 = Integer.parseInt(date1[1]);
                            int month2 = Integer.parseInt(date2[1]);
                            int day1 = Integer.parseInt(date1[0]);
                            int day2 = Integer.parseInt(date2[0]);
                            if (year1 != year2) {
                                return year1 - year2;
                            } else if (month1 != month2) {
                                return month1 - month2;
                            } else {
                                return day1 - day2;
                            }
                        });


                        if (eventsListener != null) {
                            eventsListener.onEventsLoaded(eventModelArrayList);
                        }
                    }
                }
            }
        });
    }

    // Carga lista de eventos de firebase
    public void uploadEvent(View mainView, Context context, String titulo, int capacidad, int imagenSrc,
            List<String> categorias, String descripcion, String requerimientos, String fechaInicio, String fechaFin,
            String lugares, List<ActivityModel> activities, List<CollabModel> colabs) {
        try {

            // Se leen los usuarios para verificar los colaboradores.
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

                        // Se busca el colaborador por nombre para verificar si existe.
                        for (CollabModel colab : colabs) {
                            for (String carnet : carnetUsers) {
                                if (users.get(carnet).get("name").equals(colab.getName())) {
                                    notExists = false;
                                    break;
                                }
                            }

                        }
                        // Si alguno no existe, se envía el error.
                        if (notExists) {
                            Toast.makeText(context, "ERROR: Alguno de los colaboradores no existe.", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            // Si todos existen, se crea el evento.
                            // Se revisa si el que lo crea es una asociación. Si es una asociación, es un evento.
                            // Si es un estudiante, es una propuesta.
                            String path = (currentUserType == 2) ? "eventos" : "propuestas";
                            String displayName = (currentUserType == 2) ? currentAsoName : currentUsername;
                            String currentUser = (currentUserType == 2) ? currentAsoUser : currentUserCarnet;

                            DatabaseReference eventNode = myRef.child(path).push();

                            String eventId = eventNode.getKey();
                            // Se crea un nuevo objeto con la información del evento.
                            EventModel event = new EventModel(eventId, titulo, currentUser, displayName, capacidad,
                                    imagenSrc, categorias, descripcion, requerimientos, fechaInicio, fechaFin, lugares,
                                    activities, colabs, 0, 0);
                            eventModelArrayList.add(event);
                            eventModelHashMap.put(eventId, event);
                            eventNode.setValue(event); // Se sube a Firebase

                            String text = (currentUserType == 2) ? "Evento" : "Propuesta";
                            Toast.makeText(context, text + " creado con éxito.", Toast.LENGTH_LONG).show();


                            // Crear notificación de nuevo evento
                            if (currentUserType == 2) {
                                String subject = "Nuevo evento creado";
                                String body = "Se ha creado un nuevo evento: " + titulo;
                                // fecha actual
                                String postdate = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    postdate = ZonedDateTime.now(ZoneId.of("America/Costa_Rica"))
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyy, hh:mm:ss a"));
                                }
                                int imageSrc = 3;
                                AlertModel alertModel = new AlertModel(subject, body, postdate, imageSrc);
                                String key = myRef.child("alertas").push().getKey();
                                myRef.child("alertas").child(key).setValue(alertModel);
                            }

                            // Se limpian los campos
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

    // Incrementa el numero de clicks de un evento
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