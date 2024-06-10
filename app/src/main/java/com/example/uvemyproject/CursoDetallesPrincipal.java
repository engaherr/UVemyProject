package com.example.uvemyproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.interfaces.INotificacionFragmentoClase;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.viewmodels.CursoDetallesPrincipalViewModel;
import com.example.uvemyproject.viewmodels.FormularioClaseViewModel;

import java.util.ArrayList;

public class CursoDetallesPrincipal extends Fragment implements INotificacionFragmentoClase {

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
        viewModel = new ViewModelProvider(this).get(CursoDetallesPrincipalViewModel.class);

        observarCurso();
        observarStatus();
        obtenerIdCurso();

        return binding.getRoot();
    }
    private void observarCurso(){
        viewModel.getCursoActual().observe(getViewLifecycleOwner(), curso ->{
            if(curso != null){
                mostrarOpcionesSegunRol(curso.getRol());
                if(curso.getClases() != null){
                    listadoClases = new ListadoClases(this);
                    Bundle bundle = new Bundle();
                    ArrayList<ClaseDTO> clases = new ArrayList<>(curso.getClases());
                    bundle.putParcelableArrayList("clave_clases", clases);
                    listadoClases.setArguments(bundle);
                }

                detallesCurso = new CursoDetallesInformacion();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(binding.frgDetalles.getId(), detallesCurso).commit();
                fragmento = 2;

                binding.btnCambiarFragmento.setOnClickListener(v -> cambiarFragmento());
            }
            quitarEspera();
        });
    }

    private void observarStatus(){
        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void obtenerIdCurso(){
        //Pasar como bundle del fragmento previo
        /*Bundle args = getArguments();
        if (args != null) {
            int idClase = args.getInt("id_curso");
            viewModel.obtenerCurso(idClase);
            ponerEspera();
        }*/
        viewModel.obtenerCurso(1);
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
            bundle.putString("clave_nombre_curso", viewModel.getCursoActual().getValue().getNombre());
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
                binding.txtViewNombreBtnInformacion.setText("Listado de clases del curso");
                transaction.replace(binding.frgDetalles.getId(), detallesCurso).commit();
                fragmento = 2;
                break;

            case 2:
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
        bundle.putInt("id_clase", 127);
        claseDetalles.setArguments(bundle);
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(claseDetalles);
    }

    @Override
    public void cambiarFormularioClase() {
        FormularioClase formularioClase = new FormularioClase();
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