package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.DocumentoDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ClaseServices {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("clases")
    Call<ClaseDTO> guardarClase(@Header("Authorization") String auth, @Body ClaseDTO claseNueva);

    @Multipart
    @POST("documentos/clase")
    Call<DocumentoDTO> guardarDocumento(@Header("Authorization") String auth,
                                        @Part("idClase") RequestBody idClase,
                                        @Part("nombre") RequestBody nombre,
                                        @Part MultipartBody.Part file);

}
