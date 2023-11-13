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
import com.example.eventec.email.SendMail;
import com.example.eventec.entities.AlertModel;
import com.example.eventec.entities.Asociacion;
import com.example.eventec.entities.SingleFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Clase para la actividad de perfil de asociación.
public class Perfil extends AppCompatActivity {

    private SingleFirebase singleFirebase; // Instancia de la clase SingleFirebase.
    private boolean editing; // Variable para saber si se está editando o no.
    private String asoName;
    private String asoUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil); // Se establece el layout de la actividad.

        singleFirebase = SingleFirebase.getInstance(); // Se obtiene la instancia de la clase SingleFirebase.

        // Se obtienen los datos de la asociación actual.
        asoUser = singleFirebase.getCurrentAsoUser();
        asoName = singleFirebase.getCurrentAsoName();
        DatabaseReference myRef = singleFirebase.getMyRef();

        editing = false;
        // Se obtienen los datos de la asociación actual.
        myRef.child("asociaciones").child(asoUser).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    // Se inicializan todos los valores del prefil con la información registrada en la base de datos
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
                    // Se construye el string de miembros
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

    // Método para validar el email.
    private boolean isValidEmail(String email){
        // Expresión regular para validar email.
        String regex;
        regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Objeto patrón para la expresión regular
        Pattern pattern = Pattern.compile(regex);

        // Objeto matcher que busca el patrón en el string.
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    // Método para actualizar los datos de la asociación.
    private void updateAsociacion(String userAso, String nombreAso, String carrera, String password, String email, String phone, String descripcion, List<String> miembros) {
        DatabaseReference myRef = singleFirebase.getMyRef();
        Asociacion asociacion = new Asociacion(userAso, nombreAso, carrera, password, email, phone, descripcion, miembros);

        // Se leen los usuarios para validar los carnets dados en los miembros.
        myRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    // Se valida que existan todos los carnets dados en la lista de miembros.
                    boolean notExists = false;
                    Set<String> carnetsUsers = ((HashMap<String, HashMap<?, ?>>)task.getResult().getValue()).keySet();
                    for(String miembro : miembros){
                        if(!carnetsUsers.contains(miembro)){
                            notExists = true;
                            break;
                        }
                    }
                    if (notExists){
                        // Si alguno no existe, se envía el mensaje de error
                        Toast.makeText(Perfil.this, "Alguno de los miembros no existe", Toast.LENGTH_LONG).show();
                    } else {
                        // Se actualiza la asocicación en la base de datos.
                        myRef.child("asociaciones").child(userAso).setValue(asociacion);

                        // Si se cambió el nombre de la asociación, se deben modificar los eventos creados por la asociación.
                        if (!asoName.equals(nombreAso)){
                            // se leen los eventos
                            myRef.child("eventos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        HashMap<String, HashMap<?, ?>> eventos = (HashMap<String, HashMap<?, ?>>) task.getResult().getValue();
                                        // Se recorren los eventos
                                        for (String evento : eventos.keySet()){
                                            // Si alguno tiene el userAsociacion igual al user de la asociación actual, se cambia el nombre de la asociación.
                                            HashMap<String, ?> eventoMap = (HashMap<String, ?>) eventos.get(evento);
                                            if (eventoMap.get("userAsociacion").equals(asoUser)){
                                                myRef.child("eventos").child(evento).child("nombreAsociacion").setValue(nombreAso);
                                            }
                                        }
                                        // Se actualiza el nombre de la asociación en la clase de SingleFirebase.
                                        singleFirebase.setCurrentAsoName(nombreAso);
                                        asoName = nombreAso; // Se actualiza el nombre de las asocicación local de la clase.
                                    }
                                }
                            });

                        }
                        // Se leen el resto de valores de la asociación
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

                        // Se deshabilita la edición y se cambia el texto del botón.
                        editing = false;
                        TextView editarBtn = findViewById(R.id.editar);
                        editarBtn.setText("Editar");

                        Toast.makeText(Perfil.this, "Asociación actualizada", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    // Método para ir activar la edición de la asocicación
    public void editar(View view){
        if (editing) {
            // Si está editando, lee todos los campos y llama al método para actualizar la asociación.
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
            // Si no está activada la edición, se habilitan todos los campos y se cambia el texto del botón.
            // Ya después el usuario puede modificar los campos
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

    // Método para eliminar la asociación.
    public void eliminar(View view){
        DatabaseReference myRef = singleFirebase.getMyRef();

        // Hace un borrado lógico de la asociación. Se pone el enabled como false.
        myRef.child("asociaciones").child(asoUser).child("enabled").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myRef.child("eventos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else {
                                    // Cuando se borra una asociación se deben cancelar todos los eventos que esa asociación había creado.
                                    HashMap<String, HashMap<?, ?>> eventos = (HashMap<String, HashMap<?, ?>>) task.getResult().getValue();
                                    myRef.child("inscritos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                Log.e("firebase", "Error getting data", task.getException());
                                            }
                                            else {
                                                // Se leen los inscritos a los eventos.
                                                HashMap<String, HashMap<?, ?>> inscritos = (HashMap<String, HashMap<?, ?>>) task.getResult().getValue();

                                                // Se recorren los eventos
                                                for (String evento : eventos.keySet()){
                                                    HashMap<String, ?> eventoMap = (HashMap<String, ?>) eventos.get(evento);
                                                    // Si el creador del evento es la asociación que se está borrando,
                                                    if (eventoMap.get("userAsociacion").equals(asoUser)){
                                                        // Se cambia el título a que diga CANCELADO al inicio
                                                        String eventId = eventoMap.get("eventId").toString();
                                                        String newEventTitle = "CANCELADO " + eventoMap.get("titulo").toString();
                                                        HashMap<String, Object> updates = new HashMap<>();
                                                        // Se pone el enabled como falso
                                                        updates.put("eventos/" + eventId +"/enabled", false);
                                                        // Se pone el nombre de la asociación como Asociación eliminada.
                                                        updates.put("eventos/" + eventId +"/nombreAsociacion", "Asociación eliminada");
                                                        // Se cambia el título
                                                        updates.put("eventos/" + eventId +"/titulo", newEventTitle);
                                                        // Se llena la capacidad del evento para que nadie pueda inscribirse.
                                                        updates.put("eventos/" + eventId +"/cupos", eventoMap.get("capacidad"));
                                                        HashMap<String, ?> inscritosEvento = (HashMap<String, ?>) inscritos.get(eventId);



                                                        // Se desuscriben todos los inscritos al evento
                                                        for (String inscrito : inscritosEvento.keySet()){
                                                            if (inscritosEvento.get(inscrito).equals(true)){
                                                                updates.put("userEventos/" + inscrito + "/" + eventId, false);
                                                                updates.put("inscritos/" + eventId + "/" + inscrito, false);

                                                            }
                                                        }
                                                        myRef.updateChildren(updates); // Se guardan todas las actualizaciones

                                                        // Se define el correo de la cancelación
                                                        String subject = "Evento cancelado";
                                                        String message = "El evento " + eventoMap.get("titulo").toString() + " ha sido cancelado por la asociación " + eventoMap.get("nombreAsociacion").toString() + ".\n\n" +
                                                                "Disculpe las molestias.";

                                                        // Se envía el correo a todos los users
                                                        myRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                if (!task.isSuccessful()) {
                                                                    Log.e("firebase", "Error getting data", task.getException());
                                                                }
                                                                else {
                                                                    // Se recorren los users
                                                                    HashMap<String, HashMap<?, ?>> users = (HashMap<String, HashMap<?, ?>>) task.getResult().getValue();
                                                                    for (String user : users.keySet()){
                                                                        HashMap<String, ?> userMap = (HashMap<String, ?>) users.get(user); // Se obtiene el user
                                                                        // Se envía el correo
                                                                        SendMail sendMail = new SendMail(userMap.get("email").toString(), subject, message);
                                                                        sendMail.execute(false);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                        String postdate = null;
                                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                            postdate = ZonedDateTime.now(ZoneId.of("America/Costa_Rica"))
                                                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyy, hh:mm:ss a"));
                                                        }
                                                        AlertModel alertModel = new AlertModel(subject, message, postdate,   1);
                                                        // subir alerta a la base de datos
                                                        String key = myRef.child("alertas").push().getKey();
                                                        myRef.child("alertas").child(key).setValue(alertModel);

                                                    }
                                                }
                                                Toast.makeText(Perfil.this, "Asociación eliminada", Toast.LENGTH_LONG).show();
                                                singleFirebase.logout(Perfil.this);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        Toast.makeText(Perfil.this, "Error al eliminar la asociación", Toast.LENGTH_LONG).show();
                    }
                });;

    }
}