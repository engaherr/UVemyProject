package com.example.uvemyproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.uvemyproject.databinding.FragmentFormularioUsuarioBinding;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.utils.CredencialesValidador;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.example.uvemyproject.viewmodels.FormularioUsuarioViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class FormularioUsuario extends Fragment {

    private static final int TAMANO_MAXIMO_MB = 1024 * 1024;

    private FragmentFormularioUsuarioBinding binding;
    private FormularioUsuarioViewModel viewModel;
    boolean esActualizacion;
    boolean esContrasenaVisible;
    boolean esContrasenaRepetidaVisible;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    abrirGaleria();
                } else {
                    Toast.makeText(getActivity(), "Permiso denegado para acceder a la galeria",
                            Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> galeriaLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imagenSeleccionadaUri = result.getData().getData();
                    try {
                        File file = getFileFromUri(imagenSeleccionadaUri);

                        if (file.length() > TAMANO_MAXIMO_MB) {
                            Toast.makeText(getActivity(), "Imagen demasiado grande. Debe ser menor a 1MB",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            viewModel.subirImagenPerfil(file);
                        }
                    } catch (FileNotFoundException e) {
                        Log.e("FormularioUsuario", "Error al cargar la imagen", e);
                        Toast.makeText(getContext(), "Imagen no encontrada. Intente de nuevo",
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("FormularioUsuario", "Error al cargar la imagen", e);
                        Toast.makeText(getContext(), "Error al cargar la imagen. Intente de nuevo",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private File getFileFromUri(Uri imagenSeleccionadaUri) throws IOException {
        File file = new File(getContext().getCacheDir(), "foto_perfil.png");

        try (InputStream inputStream = getActivity().getContentResolver().openInputStream(imagenSeleccionadaUri);
             OutputStream outputStream = Files.newOutputStream(file.toPath())) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            if (inputStream != null) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }

        return file;
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/png");
        galeriaLauncher.launch(intent);
    }

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

        viewModel = new ViewModelProvider(this).get(FormularioUsuarioViewModel.class);

        if(!esActualizacion) {
            binding.imgViewRegresar.setVisibility(View.GONE);
            binding.txtViewImagen.setVisibility(View.GONE);
            binding.imgViewPerfil.setVisibility(View.GONE);
            binding.btnSubirImagen.setVisibility(View.GONE);
        } else {
            setUIActualizar();
            viewModel.obtenerFotoPerfil();
            observarImagenPerfil();
            observarStatusGetFotoPerfil();
            observarStatusActualizarDatos();
            observarStatusSubirFoto();
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
                continuarRegistro();
            }
        });

        binding.txtViewIniciarSesion.setOnClickListener(c -> {
            Intent intent = new Intent(getActivity(), InicioSesion.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void observarStatusSubirFoto() {
        viewModel.getstatusSubirFoto().observe(getViewLifecycleOwner(), status ->{
            switch (status) {
                case DONE:
                    Toast.makeText(getActivity(), "Imagen subida correctamente",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), "Error al subir la imagen. Intente de nuevo" +
                                    " o intentélo más tarde",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getActivity(), "No hay conexion al servidor. " +
                                    "Intentelo de nuevo más tarde",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void observarStatusActualizarDatos() {
        viewModel.getstatusActualizarDatos().observe(getViewLifecycleOwner(), status ->{
            switch (status) {
                case DONE:
                    Toast.makeText(getActivity(), "Datos actualizados correctamente",
                            Toast.LENGTH_SHORT).show();
                    binding.edtTextContrasena.setText("");
                    binding.edtTextContrasenaRepetida.setText("");
                    SingletonUsuario.setNombres(binding.edtTextNombre.getText().toString());
                    SingletonUsuario.setApellidos(binding.edtTextApellidos.getText().toString());
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), "Error al actualizar los datos",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getActivity(), "No hay conexion al servidor. " +
                                    "Intentelo de nuevo más tarde",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void observarStatusGetFotoPerfil() {
        viewModel.getstatusGetImagen().observe(getViewLifecycleOwner(), status ->{
            if (Objects.requireNonNull(status) == StatusRequest.ERROR_CONEXION) {
                Toast.makeText(getActivity(), "No hay conexion al servidor",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observarImagenPerfil() {
        viewModel.getImagenPerfil().observe(getViewLifecycleOwner(), imagenPerfil -> {
            if (imagenPerfil != null) {
                binding.imgViewPerfil.setImageBitmap(imagenPerfil);
            }
        });
    }

    private void setUIActualizar() {
        binding.txtViewTitulo.setText("Perfil");
        binding.txtViewBienvenida.setVisibility(View.GONE);
        binding.btnSubirImagen.setVisibility(View.GONE);
        binding.cnsLayoutContrasena.setVisibility(View.GONE);
        binding.txtViewContrasena.setVisibility(View.GONE);
        binding.txtViewContrasenaRepetida.setVisibility(View.GONE);
        binding.cnsLayoutContrasenaRepetida.setVisibility(View.GONE);
        binding.btnRegistrarse.setVisibility(View.GONE);
        binding.txtViewYaTienesCuenta.setVisibility(View.GONE);
        binding.txtViewIniciarSesion.setVisibility(View.GONE);
        binding.btnModificar.setVisibility(View.VISIBLE);
        binding.imgViewRegresar.setVisibility(View.GONE);
        binding.imgViewCerrarSesion.setVisibility(View.VISIBLE);

        binding.imgViewCerrarSesion.setOnClickListener(c -> cerrarSesion());

        binding.edtTextNombre.setEnabled(false);
        binding.edtTextApellidos.setEnabled(false);
        binding.edtTextCorreoElectronico.setEnabled(false);

        binding.edtTextNombre.setText(SingletonUsuario.getNombres());
        binding.edtTextApellidos.setText(SingletonUsuario.getApellidos());
        binding.edtTextCorreoElectronico.setText(SingletonUsuario.getCorreoElectronico());

        binding.btnModificar.setOnClickListener(c -> setUIModificar());
    }

    private void cerrarSesion() {
        SingletonUsuario.setNombres("");
        SingletonUsuario.setApellidos("");
        SingletonUsuario.setCorreoElectronico("");
        SingletonUsuario.setIdsEtiqueta(new int[0]);
        SingletonUsuario.setIdUsuario(0);
        SingletonUsuario.setJwt("");

        Intent intent = new Intent(getActivity(), InicioSesion.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        getActivity().finish();
    }

    private void setUIModificar() {
        binding.edtTextNombre.setEnabled(true);
        binding.edtTextApellidos.setEnabled(true);

        binding.txtViewTitulo.setText("Modificar perfil");
        binding.btnSubirImagen.setVisibility(View.VISIBLE);
        binding.cnsLayoutContrasena.setVisibility(View.VISIBLE);
        binding.txtViewContrasena.setVisibility(View.VISIBLE);
        binding.txtViewContrasena.setText("Contraseña (Opcional)");
        binding.txtViewContrasenaRepetida.setVisibility(View.VISIBLE);
        binding.cnsLayoutContrasenaRepetida.setVisibility(View.VISIBLE);
        binding.ctrLayoutEtiquetasActualizar.setVisibility(View.VISIBLE);
        binding.btnModificar.setVisibility(View.GONE);

        binding.btnActualizar.setOnClickListener(c -> clickActualizar());
        binding.btnSubirImagen.setOnClickListener(c -> clickSubirImagen());
        binding.btnEtiquetas.setOnClickListener(c -> clickEtiquetas());
    }

    private void clickEtiquetas() {
        SeleccionEtiquetas seleccionEtiquetas = new SeleccionEtiquetas(
                null,true);
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(seleccionEtiquetas);
    }

    private void clickSubirImagen() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void clickActualizar() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        String contrasena = binding.edtTextContrasena.getText().toString().trim();
        String contrasenaRepetida = binding.edtTextContrasenaRepetida.getText().toString().trim();

        if (contrasena.isEmpty() && contrasenaRepetida.isEmpty()) {
            binding.edtTextContrasena.setText("Contrasena1");
            binding.edtTextContrasenaRepetida.setText("Contrasena1");

            if (validarCampos()) {
                binding.edtTextContrasena.setText("");
                binding.edtTextContrasenaRepetida.setText("");

                ponerEspera();
                usuarioDTO.setNombres(binding.edtTextNombre.getText().toString().trim());
                usuarioDTO.setApellidos(binding.edtTextApellidos.getText().toString().trim());
                viewModel.actualizarDatosUsuario(usuarioDTO);
            }
        } else if(validarCampos()){
            ponerEspera();
            usuarioDTO.setNombres(binding.edtTextNombre.getText().toString().trim());
            usuarioDTO.setApellidos(binding.edtTextApellidos.getText().toString().trim());
            usuarioDTO.setContrasena(contrasena);
            viewModel.actualizarDatosUsuario(usuarioDTO);
        }
    }

    private void continuarRegistro() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombres(binding.edtTextNombre.getText().toString());
        usuario.setApellidos(binding.edtTextApellidos.getText().toString());
        usuario.setCorreoElectronico(binding.edtTextCorreoElectronico.getText().toString());
        usuario.setContrasena(binding.edtTextContrasena.getText().toString());
        viewModel.setUsuario(new MutableLiveData<>(usuario));

        SeleccionEtiquetas seleccionEtiquetas = new SeleccionEtiquetas(viewModel.getUsuario().getValue(),false);

        ((InicioSesion) getActivity()).cambiarFragmentoPrincipal(seleccionEtiquetas);
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
            binding.edtTextNombre.setBackgroundResource(R.drawable.background_errorcampo);
            Toast.makeText(getActivity(),"Nombre/s requerido/s", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (apellidos.isEmpty()) {
            binding.edtTextApellidos.setBackgroundResource(R.drawable.background_errorcampo);
            Toast.makeText(getActivity(),"Apellido/s requerido/s", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (correoElectronico.isEmpty()) {
            binding.edtTextCorreoElectronico.setBackgroundResource(R.drawable.background_errorcampo);
            Toast.makeText(getActivity(),"Correo electrónico requerido", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (!CredencialesValidador.esEmailValido(correoElectronico)) {
            binding.edtTextCorreoElectronico.setBackgroundResource(R.drawable.background_errorcampo);
            Toast.makeText(getActivity(),"Correo electrónico inválido", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (contrasena.isEmpty()) {
            binding.edtTextContrasena.setBackgroundResource(R.drawable.background_errorcampo);
            Toast.makeText(getActivity(),"Contraseña requerida", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (contrasenaRepetida.isEmpty()) {
            binding.edtTextContrasenaRepetida.setBackgroundResource(R.drawable.background_errorcampo);
            Toast.makeText(getActivity(),"Repite tu contraseña", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        }
        else if (!contrasena.equals(contrasenaRepetida)) {
            binding.edtTextContrasenaRepetida.setBackgroundResource(R.drawable.background_errorcampo);
            Toast.makeText(getActivity(),"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            camposValidos = false;
        } else if (!CredencialesValidador.esContrasenaSegura(contrasena)) {
            binding.edtTextContrasena.setBackgroundResource(R.drawable.background_errorcampo);
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

    private void ponerEspera() { binding.progressOverlay.setVisibility(View.VISIBLE); }

    private void quitarEspera() { binding.progressOverlay.setVisibility(View.GONE); }
}