package com.example.eventec.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.eventec.R;
import com.example.eventec.entities.ActivityModel;
import com.example.eventec.entities.Comment;
import com.example.eventec.entities.CommentAdapter;
import com.example.eventec.entities.EventModel;
import com.example.eventec.entities.QuestionAdapter;
import com.example.eventec.entities.QuestionModel;
import com.example.eventec.entities.SingleFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Informe extends AppCompatActivity {
    private SingleFirebase singleFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);

        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");

        singleFirebase = SingleFirebase.getInstance();

        DatabaseReference myRef = singleFirebase.getMyRef();

        // Se realizan un montón de lecturas de Firebase para tener toda la información para desplegar las estadísticas y resultados de evaluaciones.
        myRef.child("eventos").child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting evento", task.getException());
                }
                else {
                    // Primero se lee el evento
                    HashMap<String, ?> evento = (HashMap<String, ?>) task.getResult().getValue();
                    myRef.child("inscritos").child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting inscritos", task.getException());
                            }
                            else {
                                // Se lee la lista de inscritos al evento
                                HashMap<String, ?> inscritos = (HashMap<String, ?>) task.getResult().getValue();
                                myRef.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting users", task.getException());
                                        }
                                        else {
                                            // Se lee la lista de usuarios para obtener su información como el nombre, teléfono y carrera
                                            HashMap<String, HashMap<String, String>> users = (HashMap<String, HashMap<String, String>>) task.getResult().getValue();
                                            myRef.child("comments").child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.e("firebase", "Error getting comments", task.getException());
                                                    }
                                                    else {
                                                        // Se lee la lista de comentarios para desplegarlos en la actividad
                                                        HashMap<String, HashMap<String, String>> commentsMap = (HashMap<String, HashMap<String, String>>) task.getResult().getValue();
                                                        myRef.child("ratings").child(eventId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                if (!task.isSuccessful()) {
                                                                    Log.e("firebase", "Error getting ratings", task.getException());
                                                                }
                                                                else {
                                                                    // Se leen los ratings
                                                                    HashMap<String, HashMap<String, Long>> ratingsMap = (HashMap<String, HashMap<String, Long>>) task.getResult().getValue();

                                                                    List<Comment> commentsList = new ArrayList<>();
                                                                    // Por cada comentario, se crea un objeto comentario y se guardan en commentsList
                                                                    for (String key : commentsMap.keySet()) {
                                                                        HashMap<String, String> comment = commentsMap.get(key);
                                                                        Comment commentObject = new Comment(comment.get("comment"), comment.get("eventId"), comment.get("timestamp"), comment.get("userInfo"));
                                                                        commentsList.add(commentObject);
                                                                    }

                                                                    // Se despliegan los datos en la actividad
                                                                    TextView eventoTitle = findViewById(R.id.title);
                                                                    eventoTitle.setText(evento.get("titulo").toString());

                                                                    TextView descripcion = findViewById(R.id.description);
                                                                    descripcion.setText("Descripción: " + evento.get("descripcion").toString());

                                                                    TextView fecha = findViewById(R.id.fecha);
                                                                    fecha.setText("Fecha: " + evento.get("fechaInicio").toString() + " - " + evento.get("fechaFin").toString());

                                                                    TextView clicks = findViewById(R.id.clicks);
                                                                    clicks.setText("Cantidad de clicks en el evento: " + evento.get("clicks").toString());

                                                                    // Con los cupos y capacidad, se calcula el porcentaje de asistencia
                                                                    long cupos = (Long) evento.get("cupos");
                                                                    long capacidad = (Long) evento.get("capacidad");

                                                                    TextView cuposText = findViewById(R.id.cupos);
                                                                    cuposText.setText("Cupos: " + String.valueOf(cupos) + "/" + String.valueOf(capacidad));
                                                                    // Formato para redondear los decimales a solo 2 decimales.
                                                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");

                                                                    float asistenciaFloat = ((float) cupos)/((float) capacidad)*100f;
                                                                    TextView asistencia = findViewById(R.id.asistencia);
                                                                    asistencia.setText("Asistencia: " + decimalFormat.format(asistenciaFloat) + "%");

                                                                    int cancelaciones = 0;

                                                                    TableLayout inscritosTable = findViewById(R.id.inscritosTable);
                                                                    HashMap<String, Integer> carrerasCount = new HashMap<>();
                                                                    // HashMap que va a guardar los códigos de carrera y la cantidad de esa carreras

                                                                    int leftPadding = 15;
                                                                    // Se recorre la lista de inscritos y se agregan a la tabla de inscritos
                                                                    for (String inscrito : inscritos.keySet()) {
                                                                        if (inscritos.get(inscrito).toString() != "false") {
                                                                            HashMap<String, String> user = users.get(inscrito);
                                                                            TableRow tableRow = new TableRow(Informe.this);
                                                                            // Fila de la tabla

                                                                            // Crea los TextViews para la fila de la tabla.
                                                                            TextView nombre = new TextView(Informe.this);
                                                                            nombre.setText(user.get("name").toString());
                                                                            tableRow.addView(nombre);
                                                                            nombre.setPadding(leftPadding, 0, 0, 0);
                                                                            nombre.setBackgroundResource(R.drawable.table_border);

                                                                            TextView telefono = new TextView(Informe.this);
                                                                            telefono.setText(user.get("phone").toString());
                                                                            tableRow.addView(telefono);
                                                                            telefono.setPadding(leftPadding, 0, 0, 0);
                                                                            telefono.setBackgroundResource(R.drawable.table_border);

                                                                            String carrera = user.get("carrera").toString();
                                                                            carrerasCount.merge(carrera, 1, Integer::sum); // inserta la carrera o la incrementa si ya existe.


                                                                            TextView carreraText = new TextView(Informe.this);
                                                                            carreraText.setText(carrera);
                                                                            tableRow.addView(carreraText);
                                                                            carreraText.setPadding(leftPadding, 0, 0, 0);
                                                                            carreraText.setBackgroundResource(R.drawable.table_border);

                                                                            TextView registro = new TextView(Informe.this);
                                                                            registro.setText(inscritos.get(inscrito).toString());
                                                                            tableRow.addView(registro);
                                                                            registro.setPadding(leftPadding, 0, 0, 0);
                                                                            registro.setBackgroundResource(R.drawable.table_border);

                                                                            inscritosTable.addView(tableRow);
                                                                        } else {
                                                                            // Si hay alguno en false, se cuenta en las cancelaciones.
                                                                            cancelaciones++;
                                                                        }
                                                                    }

                                                                    // Se recorrer el HashMap de carreras y se agregan a la tabla de carreras
                                                                    TableLayout carrerasTable = findViewById(R.id.carrerasTable);
                                                                    for (String carrera : carrerasCount.keySet()) {
                                                                        TableRow tableRow = new TableRow(Informe.this); // Fila de la tabla

                                                                        // Se crean los textviews y se agregan a la fila de la tabla

                                                                        TextView carreraText = new TextView(Informe.this);
                                                                        carreraText.setText(carrera);
                                                                        tableRow.addView(carreraText);
                                                                        carreraText.setPadding(leftPadding, 0, 0, 0);
                                                                        carreraText.setBackgroundResource(R.drawable.table_border);

                                                                        TextView countText = new TextView(Informe.this);
                                                                        countText.setText(String.valueOf(carrerasCount.get(carrera)));
                                                                        tableRow.addView(countText);
                                                                        countText.setPadding(leftPadding, 0, 0, 0);
                                                                        countText.setBackgroundResource(R.drawable.table_border);

                                                                        // Se agrega la fila a la tabla.
                                                                        carrerasTable.addView(tableRow);
                                                                    }
                                                                    TextView cancelacionesText = findViewById(R.id.cancelaciones);
                                                                    cancelacionesText.setText("Cantidad de cancelaciones: " + String.valueOf(cancelaciones));

                                                                    TextView mainRatingLabel = findViewById(R.id.mainRatingLabel);
                                                                    // Se calcula el rating del evento en general
                                                                    long sumaEvento = ((HashMap<String, Long>) ratingsMap.get("Evento")).get("suma");
                                                                    long cantidadEvento = ((HashMap<String, Long>) ratingsMap.get("Evento")).get("cantidad");

                                                                    float ratingEvento = ((float) sumaEvento)/ ((float) cantidadEvento);
                                                                    mainRatingLabel.setText("Calificación del evento: " + decimalFormat.format(ratingEvento));

                                                                    TextView cantidadComentarios = findViewById(R.id.cantidadComentarios);
                                                                    cantidadComentarios.setText("Cantidad de comentarios: " + String.valueOf(commentsList.size()));

                                                                    TableLayout ratingsTable = findViewById(R.id.ratingsTable);

                                                                    // Se recorre el HashMap de ratings y se agregan a la tabla de ratings
                                                                    for (String actividadName : ratingsMap.keySet()) {
                                                                        TableRow tableRow = new TableRow(Informe.this);

                                                                        HashMap<String, Long> activdadInfo = ratingsMap.get(actividadName);
                                                                        // Se calcula el rating a partir de la suma acumulada y la cantidad de evaluaciones.
                                                                        long suma = activdadInfo.get("suma");
                                                                        long cantidad = activdadInfo.get("cantidad");
                                                                        float ratingActividad = ((float) suma)/((float) cantidad);

                                                                        // Se crean los textViews y se actualizan
                                                                        TextView actividadNameText = new TextView(Informe.this);
                                                                        actividadNameText.setText(actividadName);
                                                                        actividadNameText.setPadding(leftPadding, 0, 0, 0);
                                                                        actividadNameText.setTypeface(null, Typeface.BOLD);
                                                                        tableRow.addView(actividadNameText);

                                                                        TextView ratingActividadText = new TextView(Informe.this);
                                                                        ratingActividadText.setText(decimalFormat.format(ratingActividad) + " con " + String.valueOf(cantidad) + " votos");
                                                                        ratingActividadText.setPadding(leftPadding, 0, 0, 0);
                                                                        tableRow.addView(ratingActividadText);

                                                                        ratingsTable.addView(tableRow);
                                                                    }

                                                                    // Se crea el recycler view que va a desplegar la lista de comentarios.
                                                                    RecyclerView commentsRecyclerView = findViewById(R.id.commentsRV);
                                                                    CommentAdapter commentAdapter = new CommentAdapter(Informe.this, commentsList);
                                                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Informe.this, LinearLayoutManager.VERTICAL, false);

                                                                    commentsRecyclerView.setLayoutManager(linearLayoutManager);
                                                                    commentsRecyclerView.setAdapter(commentAdapter);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
}