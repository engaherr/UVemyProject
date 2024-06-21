package com.example.uvemyproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.FragmentListadoCursosBinding;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.utils.StatusRequest;
import com.example.uvemyproject.viewmodels.ListadoCursosViewModel;

public class ListadoCursos extends Fragment implements ListadoCursosAdapter.OnCursoClickListener{
    FragmentListadoCursosBinding binding;
    ListadoCursosViewModel viewModel;

    int _paginaActual;
    int _paginaAnterior;
    private String _tituloCurso = "";
    private int _calificacionCurso;
    private int _idEtiqueta;
    private int _idTipoCurso;
    private String _nombreEtiqueta;
    private String _nombreTipoCurso;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ListadoCursosViewModel.class);
        _paginaActual = 0;
        _paginaAnterior = 0;
        _tituloCurso= "";
        _calificacionCurso = 0;
        _idTipoCurso = 0;
        _idEtiqueta = 0;
        if (getArguments() != null) {
            _paginaActual = 0;
            _paginaAnterior = 0;
            _idEtiqueta = getArguments().getInt("clave_idEtiqueta");
            _idTipoCurso = getArguments().getInt("clave_idTipoCurso");
            _nombreEtiqueta =getArguments().getString("clave_nombreEtiqueta");
            _nombreTipoCurso = getArguments().getString("clave_nombreTipoCurso");
            _tituloCurso = getArguments().getString("clave_titulo");
            _calificacionCurso = getArguments().getInt("clave_calificacion");
        }
        binding = FragmentListadoCursosBinding.inflate(inflater, container, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        ponerEspera();
        binding.rcyViewListadoCursos.setLayoutManager(layoutManager2);
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
        viewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<StatusRequest>() {
            @Override
            public void onChanged(StatusRequest status) {
                switch (status) {
                    case DONE:
                        Toast.makeText(requireContext(), "Solicitud exitosa", Toast.LENGTH_LONG).show();
                        binding.txtViewPagina.setText("Pagina "+(_paginaActual+1));
                        quitarEspera();
                        break;
                    case ERROR:
                        Toast.makeText(requireContext(), "Error en la solicitud", Toast.LENGTH_LONG).show();
                        binding.txtViewPagina.setText("Pagina "+(_paginaActual+1));
                        quitarEspera();
                        break;
                    case ERROR_CONEXION:
                        Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                        binding.txtViewPagina.setText("Pagina "+(_paginaActual+1));
                        quitarEspera();
                        break;
                    case NOT_FOUND:
                        Toast.makeText(requireContext(), "No existen cursos", Toast.LENGTH_LONG).show();
                        if(_paginaActual < 0){
                            _paginaActual = 0;
                        }
                        binding.txtViewPagina.setText("Pagina "+(_paginaActual+1));
                        quitarEspera();
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
        recuperarCursos(_paginaActual, _tituloCurso, _calificacionCurso, _idTipoCurso, _idEtiqueta);

        return binding.getRoot();
    }

    private void siguientePagina()
    {
        ponerEspera();
        _paginaAnterior = _paginaActual;
        _paginaActual = _paginaActual + 1;
        recuperarCursos(_paginaActual, _tituloCurso, _calificacionCurso, _idTipoCurso, _idEtiqueta);
    }

    private void recuperarCursos(int pagina, String tituloCurso, int calificacionCurso, int idTipoCurso, int idEtiqueta){
        viewModel.recuperarCursos(pagina, tituloCurso, calificacionCurso, idTipoCurso, idEtiqueta);
        if(tituloCurso != null && !tituloCurso.isEmpty()){
            binding.txtViewCurso.setText("Cursos con titulo "+tituloCurso);
        } else if(calificacionCurso != 0){
            binding.txtViewCurso.setText("Cursos con calificación mayor a "+calificacionCurso);
        } else if(idTipoCurso != 0){
            binding.txtViewCurso.setText(_nombreTipoCurso);
        } else if(idEtiqueta != 0){
            binding.txtViewCurso.setText("Cursos con etiqueta "+_nombreEtiqueta);
        }
        binding.txtViewPagina.setText("Pagina "+(pagina+1));

    }
    private void anteriorPagina()
    {
        if (_paginaActual < 0)
        {
            _paginaActual = 0;
        }
        else
        {
            ponerEspera();
            _paginaActual = _paginaActual - 1;
            viewModel.recuperarCursos(_paginaActual, _tituloCurso, _calificacionCurso, _idTipoCurso, _idEtiqueta);
        }
    }

    private void cargarCursos(){
        ListadoCursosAdapter cursoAdapter = new ListadoCursosAdapter(getContext(), viewModel.getCursos().getValue(),this);
        binding.rcyViewListadoCursos.setAdapter(cursoAdapter);
        cursoAdapter.notifyDataSetChanged();

        RecyclerView.Adapter adapter2 = binding.rcyViewListadoCursos.getAdapter();
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
