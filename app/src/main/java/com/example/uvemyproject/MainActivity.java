package com.example.uvemyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.uvemyproject.api.services.ListaCursosServices;
import com.example.uvemyproject.databinding.ActivityMainBinding;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.checkerframework.checker.units.qual.C;

public class MainActivity extends AppCompatActivity {

    final int CURSOS_ID = R.id.cursos;
    final int BUSQUEDA_ID = R.id.busqueda;
    final int PERFIL_ID = R.id.perfil;

    ActivityMainBinding binding;

    BottomNavigationView bottomNavigationView;

    FormularioUsuario formularioUsuario = new FormularioUsuario(true);
    CursoDetallesPrincipal cursoDetallesPrincipal = new CursoDetallesPrincipal();
    ListadoCursos listadoCursos = new ListadoCursos();
    FormularioClase formularioCurso = new FormularioClase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.container, formularioUsuario).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == CURSOS_ID) {
                    Bundle bundle = new Bundle();
                    listadoCursos = new ListadoCursos();
                    int pagina = 0;
                    bundle.putInt("clave_pagina", pagina);
                    listadoCursos.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.container, listadoCursos).commit();
                    return true;
                } else if (itemId == BUSQUEDA_ID) {

                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.container, cursoDetallesPrincipal).commit();
                    return true;
                } else if (itemId == PERFIL_ID) {
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.container, formularioUsuario).commit();
                    return true;
                }

                return false;
            }
        });
    }

    public void cambiarFragmentoPrincipal(Fragment fragmentoMostrar){
        getSupportFragmentManager().beginTransaction().replace(
                R.id.container, fragmentoMostrar).addToBackStack(null).commit();
    }
}