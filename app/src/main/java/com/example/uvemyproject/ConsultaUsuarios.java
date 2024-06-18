package com.example.uvemyproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uvemyproject.databinding.FragmentConsultaUsuariosBinding;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.viewmodels.ConsultaUsuariosViewModel;

import java.util.List;

public class ConsultaUsuarios extends Fragment {
    private FragmentConsultaUsuariosBinding binding;
    private ConsultaUsuariosViewModel viewModel;
    private UsuarioAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConsultaUsuariosBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(ConsultaUsuariosViewModel.class);

        binding.rcyViewListadoUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsuarioAdapter();
        binding.rcyViewListadoUsuarios.setAdapter(adapter);

        binding.btnPaginaAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ponerEspera();
                viewModel.cargarPaginaAnterior();
            }
        });

        binding.btnPaginaSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ponerEspera();
                viewModel.cargarPaginaSiguiente();
            }
        });

        viewModel.getUsuarios().observe(getViewLifecycleOwner(), new Observer<List<UsuarioDTO>>() {
            @Override
            public void onChanged(List<UsuarioDTO> usuarios) {
                adapter.submitList(usuarios);
                actualizarEstadoBotones();
            }
        });

        observarStatus();

        viewModel.cargarPrimeraPagina();

        return binding.getRoot();
    }

    private void observarStatus() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case ERROR:
                    Toast.makeText(getContext(), "Ocurrió un error al eliminar la(s) etiqueta(s).",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(), "No hay conexión con el servidor.",
                            Toast.LENGTH_SHORT).show();
                    break;
                case NO_CONTENT:
                    Toast.makeText(getContext(), "Etiqueta(s) eliminada(s) correctamente.",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void actualizarEstadoBotones() {
        binding.btnPaginaAnterior.setEnabled(viewModel.puedeCargarPaginaAnterior());
        binding.btnPaginaSiguiente.setEnabled(viewModel.puedeCargarPaginaSiguiente());
    }

    private void ponerEspera() {
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera() {
        binding.progressOverlay.setVisibility(View.GONE);
    }
}
