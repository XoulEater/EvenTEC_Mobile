package com.example.eventec.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.eventec.R;
import com.example.eventec.entities.SingleFirebase;
import com.example.eventec.entities.VPAdapter;
import com.example.eventec.fragments.EventsAlert;
import com.example.eventec.fragments.EventsCreator;
import com.example.eventec.fragments.EventsDisplay;
import com.google.android.material.tabs.TabLayout;

// Clase que se encarga de mostrar la pantalla principal de la aplicación
public class MainScreen extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView EvenTEC;
    private SingleFirebase singleFirebase; // Singleton
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Inicialización de variables
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        EvenTEC = findViewById(R.id.textView4);

        singleFirebase = SingleFirebase.getInstance(); // Instancia del singleton

        // Colores de la palabra EvenTEC
        String Even = getColoredSpanned("Even", "#FFFFFF");
        String TEC = getColoredSpanned("TEC","#0094FF");
        EvenTEC.setText(Html.fromHtml(Even+TEC));

        // Configuración del tabLayout y del viewPager
        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new EventsDisplay(), "Inicio");
        vpAdapter.addFragment(new EventsAlert(), "Avisos");
        vpAdapter.addFragment(new EventsCreator(), "Crear");
        viewPager.setAdapter(vpAdapter);

        // Configuración del toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Drawable customMenuIcon = ContextCompat.getDrawable(this, R.drawable.three_dots);
        mToolbar.setOverflowIcon(customMenuIcon);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    // Método que se encarga de inflar el menú
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("TEST", "MENU INFLATED");
        MenuInflater inflater = getMenuInflater();
        if (singleFirebase.getCurrentUserType() == 0){
            inflater.inflate(R.menu.menu_estudiante, menu);
        } else {
            inflater.inflate(R.menu.menu_asociacion, menu);
        }

        return true;
    }

    @Override
    // Método que se encarga de manejar los eventos del menú
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (singleFirebase.getCurrentUserType() == 0){
            // Si el usuario es un estudiante

            // Si se selecciona la opción de cerrar sesión
            if (item.getItemId() == R.id.logout_estudiante){
                Toast.makeText(this, "LOGOUT", Toast.LENGTH_SHORT).show();
                singleFirebase.logout(this);
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        } else {
            // Si el usuario es una asociación

            // Se se selecciona la opción de cerrar sesión
            if (item.getItemId() == R.id.logout_asociacion){
                Toast.makeText(this, "LOGOUT", Toast.LENGTH_SHORT).show();
                singleFirebase.logout(this);
                return true;
            } else if (item.getItemId() == R.id.perfil_asociacion) {
                // si se selecciona la opción de ver el perfil, se pasa a la pagína.
                Intent siguiente = new Intent(this, Perfil.class);
                startActivity(siguiente);
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    // Método que se encarga de colorear la palabra EvenTEC
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}