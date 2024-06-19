package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentCursoDetallesInformacionBinding;
import com.example.uvemyproject.databinding.FragmentCursoDetallesPrincipalBinding;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.EtiquetaDTO;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CursoDetallesInformacion extends Fragment {
    private FragmentCursoDetallesInformacionBinding binding;
    public CursoDetallesInformacion() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCursoDetallesInformacionBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            CursoDTO curso = getArguments().getParcelable("clave_curso");
            binding.txtViewProfesor.setText(curso.getProfesor());
            String calificacion = curso.getCalificacion();
            if (calificacion == null || calificacion.isEmpty()) {
                binding.txtViewCalificacion.setText("S/C");
            } else {
                binding.txtViewCalificacion.setText(calificacion);
            }

            List<EtiquetaDTO> etiquetas = curso.getEtiquetas();
            if (etiquetas != null || etiquetas.size()>0) {
                for (EtiquetaDTO etiqueta : etiquetas) {
                    binding.txtViewEtiquetas.setText(etiqueta.getNombre()+" ");
                }
            } else {
                binding.txtViewEtiquetas.setText("Sin etiquetas");
            }

            binding.txtViewDescripcion.setText(curso.getDescripcion());
            binding.txtViewObjetivos.setText(curso.getObjetivos());
            binding.txtViewRequisitos.setText(curso.getRequisitos());
            //obtenerIdCurso(_curso.getIdCurso());
            //observarCurso();
            //observarStatus();
        }
        return binding.getRoot();
    }
}