package com.example.uvemyproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uvemyproject.databinding.FragmentConfirmacionCorreoBinding;
import com.example.uvemyproject.dto.UsuarioDTO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmacionCorreo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmacionCorreo extends Fragment {

    FragmentConfirmacionCorreoBinding binding;

    UsuarioDTO usuario;

    public ConfirmacionCorreo() {
    }

    public ConfirmacionCorreo(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public static ConfirmacionCorreo newInstance(String param1, String param2) {
        ConfirmacionCorreo fragment = new ConfirmacionCorreo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfirmacionCorreoBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}