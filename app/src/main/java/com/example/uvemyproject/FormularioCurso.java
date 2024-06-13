package com.example.uvemyproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
    private ArrayList<Integer> _listIdEtiquetas = new ArrayList<Integer>();
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
            viewModel.setCursoActual(_curso);
        }
        //Si es crear curso hago algo sino es modificar curso, dependiento de si tiene idCurso
        binding = FragmentFormularioCursoBinding.inflate(inflater, container, false);
        if (_esCrearCurso == false) {
            binding.btnEliminarCurso.setOnClickListener(v -> {

            });
            binding.btnGuardarCurso.setOnClickListener(v -> {
                //Modificar curso
            });
        }
        else {
            binding.btnGuardarCurso.setOnClickListener(v -> {
                if(validarCampos()){
                    GuardarDatosCurso();
                    GuardarCurso();
                }
            });
        }
        binding.btnAnadirTemas.setOnClickListener(v -> {
            GuardarDatosCurso();

        });
        binding.btnEliminarMiniatura.setOnClickListener(v -> {
            binding.imageView.setImageResource(android.R.drawable.ic_menu_upload);
            viewModel.setArrayBites(null);
        });
        binding.imageView.setOnClickListener(v -> {
            openGallery();
        });
        IngresarEtiquetas();



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

    private void IngresarEtiquetas(){
        List<String> listaNombres = new ArrayList<>();
        //listaNombres.add("Etiqueta 1");
        //listaNombres.add("Etiqueta 2");
        //listaNombres.add("Etiqueta 3");
        //ArrayList<Integer> listaID = new ArrayList<>();
        //listaID.add(1);
        //listaID.add(2);
        //viewModel.setListEtiquetas(listaID);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, listaNombres);
        //binding.lstView.setAdapter(adapter);
    }
    private void GuardarCurso(){
        viewModel.guardarCurso(viewModel.getCursoActual().getValue());
    }

    private void GuardarDatosCurso(){
        CrearCursoDTO curso = new CrearCursoDTO();
        curso = viewModel.getCursoActual().getValue();
        String tituloCurso = String.valueOf(binding.dtTextTitulo.getText()).trim();
        String descripcionCurso = String.valueOf(binding.dtTextDescripcion.getText()).trim();
        String requisitosCurso = String.valueOf(binding.dtTextRequisitos.getText()).trim();
        String objetivosCurso = String.valueOf(binding.dtTextObjetivos.getText()).trim();
        curso.setTitulo(tituloCurso);
        curso.setDescripcion(descripcionCurso);
        curso.setRequisitos(requisitosCurso);
        curso.setObjetivos(objetivosCurso);
        viewModel.setCursoActual(curso);
    }

    private boolean validarCampos(){
        boolean sonCamposValidos = true;
        boolean sonEtiquetasValidas = true;
        CrearCursoDTO curso = viewModel.getCursoActual().getValue();
        String tituloCurso = String.valueOf(binding.dtTextTitulo.getText()).trim();
        String descripcionCurso = String.valueOf(binding.dtTextDescripcion.getText()).trim();
        String requisitosCurso = String.valueOf(binding.dtTextRequisitos.getText()).trim();
        String objetivosCurso = String.valueOf(binding.dtTextObjetivos.getText()).trim();

        binding.dtTextTitulo.setBackgroundResource(R.drawable.background_lightblue);
        binding.dtTextDescripcion.setBackgroundResource(R.drawable.background_lightblue);
        binding.dtTextRequisitos.setBackgroundResource(R.drawable.background_lightblue);
        binding.dtTextObjetivos.setBackgroundResource(R.drawable.background_lightblue);
        binding.imageView.setBackgroundColor(Color.parseColor("#00000000"));
        binding.lstView.setBackgroundResource(R.drawable.background_commentary);
        if(tituloCurso.isEmpty() || tituloCurso.length() >= 150){
            sonCamposValidos = false;
            binding.dtTextTitulo.setBackgroundResource(R.drawable.background_errorcampo);
        }
        if(descripcionCurso.isEmpty() || descripcionCurso.length() >= 660){
            sonCamposValidos = false;
            binding.dtTextDescripcion.setBackgroundResource(R.drawable.background_errorcampo);
        }
        if(requisitosCurso.isEmpty() || requisitosCurso.length() >= 300){
            sonCamposValidos = false;
            binding.dtTextRequisitos.setBackgroundResource(R.drawable.background_errorcampo);
        }
        if(objetivosCurso.isEmpty() || objetivosCurso.length() >= 300){
            sonCamposValidos = false;
            binding.dtTextObjetivos.setBackgroundResource(R.drawable.background_errorcampo);
        }
        if(curso.archivo == null){
            sonCamposValidos = false;
            binding.imageView.setBackgroundColor(Color.parseColor("#8A1818"));
        }
        if(curso.etiquetas.isEmpty()){
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
                            binding.imageView.setImageBitmap(bitmap);
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