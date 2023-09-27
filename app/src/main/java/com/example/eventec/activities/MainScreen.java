package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.eventec.R;
import com.example.eventec.entities.VPAdapter;
import com.example.eventec.fragments.EventsAlert;
import com.example.eventec.fragments.EventsCreator;
import com.example.eventec.fragments.EventsDisplay;
import com.google.android.material.tabs.TabLayout;

public class MainScreen extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vpAdapter.addFragment(new EventsDisplay(), "Inicio");
        vpAdapter.addFragment(new EventsAlert(), "Avisos");
        vpAdapter.addFragment(new EventsCreator(), "Crear");
        viewPager.setAdapter(vpAdapter);

    }
}