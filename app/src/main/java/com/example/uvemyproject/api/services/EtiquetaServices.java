package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.EtiquetaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EtiquetaServices {
    @GET("etiquetas")
    Call<List<EtiquetaDTO>> getEtiquetas();
}
