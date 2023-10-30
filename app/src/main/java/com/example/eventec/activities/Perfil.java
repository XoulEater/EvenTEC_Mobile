package com.example.eventec.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventec.R;
import com.example.eventec.entities.Asociacion;
import com.example.eventec.entities.SingleFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Perfil extends AppCompatActivity {

    private SingleFirebase singleFirebase;

    private boolean editing;
    private String asoName;
    private String asoUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        singleFirebase = SingleFirebase.getInstance();

        asoUser = singleFirebase.getCurrentAsoUser();
        asoName = singleFirebase.getCurrentAsoName();
        DatabaseReference myRef = singleFirebase.getMyRef();

        editing = false;
        myRef.child("asociaciones").child(asoUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    HashMap<String, ?> asociacion = (HashMap<String, ?>) task.getResult().getValue();
                    TextView userAsociacion = findViewById(R.id.userAso);
                    userAsociacion.setText(asoUser);
                    userAsociacion.setEnabled(false);
                    EditText nombreAsociacion = findViewById(R.id.aso);
                    nombreAsociacion.setText(asociacion.get("nombreAso").toString());
                    nombreAsociacion.setEnabled(false);
                    EditText passwordAsociacion = findViewById(R.id.password);
                    passwordAsociacion.setText(asociacion.get("password").toString());
                    passwordAsociacion.setEnabled(false);
                    EditText descripcionAsociacion = findViewById(R.id.descripcion);
                    descripcionAsociacion.setText(asociacion.get("descripcion").toString());
                    descripcionAsociacion.setEnabled(false);
                    EditText telefonoAsociacion = findViewById(R.id.phone);
                    telefonoAsociacion.setText(asociacion.get("phone").toString());
                    telefonoAsociacion.setEnabled(false);
                    EditText correoAsociacion = findViewById(R.id.email);
                    correoAsociacion.setText(asociacion.get("email").toString());
                    correoAsociacion.setEnabled(false);
                    EditText carreraAsociacion = findViewById(R.id.carrera);
                    carreraAsociacion.setText(asociacion.get("carrera").toString());
                    carreraAsociacion.setEnabled(false);
                    List<String> miembrosList = (List<String>) asociacion.get("miembros");
                    String miembros = miembrosList.get(0);
                    for (int i = 1; i < miembrosList.size(); i++) {
                        miembros += "," + miembrosList.get(i);
                    }
                    EditText miembrosAsociacion = findViewById(R.id.miembros);
                    miembrosAsociacion.setText(miembros);
                    miembrosAsociacion.setEnabled(false);
                }
            }
        });
    }

    private boolean isValidEmail(String email){
        // Expresión regular para validar email.
        String regex;
        regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private void updateAsociacion(String userAso, String nombreAso, String carrera, String password, String email, String phone, String descripcion, List<String> miembros) {
        DatabaseReference myRef = singleFirebase.getMyRef();
        Asociacion asociacion = new Asociacion(userAso, nombreAso, carrera, password, email, phone, descripcion, miembros);
        Log.d("TEST", nombreAso);
        myRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    boolean notExists = false;
                    Set<String> carnetsUsers = ((HashMap<String, HashMap<?, ?>>)task.getResult().getValue()).keySet();
                    for(String miembro : miembros){
                        if(!carnetsUsers.contains(miembro)){
                            notExists = true;
                            break;
                        }
                    }
                    if (notExists){
                        Toast.makeText(Perfil.this, "Alguno de los miembros no existe", Toast.LENGTH_LONG).show();
                    } else {
                        myRef.child("asociaciones").child(userAso).setValue(asociacion);
                        Log.d("TEST", singleFirebase.getCurrentAsoName());
                        // Si se cambió el nombre de la asociación, se deben modificar los eventos creados por la asociación.
                        if (!asoName.equals(nombreAso)){
                            myRef.child("eventos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        HashMap<String, HashMap<?, ?>> eventos = (HashMap<String, HashMap<?, ?>>) task.getResult().getValue();
                                        for (String evento : eventos.keySet()){
                                            HashMap<String, ?> eventoMap = (HashMap<String, ?>) eventos.get(evento);
                                            Log.d("TEST", eventoMap.get("nombreAsociacion").toString());
                                            if (eventoMap.get("userAsociacion").equals(asoUser)){
                                                myRef.child("eventos").child(evento).child("nombreAsociacion").setValue(nombreAso);
                                            }
                                        }
                                        singleFirebase.setCurrentAsoName(nombreAso);
                                        asoName = nombreAso;
                                    }
                                }
                            });

                        }
                        TextView userAsociacion = findViewById(R.id.userAso);
                        userAsociacion.setEnabled(false);
                        EditText nombreAsociacion = findViewById(R.id.aso);
                        nombreAsociacion.setEnabled(false);
                        EditText passwordAsociacion = findViewById(R.id.password);
                        passwordAsociacion.setEnabled(false);
                        EditText descripcionAsociacion = findViewById(R.id.descripcion);
                        descripcionAsociacion.setEnabled(false);
                        EditText telefonoAsociacion = findViewById(R.id.phone);
                        telefonoAsociacion.setEnabled(false);
                        EditText correoAsociacion = findViewById(R.id.email);
                        correoAsociacion.setEnabled(false);
                        EditText carreraAsociacion = findViewById(R.id.carrera);
                        carreraAsociacion.setEnabled(false);
                        EditText miembrosAsociacion = findViewById(R.id.miembros);
                        miembrosAsociacion.setEnabled(false);
                        editing = false;
                        TextView editarBtn = findViewById(R.id.editar);
                        editarBtn.setText("Editar");

                        Toast.makeText(Perfil.this, "Asociación actualizada", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void editar(View view){
        if (editing) {
            TextView nombreAsoText = findViewById(R.id.aso);
            String nombreAso = nombreAsoText.getText().toString();
            TextView carreraText = findViewById(R.id.carrera);
            String carrera = carreraText.getText().toString();
            TextView userAsoText = findViewById(R.id.userAso);
            String userAso = userAsoText.getText().toString();
            TextView passwordText = findViewById(R.id.password);
            String password = passwordText.getText().toString();
            TextView emailText = findViewById(R.id.email);
            String email = emailText.getText().toString();
            TextView phoneText = findViewById(R.id.phone);
            String phone = phoneText.getText().toString();
            TextView descripcionText = findViewById(R.id.descripcion);
            String descripcion = descripcionText.getText().toString();
            TextView miembrosText = findViewById(R.id.miembros);
            String miembros = miembrosText.getText().toString();
            // Toast.makeText(this, String.format("%s %s %s %s %s %s %s", nombreAso, carrera, password, email, phone, descripcion, miembros), Toast.LENGTH_LONG).show();
            String[] miembrosArray = miembros.split(",");
            List<String> miembrosList = Arrays.asList(miembrosArray);
            if (!isValidEmail(email)){
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_LONG).show();
                return;
            }
            if (nombreAso.isEmpty() || carrera.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || descripcion.isEmpty() || miembros.isEmpty()){
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                return;
            } else {
                updateAsociacion(userAso, nombreAso, carrera, password, email, phone, descripcion, miembrosList);
            }
        } else {
            TextView userAsociacion = findViewById(R.id.userAso);
            userAsociacion.setEnabled(false);
            EditText nombreAsociacion = findViewById(R.id.aso);
            nombreAsociacion.setEnabled(true);
            EditText passwordAsociacion = findViewById(R.id.password);
            passwordAsociacion.setEnabled(true);
            EditText descripcionAsociacion = findViewById(R.id.descripcion);
            descripcionAsociacion.setEnabled(true);
            EditText telefonoAsociacion = findViewById(R.id.phone);
            telefonoAsociacion.setEnabled(true);
            EditText correoAsociacion = findViewById(R.id.email);
            correoAsociacion.setEnabled(true);
            EditText carreraAsociacion = findViewById(R.id.carrera);
            carreraAsociacion.setEnabled(true);
            EditText miembrosAsociacion = findViewById(R.id.miembros);
            miembrosAsociacion.setEnabled(true);
            editing = true;
            TextView editarBtn = findViewById(R.id.editar);
            editarBtn.setText("Guardar");
        }

    }
}