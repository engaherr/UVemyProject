package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.EtiquetaDTO;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EtiquetaServices {
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("etiquetas")
    Call<List<EtiquetaDTO>> getEtiquetas(@Header("Authorization") String auth);

    @POST("etiquetas")
    Call<Void> registrarEtiqueta(@Body RequestBody usuarioRegistro,
                                 @Header("Authorization") String auth);

    @DELETE("etiquetas/{idEtiqueta}")
    Call<Void> eliminarEtiqueta(@Path("idEtiqueta") int idEtiqueta,
                                @Header("Authorization") String auth);
}
