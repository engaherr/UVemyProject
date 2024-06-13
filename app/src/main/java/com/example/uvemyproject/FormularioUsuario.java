package com.example.uvemyproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.uvemyproject.databinding.FragmentFormularioUsuarioBinding;
import com.example.uvemyproject.utils.CredencialesValidador;

public class FormularioUsuario extends Fragment {

    private FragmentFormularioUsuarioBinding binding;

    boolean esActualizacion;
    boolean esContrasenaVisible;
    boolean esContrasenaRepetidaVisible;

    public FormularioUsuario() {
    }

    public FormularioUsuario(boolean esActualizacion) {
        this.esActualizacion = esActualizacion;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormularioUsuarioBinding.inflate(getLayoutInflater());

        if(!esActualizacion) {
            binding.imgViewRegresar.setVisibility(View.GONE);
            binding.txtViewImagen.setVisibility(View.GONE);
            binding.imgViewPerfil.setVisibility(View.GONE);
            binding.btnSubirImagen.setVisibility(View.GONE);
        } else {
            binding.txtViewBienvenida.setVisibility(View.GONE);
        }

        setEditTextNombreListener();
        setEditTextApellidosListener();
        setEditTextCorreoListener();
        setEditTextContrasenasListeners();

        binding.imgViewVerContrasena.setOnClickListener(c -> {
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

        binding.imgViewVerContrasenaRepetida.setOnClickListener(c -> {
            if (esContrasenaRepetidaVisible) {
                binding.edtTextContrasenaRepetida.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.imgViewVerContrasenaRepetida.setImageResource(R.drawable.ic_slashed_eye);
            } else {
                binding.edtTextContrasenaRepetida.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.imgViewVerContrasenaRepetida.setImageResource(R.drawable.ic_opened_eye);
            }
            binding.edtTextContrasenaRepetida.setSelection(
                    binding.edtTextContrasenaRepetida.getText().length());
            esContrasenaRepetidaVisible = !esContrasenaRepetidaVisible;
        });

        binding.btnRegistrarse.setOnClickListener(c -> {
            resetearCampos();
            if (validarCampos()) {
                //TODO: Registrar usuario con ViewModel
            }
        });

        binding.txtViewIniciarSesion.setOnClickListener(c -> {
            Intent intent = new Intent(getActivity(), InicioSesion.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void setEditTextNombreListener() {
        binding.edtTextNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                binding.edtTextNombre.setBackgroundResource(R.drawable.background_lightblue);
            }
        });
    }

    private void setEditTextApellidosListener() {
        binding.edtTextApellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                binding.edtTextApellidos.setBackgroundResource(R.drawable.background_lightblue);
            }
        });
    }

    private void setEditTextCorreoListener() {
        binding.edtTextCorreoElectronico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                binding.edtTextCorreoElectronico.setBackgroundResource(R.drawable.background_lightblue);
            }
        });
    }

    private void setEditTextContrasenasListeners() {
        binding.edtTextContrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                binding.edtTextContrasena.setBackgroundResource(R.drawable.background_lightblue);
            }
        });

        binding.edtTextContrasenaRepetida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                binding.edtTextContrasenaRepetida.setBackgroundResource(R.drawable.background_lightblue);
            }
        });
    }

    private boolean validarCampos() {
        boolean camposValidos = true;

        String nombre = binding.edtTextNombre.getText().toString().trim();
        String apellidos = binding.edtTextApellidos.getText().toString().trim();
        String correoElectronico = binding.edtTextCorreoElectronico.getText().toString().trim();
        String contrasena = binding.edtTextContrasena.getText().toString().trim();
        String contrasenaRepetida = binding.edtTextContrasenaRepetida.getText().toString().trim();

        if (nombre.isEmpty()) {
            binding.edtTextNombre.setBackgroundResource(R.drawable.background_red);
            Toast.makeText(getActivity(),"Nombre/s requerido/s", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (apellidos.isEmpty()) {
            binding.edtTextApellidos.setBackgroundResource(R.drawable.background_red);
            Toast.makeText(getActivity(),"Apellido/s requerido/s", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (correoElectronico.isEmpty()) {
            binding.edtTextCorreoElectronico.setBackgroundResource(R.drawable.background_red);
            Toast.makeText(getActivity(),"Correo electrónico requerido", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (!CredencialesValidador.esEmailValido(correoElectronico)) {
            binding.edtTextCorreoElectronico.setBackgroundResource(R.drawable.background_red);
            Toast.makeText(getActivity(),"Correo electrónico inválido", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (contrasena.isEmpty()) {
            binding.edtTextContrasena.setBackgroundResource(R.drawable.background_red);
            Toast.makeText(getActivity(),"Contraseña requerida", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (contrasenaRepetida.isEmpty()) {
            binding.edtTextContrasenaRepetida.setBackgroundResource(R.drawable.background_red);
            Toast.makeText(getActivity(),"Repite tu contraseña", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        }
        else if (!contrasena.equals(contrasenaRepetida)) {
            binding.edtTextContrasenaRepetida.setBackgroundResource(R.drawable.background_red);
            Toast.makeText(getActivity(),"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (!CredencialesValidador.esContrasenaSegura(contrasena)) {
            binding.edtTextContrasena.setBackgroundResource(R.drawable.background_red);
            Toast.makeText(getActivity(),"Contraseña inválida " +
                    "(debe contener al menos una minúscula, una mayúscula y un número)",
                    Toast.LENGTH_SHORT).show();
            camposValidos = false;
        }

        return camposValidos;
    }

    private void resetearCampos() {
        binding.edtTextNombre.setBackgroundResource(R.drawable.background_lightblue);
        binding.edtTextApellidos.setBackgroundResource(R.drawable.background_lightblue);
        binding.edtTextCorreoElectronico.setBackgroundResource(R.drawable.background_lightblue);
        binding.edtTextContrasena.setBackgroundResource(R.drawable.background_lightblue);
        binding.edtTextContrasenaRepetida.setBackgroundResource(R.drawable.background_lightblue);
    }
}