package com.example.eventec.entities;

import com.example.eventec.R;

import java.util.ArrayList;

public class SingleFirebase {
    // Atributo privado para almacenar la única instancia de la clase
    private static SingleFirebase instance;
    private ArrayList<EventModel> eventModelArrayList;


    private SingleFirebase() {
        loadEventList();
    }

    public static SingleFirebase getInstance() {
        // Método para obtener la instancia única de la clase.
        if (instance == null) {
            instance = new SingleFirebase();
        }
        return instance;
    }

    public ArrayList<EventModel> getEventModelArrayList() {
        return eventModelArrayList;
    }

    private void loadEventList(){
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Musica");
        categories.add("Premios");
        categories.add("Tecnologia");

        this.eventModelArrayList = new ArrayList<EventModel>();
        EventModel event1 = new EventModel("Semana ATI", "2 de Sep", "Asocia de ATI", 500, R.drawable.des, categories, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "Ser estudiante", "02/09/2023", "09/09/2023", 20);
        EventModel event2 = new EventModel("Semana Compu", "10 de Sep", "Asocia de Compu", 500, R.drawable.no_image, categories, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "Ser estudiante", "10/09/2023", "17/09/2023", 20);

        ArrayList<CollabModel> collabs1 = new ArrayList<CollabModel>();
        ArrayList<CollabModel> collabs2 = new ArrayList<CollabModel>();

        CollabModel collab0 = new CollabModel("Asocia de ATI", "Asociación", R.drawable.no_image);
        CollabModel collab1 = new CollabModel("Raul", "Moderador", R.drawable.no_image);
        CollabModel collab2 = new CollabModel("Miguel", "Moderador", R.drawable.no_image);
        collabs1.add(collab0);
        collabs1.add(collab1);
        collabs1.add(collab2);
        event1.setCollabModelArrayList(collabs1);

        CollabModel collab3 = new CollabModel("Asocia de Compu", "Asociación", R.drawable.no_image);
        CollabModel collab4 = new CollabModel("Saul", "Moderador", R.drawable.no_image);
        CollabModel collab5 = new CollabModel("Juan", "Moderador", R.drawable.no_image);
        collabs2.add(collab3);
        collabs2.add(collab4);
        collabs2.add(collab5);
        event2.setCollabModelArrayList(collabs2);

        ArrayList<ActivityModel> acts1 = new ArrayList<ActivityModel>();
        ArrayList<ActivityModel> acts2 = new ArrayList<ActivityModel>();

        ActivityModel act1 = new ActivityModel("03/09/2023", "11:00 am", "Bingo", "Juan");
        ActivityModel act2 = new ActivityModel("03/09/2023", "12:00 am", "Baile", "Juancho");
        ActivityModel act3 = new ActivityModel("04/09/2023", "11:30 am", "Torneo", "Juanpa");
        acts1.add(act1);
        acts1.add(act2);
        acts1.add(act3);
        event1.setActivityModelArrayList(acts1);

        ActivityModel act4 = new ActivityModel("13/09/2023", "11:00 am", "Bingo", "Ale");
        ActivityModel act5 = new ActivityModel("13/09/2023", "12:00 am", "Baile", "Alex");
        ActivityModel act6 = new ActivityModel("14/09/2023", "11:30 am", "Torneo", "Alejo");
        acts2.add(act4);
        acts2.add(act5);
        acts2.add(act6);
        event2.setActivityModelArrayList(acts2);

        eventModelArrayList.add(event1);
        eventModelArrayList.add(event2);
    }
}