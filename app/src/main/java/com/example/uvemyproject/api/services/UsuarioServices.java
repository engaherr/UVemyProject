package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.UsuarioBusquedaDTO;
import com.example.uvemyproject.dto.UsuarioDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioServices {
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("usuarios/{pagina}")
    Call<List<UsuarioDTO>> obtenerUsuarios(@Header("Authorization") String auth,
                                           @Path("pagina") int pagina);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("usuarios/buscar/{pagina}")
    Call<UsuarioBusquedaDTO> buscarUsuarios(@Header("Authorization") String auth,
                                            @Path("pagina") int pagina,
                                            @Query("busqueda") String busqueda);
}
