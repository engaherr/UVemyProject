package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.UsuarioServices;
import com.example.uvemyproject.dto.UsuarioBusquedaDTO;
import com.example.uvemyproject.dto.UsuarioDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusquedaUsuariosViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<List<UsuarioDTO>> usuariosBusqueda = new MutableLiveData<>();

    private int paginaActual = 1;
    private boolean hayMasPaginas = true;
    private String terminoBusqueda = "";

    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public MutableLiveData<List<UsuarioDTO>> getUsuarios() {
        return usuariosBusqueda;
    }

    public void buscarUsuarios(int pagina, String busqueda) {
        UsuarioServices service = ApiClient.getInstance().getUsuarioServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.buscarUsuarios(auth, pagina, busqueda).enqueue(new Callback<UsuarioBusquedaDTO>() {
            @Override
            public void onResponse(Call<UsuarioBusquedaDTO> call, Response<UsuarioBusquedaDTO> response) {
                if (response.isSuccessful()) {
                    UsuarioBusquedaDTO busquedaResponse = response.body();
                    List<UsuarioDTO> listaUsuarios = busquedaResponse != null ? busquedaResponse.getData() : null;
                    usuariosBusqueda.postValue(listaUsuarios);
                    hayMasPaginas = busquedaResponse != null && busquedaResponse.getTotalPages() > pagina;
                    status.setValue(StatusRequest.DONE);
                } else {
                    Log.e("RetrofitError", "Error al buscar usuarios");
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<UsuarioBusquedaDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }


    public void cargarPrimeraPagina() {
        paginaActual = 1;
        buscarUsuarios(paginaActual, terminoBusqueda);
    }

    public void cargarPaginaAnterior() {
        if (puedeCargarPaginaAnterior()) {
            paginaActual--;
            buscarUsuarios(paginaActual, terminoBusqueda);
        }
    }

    public void cargarPaginaSiguiente() {
        if (puedeCargarPaginaSiguiente()) {
            paginaActual++;
            buscarUsuarios(paginaActual, terminoBusqueda);
        }
    }

    public boolean puedeCargarPaginaAnterior() {
        return paginaActual > 1;
    }

    public boolean puedeCargarPaginaSiguiente() {
        return hayMasPaginas;
    }

    public void setTerminoBusqueda(String busqueda) {
        terminoBusqueda = busqueda;
        cargarPrimeraPagina();
    }
}
