package com.example.uvemyproject.api;

import com.example.uvemyproject.api.services.AutenticacionServices;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.CursoServices;
import com.example.uvemyproject.api.services.DocumentoServices;
import com.example.uvemyproject.api.services.EstadisticaServices;
import com.example.uvemyproject.api.services.EtiquetaServices;
import com.example.uvemyproject.api.services.PerfilServices;

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
    private AutenticacionServices autenticacionServices;
    private EtiquetaServices etiquetaServices;
    private PerfilServices perfilServices;

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
                    .baseUrl("http://192.168.100.26:3000/api/").client(client)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public CursoServices getCursoServices(){
        if(cursoServices ==null) {
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

    public AutenticacionServices getAutenticacionServices(){
        if(autenticacionServices == null) {
            autenticacionServices = getRetrofit().create(AutenticacionServices.class);
        }
        return autenticacionServices;
    }

    public EtiquetaServices getEtiquetaServices(){
        if(etiquetaServices == null) {
            etiquetaServices = getRetrofit().create(EtiquetaServices.class);
        }
        return etiquetaServices;
    }

    public PerfilServices getPerfilServices(){
        if(perfilServices == null) {
            perfilServices = getRetrofit().create(PerfilServices.class);
        }
        return perfilServices;
    }
}
