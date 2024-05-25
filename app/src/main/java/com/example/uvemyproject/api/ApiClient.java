package com.example.uvemyproject.api;

import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.DocumentoServices;
import com.example.uvemyproject.api.services.EstadisticaServices;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private ClaseServices claseServices;
    private EstadisticaServices estadisticaServices;
    private DocumentoServices documentoServices;
    private static final ApiClient apiClient = new ApiClient();
    public static ApiClient getInstance(){
        return apiClient;
    }
    private ApiClient(){
    }
    private static Retrofit getRetrofit(){
        if(retrofit == null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.100.23:3000/api/").client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public ClaseServices getClaseServices(){
        if(claseServices ==null) {
            claseServices = getRetrofit().create(ClaseServices.class);
        }
        return claseServices;
    }

    public EstadisticaServices getEstadisticaServices(){
        if(estadisticaServices==null) {
            estadisticaServices = getRetrofit().create(EstadisticaServices.class);
        }
        return estadisticaServices;
    }

    public DocumentoServices getDocumentoServices(){
        if(documentoServices==null) {
            documentoServices = getRetrofit().create(DocumentoServices.class);
        }
        return documentoServices;
    }

}
