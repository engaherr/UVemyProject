package com.example.uvemyproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentCursoDetallesPrincipalBinding;
import com.example.uvemyproject.databinding.FragmentFormularioCursoBinding;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.utils.StatusRequest;
import com.example.uvemyproject.viewmodels.FormularioClaseViewModel;
import com.example.uvemyproject.viewmodels.FormularioCursoViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormularioCurso#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormularioCurso extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Bitmap takenPhotoBitmap;
    private String mParam1;
    private String mParam2;
    private boolean _esCrearCurso;
    private CrearCursoDTO _curso;
    private int idCurso;
    private byte[] _arrayImagen;
    private List<Integer> _listIdEtiquetas = new ArrayList<>();
    private List<String> _listNombreEtiquetas = new ArrayList<>();
    private FragmentFormularioCursoBinding binding;
    private FormularioCursoViewModel viewModel;
    public FormularioCurso() {
        // Required empty public constructor
    }
    public static FormularioCurso newInstance(String param1, String param2) {
        FormularioCurso fragment = new FormularioCurso();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FormularioCursoViewModel.class);
        if (getArguments() != null) {
            _esCrearCurso = getArguments().getBoolean("clave_esCrearCurso", true);
            _curso = getArguments().getParcelable("clave_curso");
            Log.d("Log", "Modificar "+_curso.getIdCurso()+" idCurso");
            viewModel.setCursoActual(_curso);
        }
        binding = FragmentFormularioCursoBinding.inflate(inflater, container, false);
        if (_esCrearCurso == false) {
            //Cargar el curso mostrar los botones
            cargarCurso();
            List<String> lista = viewModel.getCursoActual().getValue().getNombreEtiquetas();
            List<Integer> lista2 = viewModel.getCursoActual().getValue().getEtiquetas();
            for (String elemento : lista) {
                Log.d("Log", elemento+" ");
            }
            for (Integer elemento : lista2) {
                Log.d("Log", elemento+" ");
            }
            binding.btnGuardarCurso.setText("Modificar curso");
            binding.btnEliminarCurso.setVisibility(View.VISIBLE);
            binding.btnEliminarCurso.setOnClickListener(v -> {
                eliminarCurso();
            });
            binding.btnGuardarCurso.setOnClickListener(v -> {
                guardarDatosCurso();
                modificarCurso();
            });
        }
        else {
            binding.btnEliminarCurso.setVisibility(View.GONE);
            binding.btnGuardarCurso.setOnClickListener(v -> {
                if(validarCampos()){
                    guardarDatosCurso();
                    guardarCurso();
                }
            });
        }
        binding.btnAnadirTemas.setOnClickListener(v -> {
            guardarDatosCurso();

        });
        binding.btnEliminarMiniatura.setOnClickListener(v -> {
            binding.imgView.setImageResource(android.R.drawable.ic_menu_upload);
            viewModel.setArrayBites(null);
        });
        binding.imgView.setOnClickListener(v -> {
            openGallery();
        });
        ingresarEtiquetas();

        viewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<StatusRequest>() {
            @Override
            public void onChanged(StatusRequest status) {
                switch (status) {
                    case DONE:
                        Toast.makeText(requireContext(), "Solicitud exitosa", Toast.LENGTH_LONG).show();
                        Log.d("Log", "Done");
                        break;
                    case ERROR:
                        Toast.makeText(requireContext(), "Error en la solicitud", Toast.LENGTH_LONG).show();
                        Log.d("Log", "Error");
                        break;
                    case ERROR_CONEXION:
                        Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                        Log.d("Log", "Error de red o excepción");
                        break;
                    default:
                        break;
                }
            }
        });
        return binding.getRoot();
    }

    private void cargarCurso(){
        binding.edtTextDescripcion.setText(viewModel.getCursoActual().getValue().getDescripcion());
        binding.edtTextTitulo.setText(viewModel.getCursoActual().getValue().getTitulo());
        binding.edtTextObjetivos.setText(viewModel.getCursoActual().getValue().getObjetivos());
        binding.edtTextRequisitos.setText(viewModel.getCursoActual().getValue().getRequisitos());
        binding.imgView.setImageBitmap(BitmapFactory.decodeByteArray(_arrayImagen, 0, _arrayImagen.length));
    }

    private void eliminarCurso(){
        viewModel.eliminarCurso();
    }

    private void ingresarEtiquetas(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, viewModel.getCursoActual().getValue().getNombreEtiquetas());
        binding.lstView.setAdapter(adapter);
    }

    private void guardarCurso(){
        viewModel.guardarCurso(viewModel.getCursoActual().getValue());
    }

    private void modificarCurso(){
        viewModel.modificarCurso(viewModel.getCursoActual().getValue());
    }

    private void guardarDatosCurso(){

        CrearCursoDTO curso = new CrearCursoDTO();
        curso = viewModel.getCursoActual().getValue();
        String tituloCurso = String.valueOf(binding.edtTextTitulo.getText()).trim();
        String descripcionCurso = String.valueOf(binding.edtTextDescripcion.getText()).trim();
        String requisitosCurso = String.valueOf(binding.edtTextRequisitos.getText()).trim();
        String objetivosCurso = String.valueOf(binding.edtTextObjetivos.getText()).trim();
        curso.setTitulo(tituloCurso);
        curso.setDescripcion(descripcionCurso);
        curso.setRequisitos(requisitosCurso);
        curso.setObjetivos(objetivosCurso);
        curso.setEtiquetas(_listIdEtiquetas);
        curso.setNombreEtiquetas(_listNombreEtiquetas);
        curso.setArchivo(_arrayImagen);
        viewModel.setCursoActual(curso);
    }

    private boolean validarCampos(){
        boolean sonCamposValidos = true;
        boolean sonEtiquetasValidas = true;
        CrearCursoDTO curso = viewModel.getCursoActual().getValue();
        String tituloCurso = String.valueOf(binding.edtTextTitulo.getText()).trim();
        String descripcionCurso = String.valueOf(binding.edtTextDescripcion.getText()).trim();
        String requisitosCurso = String.valueOf(binding.edtTextRequisitos.getText()).trim();
        String objetivosCurso = String.valueOf(binding.edtTextObjetivos.getText()).trim();

        binding.edtTextTitulo.setBackgroundResource(R.drawable.background_lightblue);
        binding.edtTextDescripcion.setBackgroundResource(R.drawable.background_lightblue);
        binding.edtTextRequisitos.setBackgroundResource(R.drawable.background_lightblue);
        binding.edtTextObjetivos.setBackgroundResource(R.drawable.background_lightblue);
        binding.imgView.setBackgroundColor(Color.parseColor("#00000000"));
        binding.lstView.setBackgroundResource(R.drawable.background_commentary);
        if(tituloCurso.isEmpty() || tituloCurso.length() >= 150){
            sonCamposValidos = false;
            binding.edtTextTitulo.setBackgroundResource(R.drawable.background_errorcampo);
        }
        if(descripcionCurso.isEmpty() || descripcionCurso.length() >= 660){
            sonCamposValidos = false;
            binding.edtTextDescripcion.setBackgroundResource(R.drawable.background_errorcampo);
        }
        if(requisitosCurso.isEmpty() || requisitosCurso.length() >= 300){
            sonCamposValidos = false;
            binding.edtTextRequisitos.setBackgroundResource(R.drawable.background_errorcampo);
        }
        if(objetivosCurso.isEmpty() || objetivosCurso.length() >= 300){
            sonCamposValidos = false;
            binding.edtTextObjetivos.setBackgroundResource(R.drawable.background_errorcampo);
        }
        if(curso.getArchivo() == null){
            sonCamposValidos = false;
            binding.imgView.setBackgroundColor(Color.parseColor("#8A1818"));
        }
        if(curso.getEtiquetas().isEmpty()){
            sonCamposValidos = false;
            sonEtiquetasValidas = false;
            binding.lstView.setBackgroundColor(Color.parseColor("#8A1818"));
        }
        if(!sonCamposValidos){
            if (!sonEtiquetasValidas)
            {
                Toast.makeText(getContext(),"Campos invalidos, El curso debe tener por lo menos un tema de interes", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getContext(),"Campos inválidos, corrígalos por favor", Toast.LENGTH_LONG).show();
        }
        return sonCamposValidos;
    }


    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
                if(result.getResultCode()==RESULT_OK){
                    Intent data = result.getData();
                    if(data!=null){
                        Uri selectedImageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                            binding.imgView.setImageBitmap(bitmap);
                            _arrayImagen = bitmapToByteArray(bitmap);
                            viewModel.setArrayBites(_arrayImagen);

                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            });
}