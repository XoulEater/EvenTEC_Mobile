package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eventec.R;

public class Registro extends AppCompatActivity {
    private int currentUserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserType = getIntent().getIntExtra("userType", 0);

        switch (currentUserType) {
            case 1:
                setContentView(R.layout.activity_registro_c);
                break;
            case 2:
                setContentView(R.layout.activity_registro_a);
                break;
            default:
                setContentView(R.layout.activity_registro_e);
        }
    }
}
