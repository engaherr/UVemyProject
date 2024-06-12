package com.example.uvemyproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentClaseDetallesBinding;
import com.example.uvemyproject.databinding.FragmentListadoClasesBinding;
import com.example.uvemyproject.viewmodels.ClaseDetallesViewModel;
import com.example.uvemyproject.viewmodels.EstadisticasCursoViewModel;
import com.example.uvemyproject.viewmodels.FormularioDetallesClaseViewModel;

public class ClaseDetalles extends Fragment {
    private FragmentClaseDetallesBinding binding;
    private ClaseDetallesViewModel viewModel;
    private FormularioDetallesClaseViewModel viewModelCompartido;
    private DocumentoAdapter adapter;
    private int documentoSeleccionado = -1;
    private static final int PICK_DIRECTORY_REQUEST_CODE = 1;

    public ClaseDetalles() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClaseDetallesBinding.inflate(getLayoutInflater());
        binding.imgViewRegresar.setOnClickListener( v -> {
            CursoDetallesPrincipal cursoDetalles = new CursoDetallesPrincipal();
            ((MainActivity) getActivity()).cambiarFragmentoPrincipal(cursoDetalles);
        });

        adapter = new DocumentoAdapter(false);
        binding.rcyViewDocumentos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewDocumentos.setAdapter(adapter);
        adapter.setOnItemClickListener((documento, posicion) -> descargarDocumento(posicion));

        viewModel = new ViewModelProvider(this).get(ClaseDetallesViewModel.class);

        viewModelCompartido = new ViewModelProvider(requireActivity()).get(FormularioDetallesClaseViewModel.class);

        observarStatus();
        observarClase();

        binding.lnrLayoutModificarClase.setOnClickListener(v -> cambiarFormularioClase());

        obtenerIdClase();

        return binding.getRoot();
    }
    private void observarStatus(){
        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor, no se pudieron recuperar los datos de la clase", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void observarClase(){
        viewModel.getClaseActual().observe(getViewLifecycleOwner(), claseDTO -> {
            if(claseDTO != null){
                binding.setClase(claseDTO);
                if(claseDTO.getDocumentos() != null && claseDTO.getDocumentos().size() > 0){
                    adapter.submitList(claseDTO.getDocumentos());
                    adapter.notifyDataSetChanged();
                }
                viewModelCompartido.setClase(claseDTO);
            }
        });
    }

    private void obtenerIdClase(){
        Bundle args = getArguments();
        if (args != null) {
            int esEstudiante = args.getInt("es_estudiante");
            if(esEstudiante > 0){
                binding.lnrLayoutModificarClase.setVisibility(View.INVISIBLE);
            }
            int idClase = args.getInt("id_clase");
            viewModel.recuperarDetallesClase(idClase);
            ponerEspera();
        }
    }

    private void cambiarFormularioClase(){
        FormularioClase formularioClase = new FormularioClase();
        /*Bundle bundle = new Bundle();
        bundle.putParcelable("clave_clase_dto", viewModel.getClaseActual().getValue());
        formularioClase.setArguments(bundle);*/
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(formularioClase);
    }

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera(){
        binding.progressOverlay.setVisibility(View.GONE);
    }

    private void descargarDocumento(int posicionDocumento){
        documentoSeleccionado = posicionDocumento;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona un directorio"), PICK_DIRECTORY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DIRECTORY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri treeUri = data.getData();
            int respuesta = viewModel.descargarDocumento(getContext(), treeUri, documentoSeleccionado);

            if(respuesta == 0){
                Toast.makeText(getContext(),"Se descargó existosamente el documento", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Hubo un problema al descargar el documento", Toast.LENGTH_SHORT).show();
            }

            documentoSeleccionado = -1;
        }

    }
}