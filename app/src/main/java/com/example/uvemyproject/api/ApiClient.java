package com.example.uvemyproject.api;

import android.content.Context;

import com.example.uvemyproject.InicioSesion;
import com.example.uvemyproject.MainActivity;
import com.example.uvemyproject.R;
import com.example.uvemyproject.api.services.AutenticacionServices;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.ComentarioServices;
import com.example.uvemyproject.api.services.CursoServices;
import com.example.uvemyproject.api.services.DocumentoServices;
import com.example.uvemyproject.api.services.EstadisticaServices;
import com.example.uvemyproject.api.services.EtiquetaServices;
import com.example.uvemyproject.api.services.ListaCursosServices;
import com.example.uvemyproject.api.services.PerfilServices;
import com.example.uvemyproject.api.services.PerfilServices;
import com.example.uvemyproject.api.services.UsuarioServices;

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
    private UsuarioServices usuarioServices;
    private ListaCursosServices listaCursos;
    private ComentarioServices comentarioServices;

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

    public ListaCursosServices getListaCursos(){
        if(listaCursos==null) {
            listaCursos = getRetrofit().create(ListaCursosServices.class);
        }
        return listaCursos;
    }

    public PerfilServices getPerfilServices(){
        if(perfilServices == null) {
            perfilServices = getRetrofit().create(PerfilServices.class);
        }
        return perfilServices;
    }

    public UsuarioServices getUsuarioServices(){
        if(usuarioServices == null) {
            usuarioServices = getRetrofit().create(UsuarioServices.class);
        }
        return usuarioServices;
    public ComentarioServices getComentarioServices(){
        if(comentarioServices == null) {
            comentarioServices = getRetrofit().create(ComentarioServices.class);
        }
        return comentarioServices;
    }
}
