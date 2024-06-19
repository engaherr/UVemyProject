package com.example.uvemyproject;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uvemyproject.databinding.FragmentBusquedaUsuariosBinding;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.viewmodels.BusquedaUsuariosViewModel;

import java.util.List;

public class BusquedaUsuarios extends Fragment {
    private FragmentBusquedaUsuariosBinding binding;
    private BusquedaUsuariosViewModel viewModel;
    private UsuarioAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBusquedaUsuariosBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(BusquedaUsuariosViewModel.class);

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

        binding.btnBuscarUsuario.setOnClickListener(v -> {
            String busqueda = binding.edtTextBusqueda.getText().toString().trim();
            if (!busqueda.isEmpty()) {
                if (busqueda.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                    ponerEspera();
                    viewModel.setTerminoBusqueda(busqueda);
                } else {
                    Toast.makeText(getContext(), "Solo se permiten letras.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Ingrese un término de búsqueda.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        quitarEspera();
        observarStatus();
        obtenerUsuarios();


        return binding.getRoot();
    }

    private void observarStatus() {
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case ERROR:
                    Toast.makeText(getContext(), "Ocurrió un error al obtener los datos.", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(), "No hay conexión con el servidor.", Toast.LENGTH_SHORT).show();
                    break;
                case NO_CONTENT:
                    Toast.makeText(getContext(), "No se encontraron usuarios.", Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void obtenerUsuarios() {
        viewModel.getUsuarios().observe(getViewLifecycleOwner(), new Observer<List<UsuarioDTO>>() {
            @Override
            public void onChanged(List<UsuarioDTO> usuarios) {
                adapter.submitList(usuarios);
                actualizarEstadoBotones();
                quitarEspera();
            }
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
