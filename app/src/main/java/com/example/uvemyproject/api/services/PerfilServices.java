package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.CredencialesDTO;
import com.example.uvemyproject.dto.JwtDTO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PerfilServices {
    @POST("perfil/verificacion")
    Call<JwtDTO> solicitarCodigoVerificacion(@Body RequestBody correoElectronico);
}
