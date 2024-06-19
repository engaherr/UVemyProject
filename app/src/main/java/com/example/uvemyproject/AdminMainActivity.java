package com.example.uvemyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.uvemyproject.utils.SingletonUsuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminMainActivity extends AppCompatActivity {

    final int ETIQUETAS_ID = R.id.etiquetas;
    final int USUARIOS_ID = R.id.usuarios;
    final int BUSQUEDA_ID = R.id.busqueda;
    final int SALIR_ID = R.id.salir;

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

                if (itemId == SALIR_ID) {
                    mostrarDialogoSalir();
                    return true;
                }

                return false;
            }
        });
    }

    private void mostrarDialogoSalir() {
        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Está seguro que desea cerrar la sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        limpiarSingletonYSalir();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void limpiarSingletonYSalir() {
        SingletonUsuario.setJwt(null);
        SingletonUsuario.setIdUsuario(0);
        SingletonUsuario.setNombres(null);
        SingletonUsuario.setApellidos(null);
        SingletonUsuario.setCorreoElectronico(null);
        SingletonUsuario.setIdsEtiqueta(null);
        SingletonUsuario.setEsAdministrador(0);

        Intent intent = new Intent(AdminMainActivity.this, InicioSesion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}