package com.example.uvemyproject.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.utils.StatusRequest;

public class FormularioUsuarioViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();

    private MutableLiveData<UsuarioDTO> usuario = new MutableLiveData<>();

    private boolean esActualizacionUsuario;

    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public MutableLiveData<UsuarioDTO> getUsuario() {
        return usuario;
    }

    public void setUsuario(MutableLiveData<UsuarioDTO> usuario) {
        this.usuario = usuario;
    }

    public boolean isEsActualizacionUsuario() {
        return esActualizacionUsuario;
    }

    public void setEsActualizacionUsuario(boolean esActualizacionUsuario) {
        this.esActualizacionUsuario = esActualizacionUsuario;
    }
}
