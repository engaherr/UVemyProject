package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.ComentarioDTO;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ComentarioServices {
    @GET("comentarios/{idClase}")
    Call<List<ComentarioDTO>> obtenerComentariosClase(@Header("Authorization") String auth,
                                                      @Path("idClase") int idClase);
    @POST("comentarios")
    Call<Void> crearComentario(@Header("Authorization") String auth,
                               @Body RequestBody comentario);
}
