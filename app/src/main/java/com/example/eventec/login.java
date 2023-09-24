package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class login extends AppCompatActivity {
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
}