package com.example.uvemyproject.api;

import android.content.Context;

import com.example.uvemyproject.InicioSesion;
import com.example.uvemyproject.MainActivity;
import com.example.uvemyproject.R;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.CursoServices;
import com.example.uvemyproject.api.services.DocumentoServices;
import com.example.uvemyproject.api.services.EstadisticaServices;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private CursoServices cursoServices;
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
            Context context = InicioSesion.obtenerContexto();
            String url_host = context.getString(R.string.url_host);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(url_host).client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public CursoServices getCursoServices(){
        if(cursoServices == null) {
            cursoServices = getRetrofit().create(CursoServices.class);
        }
        return cursoServices;
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
