package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.DocumentoServices;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.interfaces.INotificacionEnvioVideo;
import com.example.uvemyproject.servicio.VideoGrpc;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioClaseViewModel extends ViewModel implements INotificacionEnvioVideo {

    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<ClaseDTO> claseActual = new MutableLiveData<>();
    private MutableLiveData<List<DocumentoDTO>> documentosClase = new MutableLiveData<>();
    private MutableLiveData<DocumentoDTO> videoClase = new MutableLiveData<>();

    public LiveData<StatusRequest> getStatus(){
        return status;
    }
    public LiveData<ClaseDTO> getClaseActual(){
        return claseActual;
    }

    public LiveData<List<DocumentoDTO>> getDocumentosClase (){
        return documentosClase;
    }
    public LiveData<DocumentoDTO> getVideoClase (){
        return videoClase;
    }
    public void setClaseActual(ClaseDTO claseActual){
        this.claseActual.setValue(claseActual);
        if(claseActual.getDocumentos() != null){
            documentosClase.setValue(claseActual.getDocumentos());
        }
        if(claseActual.getVideoDocumento() != null){
            videoClase.setValue(claseActual.getVideoDocumento());
        }
    }

    public void guardarClaseNueva(ClaseDTO clase){
        ClaseServices service = ApiClient.getInstance().getClaseServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        service.guardarClase(auth, convertirRequestBody(clase, "idClase")).enqueue(new Callback<ClaseDTO>() {
            @Override
            public void onResponse(Call<ClaseDTO> call, Response<ClaseDTO> response) {
                if (response.isSuccessful()) {
                    guardarDocumentosClase(response.body().getIdClase());
                }else{
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ClaseDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });

    }

    private RequestBody convertirRequestBody(ClaseDTO clase, String campoEliminar){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ClaseDTO> jsonAdapter = moshi.adapter(ClaseDTO.class);

        String jsonEnviado = "";
        try {
            String json = jsonAdapter.toJson(clase);
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.remove("documentosId");
            jsonObject.remove("videoId");
            jsonObject.remove(campoEliminar);

            jsonEnviado = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonEnviado);
    }

    private void guardarDocumentosClase(int idClase){
        String auth = "Bearer " + SingletonUsuario.getJwt();
        List<DocumentoDTO> listaDocumentos = documentosClase.getValue();
        AtomicBoolean haHabidoError = new AtomicBoolean(false);

        for(int i = 0; i < listaDocumentos.size(); i++){
            if (!haHabidoError.get()) {
                DocumentoDTO documento = listaDocumentos.get(i);
                documento.setIdClase(idClase);
                guardarDocumento(auth, documento, i, listaDocumentos.size(), haHabidoError, false);
            } else {
                Log.e("RetrofitErrorDocumentos", "No se pudieron mandar los documentos");
                break;
            }
        }
    }
    private void guardarDocumento(String auth, DocumentoDTO documento, int index, int fin, AtomicBoolean haHabidoError, boolean esActualizar){
        if(!haHabidoError.get()){
            DocumentoServices service = ApiClient.getInstance().getDocumentoServices();

            RequestBody idClaseBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(documento.getIdClase()));
            RequestBody nombreBody = RequestBody.create(MediaType.parse("text/plain"), documento.getNombre());

            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), documento.getFile());
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", documento.getNombre() + ".pdf", requestFile);

            service.guardarDocumento(auth, idClaseBody, nombreBody, body).enqueue(new Callback<DocumentoDTO>() {
                @Override
                public void onResponse(Call<DocumentoDTO> call, Response<DocumentoDTO> response) {
                    if(!response.isSuccessful()){
                        status.setValue(StatusRequest.ERROR_CONEXION);
                        haHabidoError.set(true);
                    }else{
                        if((fin - 1) == index){
                            guardarVideo(documento.getIdClase(), esActualizar);
                        }
                    }
                }
                @Override
                public void onFailure(Call<DocumentoDTO> call, Throwable t) {
                    Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                    status.setValue(StatusRequest.ERROR_CONEXION);
                    haHabidoError.set(true);
                }
            });
        }
    }

    private void guardarVideo(int idClase, boolean esActualizar){
        DocumentoDTO video = videoClase.getValue();
        if(esActualizar){
            video.setIdDocumento(claseActual.getValue().getVideoId());
        }
        VideoGrpc.enviarVideo(video, idClase, this);
    }

    public void actualizarClase(ClaseDTO claseActualizada){
        ClaseServices service = ApiClient.getInstance().getClaseServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        service.actualizarClase(auth, claseActualizada.getIdClase(), convertirRequestBody(claseActualizada, "idCurso")).enqueue(new Callback<ClaseDTO>() {
            @Override
            public void onResponse(Call<ClaseDTO> call, Response<ClaseDTO> response) {
                if (response.isSuccessful()) {
                    actualizarDocumentosClase(claseActualizada);
                }else{
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ClaseDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }
    
    private void actualizarDocumentosClase(ClaseDTO claseActualizada){
        ArrayList<Integer> documentosPorEliminar = obtenerDocumentosPorEliminar();
        IDocumentosEliminados notificarFinDocumentosEliminados = new IDocumentosEliminados() {
            @Override
            public void documentosEliminados(boolean haHabidoError) {
                if(!haHabidoError){
                    actualizarDocumentosNuevos(claseActualizada.getIdClase());
                }
            }
        };

        if(documentosPorEliminar.size() != 0){
            eliminarDocumentosRestantes(documentosPorEliminar, notificarFinDocumentosEliminados);
        }else{
            actualizarDocumentosNuevos(claseActualizada.getIdClase());
        }
    }

    private void actualizarDocumentosNuevos(int idClase){
        String auth = "Bearer " + SingletonUsuario.getJwt();
        List<DocumentoDTO> listaDocumentos = documentosClase.getValue();
        AtomicBoolean haHabidoError = new AtomicBoolean(false);

        int documentosAgregados = 0;

        for(int i = 0; i < listaDocumentos.size(); i++){
            if (!haHabidoError.get()) {
                DocumentoDTO documento = listaDocumentos.get(i);
                if(documento.getIdDocumento() == 0){
                    documento.setIdClase(idClase);
                    guardarDocumento(auth, documento, i, listaDocumentos.size(), haHabidoError, true);
                    documentosAgregados ++;
                }
            } else {
                Log.e("RetrofitErrorDocumentos", "No se pudieron mandar los documentos");
                break;
            }
        }

        if(documentosAgregados == 0){
            guardarVideo(idClase, true);
        }
    }

    private ArrayList<Integer> obtenerDocumentosPorEliminar(){
        List<DocumentoDTO> documentosActuales = documentosClase.getValue();
        List<Integer> documentosPrevios = Arrays.stream(claseActual.getValue().getDocumentosId()).boxed().collect(Collectors.toList());

        ArrayList<Integer> documentosPorEliminar = new ArrayList<>();

        for(int i = 0; i < documentosPrevios.size(); i++) {
            int idDocumento = documentosPrevios.get(i);
            //El documento previo existe en los documentos nuevos
            int posicion = obtenerPosicionDocumento(idDocumento, documentosActuales);
            if(posicion == -1){
                documentosPorEliminar.add(idDocumento);
            }
        }

        return documentosPorEliminar;

    }

    private int obtenerPosicionDocumento(int idDocumento, List<DocumentoDTO> listaDocumento){
        for (int i = 0; i < listaDocumento.size(); i++) {
            if(listaDocumento.get(i).getIdDocumento() == idDocumento){
                return i;
            }
        }
        return -1;
    }

    private void eliminarDocumentosRestantes(List<Integer> documentosPorEliminar, IDocumentosEliminados notificarFinEliminacion){
        String auth = "Bearer " + SingletonUsuario.getJwt();
        DocumentoServices service = ApiClient.getInstance().getDocumentoServices();
        AtomicBoolean haHabidoError = new AtomicBoolean(false);

        for (int i = 0; i < documentosPorEliminar.size(); i++) {
            if(!haHabidoError.get()){
                int finalI = i;
                service.eliminarDocumento(auth, documentosPorEliminar.get(i)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(!response.isSuccessful()){
                            status.setValue(StatusRequest.ERROR);
                            haHabidoError.set(true);
                        }else{
                            if(!haHabidoError.get() && ((documentosPorEliminar.size() - 1) == finalI)){
                                notificarFinEliminacion.documentosEliminados(haHabidoError.get());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                        status.setValue(StatusRequest.ERROR_CONEXION);
                        haHabidoError.set(true);
                    }
                });
            }else{
                break;
            }
        }

    }


    public void eliminarClase(){
        String auth = "Bearer " + SingletonUsuario.getJwt();
        ClaseServices services = ApiClient.getInstance().getClaseServices();

        services.eliminarClase(auth, claseActual.getValue().getIdClase()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                   claseActual.setValue(null);
                }else{
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });

    }

    public void agregarDocumento(File file){
        String fileName = obtenerNombreSinExtension(file.getName());
        DocumentoDTO documento = new DocumentoDTO();
        documento.setFile(file);
        documento.setNombre(fileName);
        List<DocumentoDTO> listaActual = documentosClase.getValue();

        if (listaActual == null) {
            listaActual = new ArrayList<>();
        }
        listaActual.add(documento);

        documentosClase.setValue(listaActual);
    }

    public void eliminarDocumento(int posicionDocumento){
        List<DocumentoDTO> listaActual = documentosClase.getValue();
        listaActual.remove(posicionDocumento);

        documentosClase.setValue(listaActual);
    }

    public void agregarVideo(File video){
        String fileName = obtenerNombreSinExtension(video.getName());
        DocumentoDTO documento = new DocumentoDTO();
        documento.setFile(video);
        documento.setNombre(fileName);
        videoClase.setValue(documento);
    }

    public void eliminarVideo(){
        videoClase.setValue(null);
    }
    private String obtenerNombreSinExtension(String fileName){
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }

    @Override
    public void envioExitosoVideo() {
        status.postValue(StatusRequest.DONE);
    }

    @Override
    public void envioErroneoVideo() {
        status.postValue(StatusRequest.ERROR);
    }

    private interface IDocumentosEliminados{
        void documentosEliminados(boolean haHabidoError);
    }
}
