package com.example.uvemyproject.api;

import com.example.uvemyproject.dto.EstadisticasCursoDTO;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public class ApiClient {

    public interface Services{
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        @GET("cursos/estadisticas/{idCurso}")
        Call<EstadisticasCursoDTO> obtenerEstadisticasCurso(@Path("idCurso") int idCurso, @Header("Authorization") String auth);
    }

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build();
    private Services service;
    private static final ApiClient apiClient = new ApiClient();
    public static ApiClient getInstance(){
        return apiClient;
    }
    private ApiClient(){
    }
    public Services getService(){
        if(service==null) {
            service = retrofit.create(Services.class);
        }
        return service;
    }

}
