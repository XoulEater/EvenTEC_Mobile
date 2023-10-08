package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eventec.R;


public class StartActivity extends AppCompatActivity {

    private int currentOption = 0;
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

        cardViewEstudiante = findViewById(R.id.cardView1);
        cardViewAsociacion = findViewById(R.id.cardView3);

        layoutEstudiante = findViewById(R.id.linearLayout1);
        layoutAsociacion = findViewById(R.id.linearLayout3);

        ivEstudiante = findViewById(R.id.imageView1);
        ivAsociacion = findViewById(R.id.imageView3);


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



    public void login (View view){
        Intent siguiente = new Intent(this, Login.class);
        siguiente.putExtra("userType", currentOption);
        startActivity(siguiente);
    }

    public void registro (View view){
        Intent siguiente = new Intent(this, Registro.class);
        siguiente.putExtra("userType", currentOption);
        startActivity(siguiente);
    }
}