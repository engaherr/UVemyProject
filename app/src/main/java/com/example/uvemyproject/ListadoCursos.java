package com.example.uvemyproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.FragmentFormularioCursoBinding;
import com.example.uvemyproject.databinding.FragmentListadoCursosBinding;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.utils.StatusRequest;
import com.example.uvemyproject.viewmodels.FormularioCursoViewModel;
import com.example.uvemyproject.viewmodels.ListadoCursosAdapter;
import com.example.uvemyproject.viewmodels.ListadoCursosViewModel;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class ListadoCursos extends Fragment implements ListadoCursosAdapter.OnCursoClickListener{
    FragmentListadoCursosBinding binding;
    ListadoCursosViewModel viewModel;

    int _paginaActual;
    int _paginaAnterior;
    private String _tituloCurso = "";
    private int _calificacionCurso;
    private int _idEtiqueta;
    private int _idTipoCurso;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ListadoCursosViewModel.class);
        _paginaActual = 0;
        _tituloCurso= "";
        _calificacionCurso = 8;
        _idTipoCurso = 0;
        _idEtiqueta = 0;
        Log.d("Log", "Listado cursos: ");
        if (getArguments() != null) {
            Log.d("Log", "If: ");
        } else{
            Log.d("Log", "Else: ");
        }
        viewModel.recuperarCursos(_paginaActual, _tituloCurso, _calificacionCurso, _idTipoCurso, _idEtiqueta);
        binding = FragmentListadoCursosBinding.inflate(inflater, container, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        ponerEspera();
        binding.rcyViewListadoCursos2.setLayoutManager(layoutManager2);
        binding.txtViewCrearCurso.setOnClickListener(v -> {
            FormularioCurso formularioCurso = new FormularioCurso();
            ((MainActivity) getActivity()).cambiarFragmentoPrincipal(formularioCurso);
        });
        binding.btnAnterior.setOnClickListener(v -> {
            anteriorPagina();
        });
        binding.btnSiguiente.setOnClickListener(v -> {
            siguientePagina();
        });
        //Si tiene un argumento puede ser de etiquetas o de tipo
        viewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<StatusRequest>() {
            @Override
            public void onChanged(StatusRequest status) {
                switch (status) {
                    case DONE:
                        Toast.makeText(requireContext(), "Solicitud exitosa", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR:
                        Toast.makeText(requireContext(), "Error en la solicitud", Toast.LENGTH_LONG).show();
                        quitarEspera();
                        break;
                    case ERROR_CONEXION:
                        Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });

        viewModel.getCursos().observe(getViewLifecycleOwner(), curso ->{
            if(curso != null && curso.size()>0){
                cargarCursos();
            }
        });
        return binding.getRoot();
    }

    private void siguientePagina()
    {
        ponerEspera();
        _paginaAnterior = _paginaActual;
        _paginaActual = _paginaActual + 1;
        viewModel.recuperarCursos(_paginaActual, _tituloCurso, _calificacionCurso, _idTipoCurso, _idEtiqueta);
        //_ = CargarCursosPagina(_paginaActual, _tituloCurso, _calificacionCurso, _idTipoCurso, _idEtiqueta);
    }
    private void anteriorPagina()
    {
        if (_paginaActual <= 0)
        {
            _paginaActual = 0;
        }
        else
        {
            ponerEspera();
            _paginaActual = _paginaActual - 1;
            viewModel.recuperarCursos(_paginaActual, _tituloCurso, _calificacionCurso, _idTipoCurso, _idEtiqueta);
            //_ = CargarCursosPagina(_paginaActual, _tituloCurso, _calificacionCurso, _idTipoCurso, _idEtiqueta);
        }
    }

    private void cargarCursos(){
        Log.d("Log","Tamaño: "+viewModel.getCursos().getValue().size());
        ListadoCursosAdapter cursoAdapter = new ListadoCursosAdapter(getContext(), viewModel.getCursos().getValue(),this);
        //binding.rcyViewListadoCursos1.setAdapter(cursoAdapter);
        binding.rcyViewListadoCursos2.setAdapter(cursoAdapter);
        cursoAdapter.notifyDataSetChanged();

        RecyclerView.Adapter adapter2 = binding.rcyViewListadoCursos2.getAdapter();
        //RecyclerView.Adapter adapter1 = binding.rcyViewListadoCursos1.getAdapter();
        //Log.d("Log", "Número de elementos en rcyViewListadoCursos1: " + adapter1.getItemCount());
        Log.d("Log", "Número de elementos en rcyViewListadoCursos2: " + adapter2.getItemCount());
        cursoAdapter.notifyDataSetChanged();

        quitarEspera();

    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    @Override
    public void onCursoClick(int idCurso, String titulo, byte[] archivo) {
        Log.d("Log", "Click: "+idCurso);
        CursoDetallesPrincipal cursoDetallesPrincipal = new CursoDetallesPrincipal();
        Bundle bundle = new Bundle();
        CursoDTO curso = new CursoDTO();
        curso.setIdCurso(idCurso);
        curso.setTitulo(titulo);
        curso.setArchivo(archivo);
        bundle.putParcelable("clave_curso", curso);
        cursoDetallesPrincipal.setArguments(bundle);
        cambiarFragmentoPrincipal(cursoDetallesPrincipal);
    }
    private void cambiarFragmentoPrincipal(Fragment fragmento){
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(fragmento);
    }

    private void ponerEspera() {
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera() {
        binding.progressOverlay.setVisibility(View.GONE);
    }
}
