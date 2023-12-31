package com.example.eventec.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.eventec.R;
import com.example.eventec.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.Toast;
import com.example.eventec.entities.Asociacion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Pantalla de registro de usuario
public class Registro extends AppCompatActivity {
    private int currentUserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserType = getIntent().getIntExtra("userType", 0); // 1 = N/A, 2 = Asociacion, 3 = Estudiante

        // Cambiar el layout dependiendo del tipo de usuario
        switch (currentUserType) {
            case 1:
                break;
            case 2:
                setContentView(R.layout.activity_registro_a);
                break;
            default:
                setContentView(R.layout.activity_registro_e);
        }
    }

    // Validar que el correo tenga el formato correcto
    private boolean isValidEmail(String email){
        // Expresión regular para validar email.
        String regex;
        if (currentUserType == 2){
            regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        } else {
            regex = "^[A-Za-z0-9+_.-]+@estudiantec\\.cr$";
        }


        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    // Registrar una asociacion
    private void registerAsociacion(String userAso, String nombreAso, String carrera, String password, String email, String phone, String descripcion, List<String> miembros) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // Instancia de la base de datos
        DatabaseReference myRef = database.getReference(); // Referencia a la base de datos
        Asociacion asociacion = new Asociacion(userAso, nombreAso, carrera, password, email, phone, descripcion, miembros);

        // Verificar que no exista una asociacion con el mismo nombre

        // Se revisa si la asociación ya existe.
        myRef.child("asociaciones").child(userAso).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    if (task.getResult().getValue() != null && ((HashMap<String, ?>) task.getResult().getValue()).get("enabled").equals(true)){
                        // Si la asociación con ese usuario ya existe, se manda el mensaje de error.
                        Toast.makeText(Registro.this, "Ya existe una asociación con ese nombre", Toast.LENGTH_LONG).show();
                    } else {
                        // Se leen los usuarios para verificar que los miembros estén registrados.
                        myRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else {
                                    // Se revisa que los miembros estén registrados.
                                    boolean notExists = false;
                                    Set<String> carnetsUsers = ((HashMap<String, HashMap<?, ?>>)task.getResult().getValue()).keySet();
                                    for(String miembro : miembros){
                                        if(!carnetsUsers.contains(miembro)){
                                            notExists = true;
                                            break;
                                        }
                                    }
                                    if (notExists){
                                        // Si algún miembro no existe, se manda el mensaje de error.
                                        Toast.makeText(Registro.this, "Alguno de los miembros no existe", Toast.LENGTH_LONG).show();
                                    } else {
                                        // Se guarda la actualización y se envía a la página de Login.
                                        myRef.child("asociaciones").child(userAso).setValue(asociacion);
                                        Toast.makeText(Registro.this, "Asociación registrada", Toast.LENGTH_LONG).show();
                                        Intent siguiente = new Intent(Registro.this, Login.class);
                                        siguiente.putExtra("userType", currentUserType);
                                        startActivity(siguiente);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    // Registrar un usuario
    private void registerUser(String name, String carnet, String password, String email, String phone, String sede, String carrera){
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // Instancia de la base de datos
        DatabaseReference myRef = database.getReference(); //  Referencia a la base de datos
        User user = new User(name, carnet, password, email, phone, sede, carrera);

        // Verificar que no exista un usuario con el mismo carnet
        myRef.child("users").child(carnet).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    // Se revisa que el usuario no exista.
                    if (task.getResult().getValue() != null){
                        Toast.makeText(Registro.this, "Ya existe un usuario con ese carnet", Toast.LENGTH_LONG).show();
                    } else {
                        // Se guarda el usuario en la base de datos y se redirige a la página de login.
                        myRef.child("users").child(carnet).setValue(user);
                        Toast.makeText(Registro.this, "Usuario registrado", Toast.LENGTH_LONG).show();
                        Intent siguiente = new Intent(Registro.this, Login.class);
                        siguiente.putExtra("userType", currentUserType);
                        startActivity(siguiente);
                    }
                }
            }
        });
    }

    // Registrar un usuario o asociacion dependiendo del tipo de usuario
    public void registrarse(View view){
        // Obtener los datos de la interfaz
        switch (currentUserType) {
            case 1:
                break;
            case 2: // Asociacion
                // Se leen todos los valores de los campos de texto
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

                String[] miembrosArray = miembros.split(",");
                List<String> miembrosList = Arrays.asList(miembrosArray);
                if (!isValidEmail(email)){
                    // Se valida el correo
                    Toast.makeText(this, "Correo inválido", Toast.LENGTH_LONG).show();
                    return;
                }
                // No puede haber algún campo vacío
                if (nombreAso.isEmpty() || carrera.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || descripcion.isEmpty() || miembros.isEmpty()){
                    Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    registerAsociacion(userAso, nombreAso, carrera, password, email, phone, descripcion, miembrosList);
                }
                break;
            default: //Estudiante
                // Se leen todos los campos de texto.
                TextView userText = findViewById(R.id.user);
                String user = userText.getText().toString();
                TextView carnetText = findViewById(R.id.carne);
                String carnet = carnetText.getText().toString();
                TextView passwordUserText = findViewById(R.id.password);
                String passwordUser = passwordUserText.getText().toString();
                TextView emailUserText = findViewById(R.id.correo);
                String emailUser = emailUserText.getText().toString();
                TextView phoneUserText = findViewById(R.id.Telefono);
                String phoneUser = phoneUserText.getText().toString();
                TextView sedeText = findViewById(R.id.sede);
                String sede = sedeText.getText().toString();
                TextView carreraUserText = findViewById(R.id.carrera);
                String carreraUser = carreraUserText.getText().toString();

                // Se valida que el correo sea válido y termine con estudiantec.cr
                if (!isValidEmail(emailUser)){
                    Toast.makeText(this, "Correo inválido. Debe terminar con @estudiantec.cr", Toast.LENGTH_LONG).show();
                    return;
                }
                // No puede haber campos vacíos.
                if (user.isEmpty() || carnet.isEmpty() || passwordUser.isEmpty() || emailUser.isEmpty() || phoneUser.isEmpty() || sede.isEmpty() || carreraUser.isEmpty()){
                    Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    registerUser(user, carnet, passwordUser, emailUser, phoneUser, sede, carreraUser);
                }
                break;
        }
    }
}
