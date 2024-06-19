package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.UsuarioServices;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultaUsuariosViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<List<UsuarioDTO>> usuarios = new MutableLiveData<>();


    private int paginaActual = 1;
    private boolean hayMasPaginas = true;


    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public MutableLiveData<List<UsuarioDTO>> getUsuarios() {
        return usuarios;
    }

    private void obtenerUsuarios(int pagina) {
        UsuarioServices service = ApiClient.getInstance().getUsuarioServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.obtenerUsuarios(auth, pagina).enqueue(new Callback<List<UsuarioDTO>>() {
            @Override
            public void onResponse(Call<List<UsuarioDTO>> call, Response<List<UsuarioDTO>> response) {
                if (response.isSuccessful()) {
                    List<UsuarioDTO> listaUsuarios = response.body();
                    usuarios.postValue(listaUsuarios);
                    hayMasPaginas = listaUsuarios != null && listaUsuarios.size() == 6;
                    status.setValue(StatusRequest.DONE);
                } else {
                    Log.e("RetrofitError", "Error al obtener usuarios");
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioDTO>> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void cargarPrimeraPagina() {
        paginaActual = 1;
        obtenerUsuarios(paginaActual);
    }

    public void cargarPaginaAnterior() {
        if (puedeCargarPaginaAnterior()) {
            paginaActual--;
            obtenerUsuarios(paginaActual);
        }
    }

    public void cargarPaginaSiguiente() {
        if (puedeCargarPaginaSiguiente()) {
            paginaActual++;
            obtenerUsuarios(paginaActual);
        }
    }

    public boolean puedeCargarPaginaAnterior() {
        return paginaActual > 1;
    }

    public boolean puedeCargarPaginaSiguiente() {
        return hayMasPaginas;
    }
}
