package com.example.eventec.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

// Clase que se encarga de manejar los fragmentos de la aplicación
public class VPAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>(); // Lista de fragmentos
    private final ArrayList<String> fragmentTitle = new ArrayList<>(); // Lista de títulos de los fragmentos

    public VPAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    // Método que retorna el fragmento de la posición indicada
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    // Método que retorna la cantidad de fragmentos
    public int getCount() {
        return fragmentArrayList.size();
    }

    // Método que agrega un fragmento a la lista de fragmentos
    public void addFragment(Fragment pFragment, String pTitle) {
        fragmentArrayList.add(pFragment);
        fragmentTitle.add(pTitle);
    }

    @Nullable
    @Override
    // Método que retorna el título del fragmento de la posición indicada
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }

}
