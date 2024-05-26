package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.UsuarioCursoDTO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CursoServices {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("cursos/inscripcion/{idCurso}")
    Call<UsuarioCursoDTO> inscribirseCurso(@Header("Authorization") String auth,
                                           @Path("idCurso") int idCurso,
                                           @Body RequestBody usuarioCurso);
}
