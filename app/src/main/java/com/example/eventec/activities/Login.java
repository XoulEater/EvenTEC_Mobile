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

public class Login extends AppCompatActivity {
    private int currentUserType;
    private SingleFirebase singleFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserType = getIntent().getIntExtra("userType", 0);
        singleFirebase = SingleFirebase.getInstance();
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
    public void registro (View view){
        Intent siguiente = new Intent(this, Registro.class);
        siguiente.putExtra("userType", currentUserType);
        startActivity(siguiente);
    }

    // Función que valida con Firebase.
    public void login (View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        switch (currentUserType){
            case 1:
                break;
            case 2:
                TextView asocianameText = findViewById(R.id.asocianame); // Usuario de asociación, no display name
                String asocianame = asocianameText.getText().toString();
                TextView passwordText = findViewById(R.id.password);
                String password = passwordText.getText().toString();
                if (asocianame.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Llene todos los campos", Toast.LENGTH_LONG).show();
                    return;
                }
                myRef.child("asociaciones").child (asocianame).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            DataSnapshot dataSnapshot = task.getResult();
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.child("password").getValue().toString().equals(password) && dataSnapshot.child("enabled").getValue().equals(true)){
                                    singleFirebase.setCurrentAsoUser(asocianame);
                                    singleFirebase.setCurrentAsoName(dataSnapshot.child("nombreAso").getValue().toString());
                                    singleFirebase.setCurrentUserType(currentUserType);
                                    continuar(view);
                                } else {
                                    Toast.makeText(Login.this, "Contraseña incorrecta o asociación fue borrada", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Asociación no existe", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                break;
            default:
                TextView carnetText = findViewById(R.id.carnet);
                String carnet = carnetText.getText().toString();
                TextView passwordUserText = findViewById(R.id.password);
                String passwordUser = passwordUserText.getText().toString();
                if (carnet.isEmpty() || passwordUser.isEmpty()){
                    Toast.makeText(Login.this, "Llene todos los campos", Toast.LENGTH_LONG).show();
                    return;
                }
                myRef.child("users").child (carnet).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            DataSnapshot dataSnapshot = task.getResult();
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.child("password").getValue().toString().equals(passwordUser)){
                                    singleFirebase.setCurrentUserCarnet(carnet);
                                    singleFirebase.setCurrentUserType(currentUserType);
                                    continuar(view);
                                } else {
                                    Toast.makeText(Login.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Usuario no existe", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                break;
        }
    }
    public void continuar(View view){
        Intent siguiente = new Intent(this, MainScreen.class);
        siguiente.putExtra("userType", currentUserType);
        startActivity(siguiente);
    }
}