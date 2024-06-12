package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.UsuarioDTO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AutenticacionServices {
    @POST("autenticacion")
    Call<UsuarioDTO> iniciarSesion(@Body RequestBody credenciales);
}
