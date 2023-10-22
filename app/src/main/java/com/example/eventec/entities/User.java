package com.example.eventec.entities;

public class User {
    private String name;
    private String carnet;
    private String password;
    private String email;
    private String phone;
    private String sede;
    private String carrera;

    public User(String name, String carnet, String password, String email, String phone, String sede, String carrera) {
        this.name = name;
        this.carnet = carnet;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.sede = sede;
        this.carrera = carrera;
    }

    // Getter and Setter methods:
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCarnet() {
        return carnet;
    }
    public void setCarnet(String carnet) {
        this.carnet = carnet;
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

    public String getSede() {
        return sede;
    }
    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getCarrera() {
        return carrera;
    }
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }


}
