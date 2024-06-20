package com.example.uvemyproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.uvemyproject.databinding.ActivityInicioSesionBinding;
import com.example.uvemyproject.utils.CredencialesValidador;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.viewmodels.InicioSesionViewModel;

public class InicioSesion extends AppCompatActivity {

    private static Context contexto;

    private ActivityInicioSesionBinding binding;
    private InicioSesionViewModel viewModel;

    boolean esContrasenaVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contexto = this;
        binding = ActivityInicioSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(InicioSesionViewModel.class);

        binding.imgViewVerContrasena.setOnClickListener(v -> {
            if (esContrasenaVisible) {
                binding.edtTextContrasena.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.imgViewVerContrasena.setImageResource(R.drawable.ic_slashed_eye);
            } else {
                binding.edtTextContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.imgViewVerContrasena.setImageResource(R.drawable.ic_opened_eye);
            }
            binding.edtTextContrasena.setSelection(binding.edtTextContrasena.getText().length());
            esContrasenaVisible = !esContrasenaVisible;
        });

        binding.txtViewRegistrate.setOnClickListener(c -> redireccionarRegistroUsuario());

        binding.edtTextCorreoElectronico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                binding.txtViewMensajeError.setText("");
            }
        });

        binding.edtTextContrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                binding.txtViewMensajeError.setText("");
            }
        });

        binding.btnIniciarSesion.setOnClickListener(c -> {
            resetearCampos();
            if (validarCampos()) {
                iniciarSesion(String.valueOf(binding.edtTextCorreoElectronico.getText()).trim(),
                        String.valueOf(binding.edtTextContrasena.getText()).trim());
            }
        });

        observarStatus();

        observarUsuario();
    }

    private void redireccionarRegistroUsuario() {
        FormularioUsuario formularioUsuario = new FormularioUsuario(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.frmLayoutInicioSesion,
                formularioUsuario).commit();
    }

    private void observarUsuario() {
        viewModel.getUsuarioActual().observe(this, usuario -> {
            if (usuario != null) {
                String mensajeExito = "Bienvenido a UVemy " + usuario.getNombres() +
                        " " + usuario.getApellidos();
                Toast.makeText(this,mensajeExito, Toast.LENGTH_LONG).show();
                SingletonUsuario.setIdUsuario(usuario.getIdUsuario());
                SingletonUsuario.setNombres(usuario.getNombres());
                SingletonUsuario.setApellidos(usuario.getApellidos());
                SingletonUsuario.setCorreoElectronico(usuario.getCorreoElectronico());
                SingletonUsuario.setIdsEtiqueta(usuario.getIdsEtiqueta());
                SingletonUsuario.setJwt(usuario.getJwt());
                SingletonUsuario.setEsAdministrador(usuario.getEsAdministrador());
                redireccionarMenuPrincipal();
            }
        });
    }

    private void observarStatus(){
        viewModel.getStatus().observe(this, status -> {
            switch (status) {
                case ERROR:
                    binding.txtViewMensajeError.setText("Correo electrónico o contraseña incorrectos");
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(this,"Ocurrió un error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void redireccionarMenuPrincipal() {
        Intent intent;
        if (SingletonUsuario.getEsAdministrador() == 1) {
            intent = new Intent(this, AdminMainActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void resetearCampos() {
        binding.edtTextCorreoElectronico.setBackgroundResource(R.drawable.background_lightblue);
        binding.edtTextContrasena.setBackgroundResource(R.drawable.background_lightblue);
    }

    private boolean validarCampos() {
        boolean sonCamposValidos = true;
        String mensajeError = "";
        String correoElectronico = String.valueOf(binding.edtTextCorreoElectronico.getText());
        String contrasena = String.valueOf(binding.edtTextContrasena.getText());

        if (correoElectronico.trim().isEmpty()) {
            sonCamposValidos = false;
            mensajeError = "Correo electrónico requerido";
            binding.edtTextCorreoElectronico.setBackgroundResource(R.drawable.background_errorcampo);
        } else if (!CredencialesValidador.esEmailValido(correoElectronico)) {
            sonCamposValidos = false;
            mensajeError = "Correo electrónico inválido";
        }

        if (contrasena.trim().isEmpty()) {
            sonCamposValidos = false;
            mensajeError += mensajeError.isEmpty() ? "Contraseña requerida" : " y la contraseña es requerida";
            binding.edtTextContrasena.setBackgroundResource(R.drawable.background_errorcampo);
        }

        if (!mensajeError.isEmpty()) {
            Toast.makeText(this,mensajeError,Toast.LENGTH_LONG).show();
        }
        return sonCamposValidos;
    }

    private void iniciarSesion(String correoElectronico, String contrasena) {
        ponerEspera();
        viewModel.iniciarSesion(correoElectronico, contrasena);
    }

    private void ponerEspera() {
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera() {
        binding.progressOverlay.setVisibility(View.GONE);
    }

    public void cambiarFragmentoPrincipal(Fragment fragmentoMostrar){
        binding.cnsLayoutInicioSesion.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.frmLayoutInicioSesion, fragmentoMostrar).addToBackStack(null).commit();
    }

    public static Context obtenerContexto(){
        return contexto;
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(binding.cnsLayoutInicioSesion.getVisibility() == View.GONE)
            redireccionarRegistroUsuario();
    }
}