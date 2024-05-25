package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.EstadisticasCursoDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface EstadisticaServices {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("cursos/estadisticas/{idCurso}")
    Call<EstadisticasCursoDTO> obtenerEstadisticasCurso(@Path("idCurso") int idCurso, @Header("Authorization") String auth);

    @GET("cursos/reporte/{idCurso}")
    Call<ResponseBody> obtenerReporteCurso(@Path("idCurso") int idCurso, @Header("Authorization") String auth);

}
