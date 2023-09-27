package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import com.example.eventec.R;


public class StartActivity extends AppCompatActivity {

    private int currentOption = 0;
    private CardView cardViewEstudiante;
    private CardView cardViewColaborador;
    private CardView cardViewAsociacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        cardViewEstudiante = findViewById(R.id.cardView1);
        cardViewColaborador = findViewById(R.id.cardView2);
        cardViewAsociacion = findViewById(R.id.cardView3);

        cardViewEstudiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOption = 0;
                updateCardViewSelection(cardViewEstudiante, true);
                updateCardViewSelection(cardViewColaborador, false);
                updateCardViewSelection(cardViewAsociacion, false);
            }
        });
        cardViewColaborador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOption = 1;
                updateCardViewSelection(cardViewEstudiante, false);
                updateCardViewSelection(cardViewColaborador, true);
                updateCardViewSelection(cardViewAsociacion, false);
            }
        });
        cardViewAsociacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOption = 2;
                updateCardViewSelection(cardViewEstudiante, false);
                updateCardViewSelection(cardViewColaborador, false);
                updateCardViewSelection(cardViewAsociacion, true);            }
        });
    }

    private void updateCardViewSelection(CardView cardView, boolean selected) {
        if (selected) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.darkGrey));
            // Realiza cualquier otra acción que desees cuando se selecciona el CardView
        } else {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.black));
            // Realiza cualquier otra acción que desees cuando se deselecciona el CardView
        }
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