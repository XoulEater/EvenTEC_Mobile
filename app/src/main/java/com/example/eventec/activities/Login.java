package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.eventec.R;

public class Login extends AppCompatActivity {
    private int currentUserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserType = getIntent().getIntExtra("userType", 0);

        switch (currentUserType){
            case 1:
                setContentView(R.layout.activity_login_c);
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

    public void continuar(View view){
        Intent siguiente = new Intent(this, MainScreen.class);
        siguiente.putExtra("userType", currentUserType);
        startActivity(siguiente);
    }
}