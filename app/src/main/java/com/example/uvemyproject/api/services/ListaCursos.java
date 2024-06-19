package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.ListaCursoDTO;
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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ListaCursos {


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("cursoslistas/{pagina}")
    Call<List<ListaCursoDTO>> recuperarCursosLista(@Header("Authorization") String auth,
                                                   @Path("pagina") int pagina);

}
