package com.example.uvemyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminMainActivity extends AppCompatActivity {

    final int ETIQUETAS_ID = R.id.etiquetas;
    final int USUARIOS_ID = R.id.usuarios;
    final int BUSQUEDA_ID = R.id.busqueda;

    BottomNavigationView bottomNavigationView;

    ConsultaEtiquetas consultaEtiquetas = new ConsultaEtiquetas();
    ConsultaUsuarios consultaUsuarios = new ConsultaUsuarios();
    BusquedaUsuarios busquedaUsuarios = new BusquedaUsuarios();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == ETIQUETAS_ID){
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.container, consultaEtiquetas).commit();
                    return true;
                }

                if(itemId == USUARIOS_ID){
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.container, consultaUsuarios).commit();
                    return true;
                }

                if(itemId == BUSQUEDA_ID){
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.container, busquedaUsuarios).commit();
                    return true;
                }

                return false;
            }
        });
    }
}