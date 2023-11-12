package com.example.eventec.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventec.R;
import com.example.eventec.entities.SingleFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Clase que maneja el login de los usuarios
public class Login extends AppCompatActivity {
    private int currentUserType; // 0 = Estudiante, 2 = Asociacion
    private SingleFirebase singleFirebase; // Instancia de la clase SingleFirebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserType = getIntent().getIntExtra("userType", 0); // Obtener el tipo de usuario
        singleFirebase = SingleFirebase.getInstance(); // Obtener la instancia de SingleFirebase
        // Dependiendo del tipo de usuario, se muestra una interfaz diferente
        switch (currentUserType){
            case 1:
                break;
            case 2:
                setContentView(R.layout.activity_login_a);
                break;
            default:
                setContentView(R.layout.activity_login_e);
        }

    }

    // Función que lleva a la pantalla de registro.
    public void registro (View view){
        Intent siguiente = new Intent(this, Registro.class);
        siguiente.putExtra("userType", currentUserType);
        startActivity(siguiente);
    }

    // Función que valida con Firebase.
    public void login (View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // Instancia de la base de datos
        DatabaseReference myRef = database.getReference(); //  Referencia a la base de datos

        // Dependiendo del tipo de usuario, se valida de una manera diferente
        // TODO: comenten esta obra arquitectonica de la ingenieria de software
        switch (currentUserType){
            case 1: // Si es 1 no se hace nada
                break;
            case 2:
                // Si es una asociación, se leen los valores de los campos de texto
                TextView asocianameText = findViewById(R.id.asocianame); // Usuario de asociación, no display name
                String asocianame = asocianameText.getText().toString();
                TextView passwordText = findViewById(R.id.password);
                String password = passwordText.getText().toString();

                // Si están vacíos, se envía un mensaje
                if (asocianame.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Llene todos los campos", Toast.LENGTH_LONG).show();
                    return;
                }
                // Se leen intenta leer la asociación con el usuario dado de la base de datos.
                myRef.child("asociaciones").child (asocianame).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            DataSnapshot dataSnapshot = task.getResult();
                            if (dataSnapshot.exists()){
                                // Si la asociación existe, está registrada

                                // Si la contraseña dada es la misma que la de la asociación, y la asociación está habilitada
                                // se inicia la sesión.
                                if (dataSnapshot.child("password").getValue().toString().equals(password) && dataSnapshot.child("enabled").getValue().equals(true)){
                                    singleFirebase.setCurrentAsoUser(asocianame); // Guardar el usuario de la asociación
                                    singleFirebase.setCurrentAsoName(dataSnapshot.child("nombreAso").getValue().toString()); // Guardar el nombre de la asociación
                                    singleFirebase.setCurrentUserType(currentUserType); // Guardar el tipo de usuario
                                    continuar(view);
                                } else {
                                    // Si la contraseña es incorrecta o la asociación está deshabilitada, se envía un mensaje
                                    Toast.makeText(Login.this, "Contraseña incorrecta o asociación fue borrada", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // Si no se encuentra el usuario, la asocicación no existe
                                Toast.makeText(Login.this, "Asociación no existe", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                break;
            default:
                // Si es un estudiante
                // Se lee el carnet y la contraseña
                TextView carnetText = findViewById(R.id.carnet);
                String carnet = carnetText.getText().toString();
                TextView passwordUserText = findViewById(R.id.password);
                String passwordUser = passwordUserText.getText().toString();

                // Si están vacíos, se envía un mensaje
                if (carnet.isEmpty() || passwordUser.isEmpty()){
                    Toast.makeText(Login.this, "Llene todos los campos", Toast.LENGTH_LONG).show();
                    return;
                }

                // Se intenta leer el usuario con el carnet dado de la base de datos
                myRef.child("users").child (carnet).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            DataSnapshot dataSnapshot = task.getResult();

                            // Si existe el carnet, se revisa si la contraseña dada es la que está registrada
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.child("password").getValue().toString().equals(passwordUser)){
                                    singleFirebase.setCurrentUserCarnet(carnet); // Guardar el carnet del usuario
                                    singleFirebase.setCurrentUserType(currentUserType); // Guardar el tipo de usuario
                                    singleFirebase.setCurrentUserEmail(dataSnapshot.child("email").getValue().toString()); // Guardar el correo del usuario
                                    singleFirebase.setCurrentUsername(dataSnapshot.child("name").getValue().toString()); // Guardar el nombre de usuario
                                    continuar(view);
                                } else {
                                    // Si la contraseña es incorrecta, se envía un mensaje
                                    Toast.makeText(Login.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // Si no se encuentra el usuario, el carnet no existe
                                Toast.makeText(Login.this, "Usuario no existe", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                break;
        }
    }
    // Función que lleva a la pantalla principal.
    public void continuar(View view){
        Intent siguiente = new Intent(this, MainScreen.class);
        siguiente.putExtra("userType", currentUserType);
        startActivity(siguiente);
    }
}