package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.ListaCursoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ListaCursosServices {


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("cursoslistas/{pagina}")
    Call<List<ListaCursoDTO>> recuperarCursosLista(@Header("Authorization") String auth,
                                                   @Path("pagina") int pagina);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("cursoslistas/{pagina}")
    Call<List<ListaCursoDTO>> recuperarCursosListaPorTitulo(@Header("Authorization") String auth,
                                                   @Path("pagina") int pagina,
                                                   @Query("titulo") String titulo);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("cursoslistas/{pagina}")
    Call<List<ListaCursoDTO>> recuperarCursosListaPorCalificacion(@Header("Authorization") String auth,
                                                   @Path("pagina") int pagina,
                                                   @Query("calificacion") int calficacion);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("cursoslistas/{pagina}")
    Call<List<ListaCursoDTO>> recuperarCursosListaPorTipoCursos(@Header("Authorization") String auth,
                                                   @Path("pagina") int pagina,
                                                   @Query("tipoCursos") int tipoCursos);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("cursoslistas/{pagina}")
    Call<List<ListaCursoDTO>> recuperarCursosListaPorEtiquetas(@Header("Authorization") String auth,
                                                                @Path("pagina") int pagina,
                                                                @Query("etiqueta") int tipoCursos);

}
