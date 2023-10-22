package com.example.eventec.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.eventec.R;
import com.example.eventec.entities.VPAdapter;
import com.example.eventec.fragments.EventsAlert;
import com.example.eventec.fragments.EventsCreator;
import com.example.eventec.fragments.EventsDisplay;
import com.google.android.material.tabs.TabLayout;

public class MainScreen extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView EvenTEC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        EvenTEC = findViewById(R.id.textView4);

        String Even = getColoredSpanned("Even", "#FFFFFF");
        String TEC = getColoredSpanned("TEC","#0094FF");
        EvenTEC.setText(Html.fromHtml(Even+TEC));

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new EventsDisplay(), "Inicio");
        vpAdapter.addFragment(new EventsAlert(), "Avisos");
        vpAdapter.addFragment(new EventsCreator(), "Crear");
        viewPager.setAdapter(vpAdapter);

    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}