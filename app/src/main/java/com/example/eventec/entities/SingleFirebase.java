package com.example.eventec.entities;

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

    private void loadEventList(){

    }
}