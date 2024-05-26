package com.example.uvemyproject.api.services;

import com.example.uvemyproject.dto.DocumentoDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DocumentoServices {
    @GET("documentos/clase/{idDocumento}")
    Call<ResponseBody> obtenerDocumentoDeClase(@Header("Authorization") String auth,
                                               @Path("idDocumento") int idDocumento);

    @Multipart
    @POST("documentos/clase")
    Call<DocumentoDTO> guardarDocumento(@Header("Authorization") String auth,
                                        @Part("idClase") RequestBody idClase,
                                        @Part("nombre") RequestBody nombre,
                                        @Part MultipartBody.Part file);

    @DELETE("documentos/clase/{idDocumento}")
    Call<Void> eliminarDocumento(@Header("Authorization") String auth,
                                 @Path("idDocumento") int idDocumento);

}
