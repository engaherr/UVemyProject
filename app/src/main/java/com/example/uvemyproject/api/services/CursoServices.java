package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.UsuarioCursoDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CursoServices {
    @GET("cursos/calificacion/{idCurso}")
    Call<UsuarioCursoDTO> obtenerCalificacion(@Header("Authorization") String auth,
                                              @Path("idCurso") int idCurso);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("cursos/inscripcion/{idCurso}")
    Call<UsuarioCursoDTO> inscribirseCurso(@Header("Authorization") String auth,
                                           @Path("idCurso") int idCurso,
                                           @Body RequestBody usuarioCurso);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("cursos/calificacion/{idCurso}")
    Call<Void> calificarCurso(@Header("Authorization") String auth,
                              @Path("idCurso") int idCurso,
                              @Body RequestBody usuarioCurso);

    @Multipart
    @POST("cursos")
    Call<CrearCursoDTO> crearCurso(@Header("Authorization") String auth,
                                   @Part("titulo") RequestBody titulo,
                                   @Part("descripcion") RequestBody descripcion,
                                   @Part("objetivos") RequestBody objetivos,
                                   @Part("requisitos") RequestBody requisitos,
                                   @Part("etiquetas") List<RequestBody> etiquetas,
                                   @Part MultipartBody.Part file);

    @Multipart
    @PUT("cursos/{idCurso}")
    Call<CrearCursoDTO> modificarCurso(@Header("Authorization") String auth,
                                       @Path("idCurso") int idCursoPath,
                                       @Part("idCurso") RequestBody idCurso,
                                       @Part("idDocumento") RequestBody idDocumento,
                                       @Part("titulo") RequestBody titulo,
                                       @Part("descripcion") RequestBody descripcion,
                                       @Part("objetivos") RequestBody objetivos,
                                       @Part("requisitos") RequestBody requisitos,
                                       @Part("etiquetas") List<RequestBody> etiquetas,
                                       @Part MultipartBody.Part file);

    @DELETE("cursos/{idCurso}")
    Call<CrearCursoDTO> eliminarCurso(@Header("Authorization") String auth,
                                      @Path("idCurso") int idCursoPath);

    @GET("cursos/{idCurso}")
    Call<CursoDTO> recuperarCurso(@Header("Authorization") String auth,
                                              @Path("idCurso") int idCurso);
}
