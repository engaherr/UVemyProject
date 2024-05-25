package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.interfaces.INotificacionEnvioVideo;
import com.example.uvemyproject.servicio.VideoGrpc;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioClaseViewModel extends ViewModel implements INotificacionEnvioVideo {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<ClaseDTO> claseNueva = new MutableLiveData<>();
    private MutableLiveData<List<DocumentoDTO>> documentosClase = new MutableLiveData<>();
    private MutableLiveData<DocumentoDTO> videoClase = new MutableLiveData<>();

    public LiveData<StatusRequest> getStatus(){
        return status;
    }
    public LiveData<ClaseDTO> getClaseNueva (){
        return claseNueva;
    }
    public LiveData<List<DocumentoDTO>> getDocumentosClase (){
        return documentosClase;
    }
    public LiveData<DocumentoDTO> getVideoClase (){
        return videoClase;
    }


    public void guardarClaseNueva(ClaseDTO clase){
        ApiClient.Services service = ApiClient.getInstance().getService();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        service.guardarClase(auth, clase).enqueue(new Callback<ClaseDTO>() {
            @Override
            public void onResponse(Call<ClaseDTO> call, Response<ClaseDTO> response) {
                if (response.isSuccessful()) {
                    claseNueva.setValue(response.body());
                    guardarDocumentosClase(claseNueva.getValue().getIdClase());

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

    private void guardarDocumentosClase(int idClase){
        String auth = "Bearer " + SingletonUsuario.getJwt();
        ApiClient.Services service = ApiClient.getInstance().getService();

        List<DocumentoDTO> listaDocumentos = documentosClase.getValue();
        final boolean[] haHabidoError = {false};
        for(int i = 0; i < listaDocumentos.size(); i++){
            DocumentoDTO documento = listaDocumentos.get(i);
            if(!haHabidoError[0]){
                RequestBody idClaseBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idClase));
                RequestBody nombreBody = RequestBody.create(MediaType.parse("text/plain"), documento.getNombre());

                RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), documento.getFile());
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", documento.getNombre(), requestFile);

                int finalI = i;
                service.guardarDocumento(auth, idClaseBody, nombreBody, body).enqueue(new Callback<DocumentoDTO>() {
                    @Override
                    public void onResponse(Call<DocumentoDTO> call, Response<DocumentoDTO> response) {
                        if(!response.isSuccessful()){
                            status.setValue(StatusRequest.ERROR_CONEXION);
                            haHabidoError[0] = true;
                        }else{
                            if((listaDocumentos.size() - 1) == finalI){
                                guardarVideo(idClase);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DocumentoDTO> call, Throwable t) {
                        Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                        status.setValue(StatusRequest.ERROR_CONEXION);
                        haHabidoError[0] = true;
                    }
                });
            }else{
                Log.e("RetrofitErrorDocumentos", "No se pudieron mandar los documentos");
                break;
            }
        }
    }

    private void guardarVideo(int idClase){
        VideoGrpc.enviarVideo(videoClase.getValue(), idClase, this);
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
}
