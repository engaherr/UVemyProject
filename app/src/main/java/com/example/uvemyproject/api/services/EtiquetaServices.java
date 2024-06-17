package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.EtiquetaDTO;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EtiquetaServices {
    @GET("etiquetas")
    Call<List<EtiquetaDTO>> getEtiquetas();

    @POST("etiquetas")
    Call<Void> registrarEtiqueta(@Body RequestBody usuarioRegistro);

    @DELETE("etiquetas/{idEtiqueta}")
    Call<Void> eliminarEtiqueta(@Path("idEtiqueta") int idEtiqueta);
}
