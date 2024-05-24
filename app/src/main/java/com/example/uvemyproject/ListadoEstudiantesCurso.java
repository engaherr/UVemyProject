package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentEstadisticasCursoBinding;
import com.example.uvemyproject.databinding.FragmentListadoEstudiantesCursoBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListadoEstudiantesCurso#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListadoEstudiantesCurso extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<String> estudiantes;

    public ListadoEstudiantesCurso(List<String> estudiantes) {
        this.estudiantes = estudiantes;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListadoEstudiantesCurso.
     */
    // TODO: Rename and change types and number of parameters
    public static ListadoEstudiantesCurso newInstance(String param1, String param2) {
        ListadoEstudiantesCurso fragment = new ListadoEstudiantesCurso(new ArrayList<>());
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
    private FragmentListadoEstudiantesCursoBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListadoEstudiantesCursoBinding.inflate(inflater, container, false);

        EstudianteAdapter adapter = new EstudianteAdapter();
        binding.rcyViewListadoEstudiantes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewListadoEstudiantes.setAdapter(adapter);
        adapter.submitList(estudiantes);

        return binding.getRoot();
    }
}