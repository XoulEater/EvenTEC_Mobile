package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private int currentOption = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void estudiante (View view){
        currentOption = 0;
    }
    public void colaborador (View view){
        currentOption = 1;
    }
    public void asocia (View view){
        currentOption = 2;
    }

    public void login (View view){
        Intent siguiente = new Intent(this, login.class);
        siguiente.putExtra("userType", currentOption);
        startActivity(siguiente);
    }
}