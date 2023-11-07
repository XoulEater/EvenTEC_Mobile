package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eventec.R;
import com.example.eventec.entities.SingleFirebase;

// Pantalla de inicio de la aplicacion
public class StartActivity extends AppCompatActivity {

    private int currentOption = 0; // 0 = Estudiante, 2 = Asociacion
    private CardView cardViewEstudiante;
    private CardView cardViewAsociacion;
    private LinearLayout layoutEstudiante;
    private LinearLayout layoutAsociacion;
    private ImageView ivEstudiante;
    private ImageView ivAsociacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Obtener los elementos de la interfaz
        cardViewEstudiante = findViewById(R.id.cardView1);
        cardViewAsociacion = findViewById(R.id.cardView3);
        layoutEstudiante = findViewById(R.id.linearLayout1);
        layoutAsociacion = findViewById(R.id.linearLayout3);
        ivEstudiante = findViewById(R.id.imageView1);
        ivAsociacion = findViewById(R.id.imageView3);

        Log.d("TEST", "" + R.drawable.events);
//        SingleFirebase singleFirebase = SingleFirebase.getInstance();
//        singleFirebase.refreshEventList();

        // Cambiar el color de la cardview y el icono al seleccionar una opcion
        cardViewEstudiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOption = 0;
                layoutEstudiante.setBackgroundResource(R.drawable.card_background);
                ivEstudiante.setImageResource(R.drawable.studentlogoaccent);
                layoutAsociacion.setBackgroundResource(R.color.black);
                ivAsociacion.setImageResource(R.drawable.asologo);
            }
        });
        cardViewAsociacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOption = 2;
                layoutAsociacion.setBackgroundResource(R.drawable.card_background);
                ivAsociacion.setImageResource(R.drawable.asologoaccent);
                layoutEstudiante.setBackgroundResource(R.color.black);
                ivEstudiante.setImageResource(R.drawable.studentlogo);
            }
        });
    }


    // Ir a la pantalla de login o registro dependiendo de la opcion seleccionada
    public void login (View view){
        Intent siguiente = new Intent(this, Login.class); // Cambiar por la pantalla de login
        siguiente.putExtra("userType", currentOption); // Enviar el tipo de usuario seleccionado
        startActivity(siguiente); // Iniciar la siguiente actividad
    }
    public void registro (View view){
        Intent siguiente = new Intent(this, Registro.class);
        siguiente.putExtra("userType", currentOption);
        startActivity(siguiente);
    }
}