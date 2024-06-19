package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.JwtDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

public interface PerfilServices {
    @POST("perfil/verificacion")
    Call<JwtDTO> solicitarCodigoVerificacion(@Body RequestBody correoElectronico);
    @POST("perfil/registro")
    Call<Void> registrarUsuario(@Header("Authorization") String auth,
                                @Body RequestBody usuarioRegistro);

    @GET("perfil/foto")
    @Streaming
    Call<ResponseBody> obtenerFotoPerfil(@Header("Authorization") String auth);
    @Multipart
    @PUT("perfil/foto")
    Call<Void> subirFotoPerfil(@Header("Authorization") String auth,
                               @Part("idUsuario") RequestBody idUsuario,
                               @Part MultipartBody.Part imagen);

    @PUT("perfil")
    Call<Void> actualizarUsuario(@Header("Authorization") String auth,
                                 @Body RequestBody usuarioActualizar);
    @PUT("perfil/usuarioetiquetas")
    Call<Void> actualizarEtiquetas(@Header("Authorization") String auth,
                                   @Body RequestBody etiquetas);
}
