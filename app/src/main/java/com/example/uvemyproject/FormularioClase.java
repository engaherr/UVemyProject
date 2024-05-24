package com.example.uvemyproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentFormularioClaseBinding;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.interfaces.INotificacionDocumento;
import com.example.uvemyproject.utils.FileUtil;
import com.example.uvemyproject.viewmodels.FormularioClaseViewModel;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormularioClase#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormularioClase extends Fragment implements INotificacionDocumento {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormularioClase() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormularioClase.
     */
    // TODO: Rename and change types and number of parameters
    public static FormularioClase newInstance(String param1, String param2) {
        FormularioClase fragment = new FormularioClase();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FragmentFormularioClaseBinding binding;
    private FormularioClaseViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormularioClaseBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(FormularioClaseViewModel.class);

        DocumentoAdapter adapter = new DocumentoAdapter(this);
        binding.rcyViewListadoDocumentos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewListadoDocumentos.setAdapter(adapter);

        viewModel.getDocumentosClase().observe(getViewLifecycleOwner(), documentoDTOS -> {
            adapter.submitList(documentoDTOS);
            adapter.notifyDataSetChanged();
        });

        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case DONE:
                    Toast.makeText(getContext(),"Se ha creado la clase", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"Ocurrió un error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        viewModel.getClaseNueva().observe(getViewLifecycleOwner(), claseNueva ->{
            guardarDocumentos();
        });

        binding.btnGuardarClase.setOnClickListener(v -> {
            if(validarCampos()){
                guardarClase();
            }
        });

        binding.btnAgregarDocumento.setOnClickListener(v -> mostrarFileChooserPDF());
        binding.btnAgregarVideo.setOnClickListener(v -> mostrarFileChooserVideo());
        binding.btnEliminarVideo.setOnClickListener(v -> eliminarVideo());

        return binding.getRoot();
    }

    private Boolean validarCampos(){
        return true;
    }

    private void guardarClase(){
        ClaseDTO claseNueva = new ClaseDTO();
        claseNueva.setNombre("Nuevaaaaa");
        claseNueva.setDescripcion("Android");
        claseNueva.setIdCurso(1);

        viewModel.guardarClaseNueva(claseNueva);
    }

    private void guardarDocumentos(){
        Log.i("Guardando", "Documentos");
    }

    private static final int PICK_PDF_FILE = 2;

    private void mostrarFileChooserPDF() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    PICK_PDF_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private static final int PICK_VIDEO_FILE = 1;
    private void mostrarFileChooserVideo() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/mp4");
        startActivityForResult(intent, PICK_VIDEO_FILE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null){
            Uri uri = data.getData();
            String nombreDocumento = obtenerNombreDocumento(uri);

            if(nombreDocumento != null){
                switch (requestCode){
                    case PICK_PDF_FILE:
                        agregarDocumento(uri, nombreDocumento);
                        break;
                    case PICK_VIDEO_FILE:
                        agregarVideo(uri, nombreDocumento);
                        break;
                }
            }else{
                Toast.makeText(getContext(),"Ocurrió un error al agregar el documento", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void agregarDocumento(Uri uri, String nombre) {
        File file = FileUtil.uriToFile(getContext(), uri, nombre);
        if(file != null){
            viewModel.agregarDocumento(file);
        }else{
            Toast.makeText(getContext(),"Ocurrió un error al agregar el archivo", Toast.LENGTH_SHORT).show();
        }
    }

    public String obtenerNombreDocumento(Uri uri) {
        String nombre = null;
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if(columIndex >= 0){
                    nombre = cursor.getString(columIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return nombre;
    }

    @Override
    public void eliminarDocumento(int posicionDocumento) {
        viewModel.eliminarDocumento(posicionDocumento);
    }

    private void agregarVideo(Uri uri, String nombre){
        File file = FileUtil.uriToFile(getContext(), uri, nombre);
        if(file != null){
            viewModel.agregarVideo(file);
            binding.videoView.setVideoURI(uri);
            MediaController mediaController = new MediaController(getContext());

            binding.videoView.setMediaController(mediaController);
            mediaController.setAnchorView(binding.videoView);
            binding.videoView.setVisibility(View.VISIBLE);

            binding.btnAgregarVideo.setEnabled(false);
            binding.btnEliminarVideo.setEnabled(true);
            setDarkBlueStyle(binding.btnEliminarVideo);
            setLightBlueStyle(binding.btnAgregarVideo);
        }
    }

    private void eliminarVideo(){
        viewModel.eliminarVideo();
        binding.videoView.stopPlayback();
        binding.videoView.setVideoURI(null);
        binding.videoView.setVisibility(View.INVISIBLE);

        setDarkBlueStyle(binding.btnAgregarVideo);
        setLightBlueStyle(binding.btnEliminarVideo);
        binding.btnAgregarVideo.setEnabled(true);
        binding.btnEliminarVideo.setEnabled(false);
    }

    private void setDarkBlueStyle(Button button){
        button.setBackgroundResource(R.drawable.background_darkerblue);
        button.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    private void setLightBlueStyle(Button button){
        button.setBackgroundResource(R.drawable.background_lighterblue);
        button.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
    }
}