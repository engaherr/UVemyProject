package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.DocumentoDTO;

import org.json.JSONStringer;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ClaseServices {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("clases")
    Call<ClaseDTO> guardarClase(@Header("Authorization") String auth, @Body RequestBody claseNueva);

    @GET("clases/{idClase}")
    Call<ClaseDTO> obtenerClase(@Header("Authorization") String auth,
                                @Path("idClase") int idClase);

}
