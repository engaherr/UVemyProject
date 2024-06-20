package com.example.uvemyproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.FragmentBuscarCursosBinding;
import com.example.uvemyproject.databinding.FragmentCursoDetallesPrincipalBinding;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.dto.ListaCursoDTO;
import com.example.uvemyproject.dto.TipoCursoDTO;
import com.example.uvemyproject.viewmodels.BuscarCursosViewModel;
import com.example.uvemyproject.viewmodels.CursoDetallesPrincipalViewModel;
import com.example.uvemyproject.viewmodels.EtiquetaAdapter;

import java.util.ArrayList;
import java.util.List;

public class BuscarCursos extends Fragment implements TipoCursoAdapter.OntipoCursoClickListener, EtiquetaAdapter.OnEtiquetasClickListener{
    private BuscarCursosViewModel viewModel;
    private FragmentBuscarCursosBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBuscarCursosBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(BuscarCursosViewModel.class);
        binding.imgBuscar.setOnClickListener(v -> {
            ListadoCursos listadoCursos = new ListadoCursos();
            Bundle bundle = new Bundle();
            bundle.putString("clave_titulo", binding.edtBuscarCursos.getText().toString());
            listadoCursos.setArguments(bundle);
            cambiarFragmentoPrincipal(listadoCursos);
        });
        binding.spinnerCalificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoCursoDTO selectedTipoCurso = (TipoCursoDTO) parent.getItemAtPosition(position);
                int idSeleccionado = selectedTipoCurso.getIdTipoCurso();
                String nombreSeleccionado = selectedTipoCurso.getNombre();
                if(idSeleccionado!=0){
                    binding.spinnerCalificacion.setSelection(0);
                    ListadoCursos listadoCursos = new ListadoCursos();
                    Bundle bundle = new Bundle();
                    bundle.putInt("clave_calificacion", idSeleccionado);
                    listadoCursos.setArguments(bundle);
                    cambiarFragmentoPrincipal(listadoCursos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ponerEspera();
        List<TipoCursoDTO> tiposCursos = new ArrayList<>();
        TipoCursoDTO tipoCurso1 = new TipoCursoDTO(1,"Cursos creados");
        TipoCursoDTO tipoCurso2 = new TipoCursoDTO(2,"Cursos inscritos");
        tiposCursos.add(tipoCurso1);
        tiposCursos.add(tipoCurso2);
        Log.d("Log","1");
        viewModel.setTiposCursos(tiposCursos);
        TipoCursoDTO[] tipoCalificaciones = new TipoCursoDTO[] {
                new TipoCursoDTO(0, "Buscar por calificaciones"),
                new TipoCursoDTO(2, "Calificaciones mayores a 2"),
                new TipoCursoDTO(4, "Calificaciones mayores a 4"),
                new TipoCursoDTO(6, "Calificaciones mayores a 6"),
                new TipoCursoDTO(8, "Calificaciones mayores a 8")
        };
        ArrayAdapter<TipoCursoDTO> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tipoCalificaciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCalificacion.setAdapter(adapter);
        binding.txtViewCrearCurso.setOnClickListener(v -> {
            FormularioCurso formularioCurso = new FormularioCurso();
            ((MainActivity) getActivity()).cambiarFragmentoPrincipal(formularioCurso);
        });
        viewModel.obtenerEtiquetas();
        observarStatus();
        observarEtiquetas();
        return binding.getRoot();
    }

    private void observarStatus(){
        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor", Toast.LENGTH_SHORT).show();
                    quitarEspera();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
                    quitarEspera();
                    break;
            }
        });
    }

    private void cargarTiposCursos(){
        Log.d("Log","Tamaño: "+viewModel.getTiposCursos().getValue().size());

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        binding.rcyViewListaTiposCursos.setLayoutManager(layoutManager2);
        TipoCursoAdapter tipoCursosAdapter = new TipoCursoAdapter(getContext(), viewModel.getTiposCursos().getValue(),this);
        binding.rcyViewListaTiposCursos.setAdapter(tipoCursosAdapter);
        tipoCursosAdapter.notifyDataSetChanged();
        Log.d("Log","Tamaño: "+tipoCursosAdapter.getItemCount());
    }

    private void cargarEtiquetas(){
        Log.d("Log","Tamaño Etiquetas: "+viewModel.getEtiquetas().getValue().size());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rcyViewListaEtiquetas.setLayoutManager(layoutManager);
        EtiquetaAdapter etiquetaAdapter = new EtiquetaAdapter(getContext(), viewModel.getEtiquetas().getValue(),this);
        binding.rcyViewListaEtiquetas.setAdapter(etiquetaAdapter);
        etiquetaAdapter.notifyDataSetChanged();
        Log.d("Log","Tamaño: "+etiquetaAdapter.getItemCount());
    }

    private void observarEtiquetas(){
        viewModel.getEtiquetas().observe(getViewLifecycleOwner(), etiquetas ->{
            if(etiquetas != null && etiquetas.size()>0){
                cargarTiposCursos();
                cargarEtiquetas();
                quitarEspera();
            }
        });
    }

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera(){
        binding.progressOverlay.setVisibility(View.GONE);
    }


    @Override
    public void OntipoCursoClickListener(int idTipoCurso, String nombreTipoCurso) {
        Log.d("Log", "ClickTipoCurso: "+idTipoCurso);
        ListadoCursos listadoCursos = new ListadoCursos();
        Bundle bundle = new Bundle();
        bundle.putInt("clave_idTipoCurso", idTipoCurso);
        bundle.putString("clave_nombreTipoCurso", nombreTipoCurso);
        listadoCursos.setArguments(bundle);
        cambiarFragmentoPrincipal(listadoCursos);
    }
    @Override
    public void OnEtiquetasClickListener(int idEtiqueta, String nombreEtiqueta) {
        Log.d("Log", "ClickEtiquetas: "+idEtiqueta);
        ListadoCursos listadoCursos = new ListadoCursos();
        Bundle bundle = new Bundle();
        bundle.putInt("clave_idEtiqueta", idEtiqueta);
        bundle.putString("clave_nombreEtiqueta", nombreEtiqueta);
        listadoCursos.setArguments(bundle);
        cambiarFragmentoPrincipal(listadoCursos);
    }
    private void cambiarFragmentoPrincipal(Fragment fragmento){
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(fragmento);
    }
}
