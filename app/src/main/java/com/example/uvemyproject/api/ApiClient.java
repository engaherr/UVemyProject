package com.example.uvemyproject.api;

import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.dto.EstadisticasCursoDTO;
import com.squareup.moshi.Moshi;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {

    public interface Services{
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        @GET("cursos/estadisticas/{idCurso}")
        Call<EstadisticasCursoDTO> obtenerEstadisticasCurso(@Path("idCurso") int idCurso, @Header("Authorization") String auth);

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

    //Eliminar logginginterceptor
    /*private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.100.23:3000/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build();*/
    private Services service;
    private static final ApiClient apiClient = new ApiClient();
    public static ApiClient getInstance(){
        return apiClient;
    }
    private ApiClient(){
    }
    public Services getService(){
        if(service==null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.100.23:3000/api/").client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();
            service = retrofit.create(Services.class);
        }
        return service;
    }

}
