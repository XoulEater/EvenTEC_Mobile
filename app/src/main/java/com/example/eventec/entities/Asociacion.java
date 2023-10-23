package com.example.eventec.entities;

import java.util.List;

public class Asociacion {
    private String nombreAso;
    private String carrera;
    private String password;
    private String email;
    private String phone;
    private String descripcion;
    private List<String> miembros;

    public Asociacion(String nombreAso, String carrera, String password, String email, String phone, String descripcion, List<String> miembros) {
        this.nombreAso = nombreAso;
        this.carrera = carrera;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.descripcion = descripcion;
        this.miembros = miembros;
    }

    public String getNombreAso() {
        return nombreAso;
    }

    public void setNombreAso(String nombreAso) {
        this.nombreAso = nombreAso;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<String> miembros) {
        this.miembros = miembros;
    }
}
