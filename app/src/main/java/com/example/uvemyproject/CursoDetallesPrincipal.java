package com.example.uvemyproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentCursoDetallesPrincipalBinding;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.interfaces.INotificacionFragmentoClase;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.viewmodels.CursoClaseDetallesViewModel;
import com.example.uvemyproject.viewmodels.CursoDetallesPrincipalViewModel;
import com.example.uvemyproject.viewmodels.FormularioClaseViewModel;
import com.example.uvemyproject.viewmodels.FormularioDetallesClaseViewModel;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class CursoDetallesPrincipal extends Fragment implements INotificacionFragmentoClase {

    private CursoClaseDetallesViewModel viewModelCompartido;
    private FormularioDetallesClaseViewModel viewModelCompartidoFormulario;
    private CursoDetallesPrincipalViewModel viewModel;
    private FragmentCursoDetallesPrincipalBinding binding;
    private ListadoClases listadoClases;
    private CursoDetallesInformacion detallesCurso;
    private int fragmento = -1;
    public CursoDetallesPrincipal() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCursoDetallesPrincipalBinding.inflate(inflater, container, false);
        ponerEspera();
        viewModelCompartido = new ViewModelProvider(requireActivity()).get(CursoClaseDetallesViewModel.class);
        viewModelCompartidoFormulario = new ViewModelProvider(requireActivity()).get(FormularioDetallesClaseViewModel.class);

        viewModel = new ViewModelProvider(this).get(CursoDetallesPrincipalViewModel.class);
        if (getArguments() != null) {
            CursoDTO curso = getArguments().getParcelable("clave_curso");
            observarCurso();
            observarStatus();
            observarClases();
            observarInscripcion();
            obtenerIdCurso(curso);
        }
        binding.imgViewRegresar.setOnClickListener(v -> {
            regresar();
        });
        return binding.getRoot();
    }

    private void observarInscripcion(){
        viewModel.getInscripcion().observe(getViewLifecycleOwner(), inscripcion ->{
            if(inscripcion!=null || inscripcion > 0){
                CursoDetallesPrincipal cursoDetallesPrincipal = new CursoDetallesPrincipal();
                Bundle bundle = new Bundle();
                CursoDTO curso = viewModel.getCursoActual().getValue();
                bundle.putParcelable("clave_curso", curso);
                cursoDetallesPrincipal.setArguments(bundle);
                cambiarFragmentoPrincipal(cursoDetallesPrincipal);
            }
        });
    }

    private void regresar(){
        ListadoCursos listadoCursos = new ListadoCursos();
        int pagina = 0;
        Bundle bundle = new Bundle();
        bundle.putInt("clave_pagina", pagina);
        listadoCursos.setArguments(bundle);
        cambiarFragmentoPrincipal(listadoCursos);
    }

    private void observarCurso(){
        viewModel.getCursoActual().observe(getViewLifecycleOwner(), curso ->{
            if(curso != null && curso.getRol()!=null){
                viewModel.recuperarClases(viewModel.getCursoActual().getValue().getIdCurso());
                mostrarOpcionesSegunRol(curso.getRol());
                cargarCurso();
                detallesCurso = new CursoDetallesInformacion();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("clave_curso", curso);
                detallesCurso.setArguments(bundle1);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(binding.frgDetalles.getId(), detallesCurso).commit();
                fragmento = 2;

                viewModelCompartido.setCurso(curso);
                viewModelCompartidoFormulario.setCurso(curso);
            }
        });
    }

    private void cargarCurso(){
        binding.txtViewTitulo.setText(viewModel.getCursoActual().getValue().getTitulo());
        byte[] miniatura = viewModel.getCursoActual().getValue().getArchivo();
        Bitmap bitmap = BitmapFactory.decodeByteArray(miniatura, 0, miniatura.length);
        binding.imgViewMiniatura.setImageBitmap(bitmap);

    }

    private void observarStatus(){
        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor", Toast.LENGTH_SHORT).show();
                    regresar();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
                    regresar();
                    break;
            }
        });
    }

    private void observarClases(){
        viewModel.getClases().observe(getViewLifecycleOwner(), clases ->{
            if(clases != null){
                quitarEspera();
                binding.btnCambiarFragmento.setOnClickListener(v -> cambiarFragmento());
            }
        });
    }

    private void obtenerIdCurso(CursoDTO curso){
        viewModel.obtenerCurso(curso);
        ponerEspera();
    }

    private void mostrarOpcionesSegunRol(String rol){
        switch (rol){
            case "Profesor":
                mostrarOpcionesProfesor();
                break;
            case "Estudiante":
                mostrarOpcionesEstudiante();
                break;
            default:
                mostrarOpcionesUsuario();
                break;
        }
    }

    private void mostrarOpcionesProfesor(){
        binding.lnrLayoutEstadisticas.setVisibility(View.VISIBLE);
        binding.btnModificarCurso.setVisibility(View.VISIBLE);
        CrearCursoDTO cursoNuevo = new CrearCursoDTO();
        cursoNuevo.setIdCurso(viewModel.getCursoActual().getValue().getIdCurso());
        cursoNuevo.setTitulo(viewModel.getCursoActual().getValue().getTitulo());
        cursoNuevo.setObjetivos(viewModel.getCursoActual().getValue().getObjetivos());
        cursoNuevo.setDescripcion(viewModel.getCursoActual().getValue().getDescripcion());
        cursoNuevo.setRequisitos(viewModel.getCursoActual().getValue().getRequisitos());
        cursoNuevo.setIdDocumento(viewModel.getCursoActual().getValue().getIdDocumento());
        cursoNuevo.setArchivo(viewModel.getCursoActual().getValue().getArchivo());
        if(viewModel.getCursoActual().getValue().getEtiquetas() != null && viewModel.getCursoActual().getValue().getEtiquetas().size()>0){
            List<Integer> listaIdsEtiquetas = new ArrayList<>();
            List<String> listaNombresEtiquetas = new ArrayList<>();
            for (EtiquetaDTO etiqueta : viewModel.getCursoActual().getValue().getEtiquetas()) {
                int idEtiqueta = etiqueta.getIdEtiqueta();
                String nombreEtiqueta = etiqueta.getNombre();
                listaIdsEtiquetas.add(idEtiqueta);
                listaNombresEtiquetas.add(nombreEtiqueta);
            }
            cursoNuevo.setEtiquetas(listaIdsEtiquetas);
            cursoNuevo.setNombreEtiquetas(listaNombresEtiquetas);
        }
        binding.btnModificarCurso.setOnClickListener(v ->{
            FormularioCurso formularioCurso = new FormularioCurso();
            Bundle bundle = new Bundle();
            bundle.putBoolean("clave_esCrearCurso", false);
            bundle.putParcelable("clave_curso", cursoNuevo);
            formularioCurso.setArguments(bundle);
            cambiarFragmentoPrincipal(formularioCurso);
        });
        binding.lnrLayoutEstadisticas.setOnClickListener(v ->{
            EstadisticasCurso curso = new EstadisticasCurso();
            Bundle bundle = new Bundle();
            bundle.putInt("clave_id_curso", viewModel.getCursoActual().getValue().getIdCurso());
            curso.setArguments(bundle);
            cambiarFragmentoPrincipal(curso);
        });
    }

    private void mostrarOpcionesEstudiante(){
        binding.btnInscribirseCurso.setVisibility(View.INVISIBLE);
        binding.btnCalificarCurso.setVisibility(View.VISIBLE);
        binding.btnCalificarCurso.setOnClickListener(v ->{
            CalificacionCurso curso = new CalificacionCurso();
            Bundle bundle = new Bundle();
            bundle.putInt("clave_id_curso", viewModel.getCursoActual().getValue().getIdCurso());
            bundle.putString("clave_nombre_curso", viewModel.getCursoActual().getValue().getTitulo());
            curso.setArguments(bundle);
            cambiarFragmentoPrincipal(curso);
        });
    }
    private void mostrarOpcionesUsuario(){
        binding.btnInscribirseCurso.setVisibility(View.VISIBLE);
        binding.btnInscribirseCurso.setOnClickListener(v ->{
            mostrarConfirmacionInscripcion();
        });
    }

    private void mostrarConfirmacionInscripcion(){
        new AlertDialog.Builder(getContext()).setTitle("Confirmar inscripción curso")
                .setMessage("¿Desea inscribirse al curso?").setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        viewModel.inscribirseCurso();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void cambiarFragmento(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (fragmento){
            case 1:
                detallesCurso = new CursoDetallesInformacion();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("clave_curso", viewModel.getCursoActual().getValue());
                detallesCurso.setArguments(bundle1);
                binding.txtViewNombreBtnInformacion.setText("Listado de clases del curso");
                transaction.replace(binding.frgDetalles.getId(), detallesCurso).commit();
                fragmento = 2;
                break;

            case 2:
                if (listadoClases == null) {
                    listadoClases = new ListadoClases(this);
                    Bundle bundle = new Bundle();
                    ArrayList<ClaseDTO> clases = new ArrayList<ClaseDTO>();
                    if(viewModel.getClases().getValue().size() == 0){
                        clases = new ArrayList<>();
                    } else{
                        clases = new ArrayList<>(viewModel.getClases().getValue());
                    }
                    bundle.putParcelableArrayList("clave_clases", clases);
                    bundle.putString("clave_rol", viewModel.getCursoActual().getValue().getRol());
                    listadoClases.setArguments(bundle);
                }
                binding.txtViewNombreBtnInformacion.setText("Información del curso");
                transaction.replace(binding.frgDetalles.getId(), listadoClases).commit();
                fragmento = 1;
                break;
        }
    }

    @Override
    public void cambiarVerMas(ClaseDTO clase) {
        ClaseDetalles claseDetalles = new ClaseDetalles();
        Bundle bundle = new Bundle();
        int esEstudiante = viewModel.getCursoActual().getValue().getRol().equals("Estudiante") ? 1 : 0;
        bundle.putInt("es_estudiante",esEstudiante);
        bundle.putInt("id_clase", clase.getIdClase());

        claseDetalles.setArguments(bundle);
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(claseDetalles);
    }

    @Override
    public void cambiarFormularioClase() {
        FormularioClase formularioClase = new FormularioClase(false);
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(formularioClase);
    }

    private void cambiarFragmentoPrincipal(Fragment fragmento){
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(fragmento);
    }

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera(){
        binding.progressOverlay.setVisibility(View.GONE);
    }
}