package com.example.eventec.entities;

import java.util.ArrayList;
import java.util.List;

public class EventModel {
    private String eventId;
    private String titulo;
    private String date;
    private String nombreAsociacion;
    private int capacidad;
    private int imagenSrc;
    private List<String> categorias;
    private String descripcion;
    private String requerimientos;
    private String fechaInicio;
    private String fechaFin;
    private String lugares;
    private List<ActivityModel> activities;
    private List<CollabModel> colabs;
    private int clicks;
    private int cupos;
    private String userAsociacion;

    public void formatDate(String fecha){
        String[] parts = fecha.split("/");
        String day = parts[2];
        String month = parts[1];
        switch(month){
            case "01":
                month = "Ene";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Abr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Ago";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dic";
                break;
        }
        String newDate = day + " de " + month;
        this.date = newDate;
    }
    public EventModel(String eventId, String titulo, String userAsociacion, String nombreAsociacion, int capacidad, int imagenSrc, List<String> categorias, String descripcion, String requerimientos, String fechaInicio, String fechaFin, String lugares, List<ActivityModel> activities, List<CollabModel> colabs, int clicks, int cupos) {
        this.eventId = eventId;
        this.titulo = titulo;
        formatDate(fechaInicio);
        this.userAsociacion = userAsociacion;
        this.nombreAsociacion = nombreAsociacion;
        this.capacidad = capacidad;
        this.imagenSrc = imagenSrc;
        this.categorias = categorias;
        this.descripcion = descripcion;
        this.requerimientos = requerimientos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugares = lugares;
        this.activities = activities;
        this.colabs = colabs;
        this.clicks = clicks;
        this.cupos = cupos;

    }

    public EventModel(String eventId, String titulo, String date, String nombreAsociacion, int capacidad, int imagenSrc, List<String> categorias, String descripcion, String requerimientos, String fechaInicio, String fechaFin, String lugares, List<ActivityModel> activities, List<CollabModel> colabs, int clicks, int cupos, String userAsociacion) {
        this.eventId = eventId;
        this.titulo = titulo;
        this.date = date;
        this.nombreAsociacion = nombreAsociacion;
        this.capacidad = capacidad;
        this.imagenSrc = imagenSrc;
        this.categorias = categorias;
        this.descripcion = descripcion;
        this.requerimientos = requerimientos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugares = lugares;
        this.activities = activities;
        this.colabs = colabs;
        this.clicks = clicks;
        this.cupos = cupos;
        this.userAsociacion = userAsociacion;
    }

    public EventModel(String eventId, String titulo, String date, String nombreAsociacion, int capacidad, int imagenSrc, List<String> categorias, String descripcion, String requerimientos, String fechaInicio, String fechaFin, String lugares) {
        this.eventId = eventId;
        this.titulo = titulo;
        this.date = date;
        this.nombreAsociacion = nombreAsociacion;
        this.capacidad = capacidad;
        this.imagenSrc = imagenSrc;
        this.categorias = categorias;
        this.descripcion = descripcion;
        this.requerimientos = requerimientos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugares = lugares;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNombreAsociacion() {
        return nombreAsociacion;
    }

    public void setNombreAsociacion(String nombreAsociacion) {
        this.nombreAsociacion = nombreAsociacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getImagenSrc() {
        return imagenSrc;
    }

    public void setImagenSrc(int imagenSrc) {
        this.imagenSrc = imagenSrc;
    }


    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<String> categorias) {
        this.categorias = categorias;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(String requerimientos) {
        this.requerimientos = requerimientos;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getLugares() {
        return lugares;
    }

    public void setLugares(String lugares) {
        this.lugares = lugares;
    }

    public List<ActivityModel> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<ActivityModel> activities) {
        this.activities = activities;
    }

    public List<CollabModel> getColabs() {
        return colabs;
    }

    public void setColabs(ArrayList<CollabModel> colabs) {
        this.colabs = colabs;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public String getUserAsociacion() {
        return userAsociacion;
    }

    public void setUserAsociacion(String userAsociacion) {
        this.userAsociacion = userAsociacion;
    }
}

