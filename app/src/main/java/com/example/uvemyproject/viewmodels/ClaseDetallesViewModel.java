package com.example.uvemyproject.viewmodels;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.InicioSesion;
import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.ComentarioServices;
import com.example.uvemyproject.api.services.DocumentoServices;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.ComentarioDTO;
import com.example.uvemyproject.dto.ComentarioEnvioDTO;
import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.interfaces.INotificacionReciboVideo;
import com.example.uvemyproject.servicio.VideoGrpc;
import com.example.uvemyproject.utils.FileUtil;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaseDetallesViewModel extends ViewModel implements INotificacionReciboVideo {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<ClaseDTO> claseActual = new MutableLiveData<>();
    private final MutableLiveData<CursoDTO> cursoActual = new MutableLiveData<>();
    private final MutableLiveData<List<ComentarioDTO>> comentarios = new MutableLiveData<>();
    private final MutableLiveData<StatusRequest> statusEnviarComentario = new MutableLiveData<>();

    public LiveData<StatusRequest> getStatus(){
        return status;
    }
    public LiveData<CursoDTO> getCurso(){
        return cursoActual;
    }
    public void setCurso (CursoDTO cursoNuevo){
        cursoActual.setValue(cursoNuevo);
    }
    public LiveData<ClaseDTO> getClaseActual() { return claseActual; }
    public LiveData<List<ComentarioDTO>> getComentarios() { return comentarios; }
    public LiveData<StatusRequest> getStatusEnviarComentario() { return statusEnviarComentario; }
    public void recuperarDetallesClase(int idClase){
        ClaseServices service = ApiClient.getInstance().getClaseServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.obtenerClase(auth, idClase).enqueue(new Callback<ClaseDTO>() {
            @Override
            public void onResponse(Call<ClaseDTO> call, Response<ClaseDTO> response) {
                if(response.isSuccessful()){
                    claseActual.setValue(response.body());
                    obtenerDocumentosClase();
                    obtenerComentariosClase();
                }else{
                    status.setValue(StatusRequest.ERROR);
                }
            }
            @Override
            public void onFailure(Call<ClaseDTO> call, Throwable t) {
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private void obtenerComentariosClase() {
        String auth = "Bearer " + SingletonUsuario.getJwt();
        ComentarioServices service = ApiClient.getInstance().getComentarioServices();

        service.obtenerComentariosClase(auth, claseActual.getValue().getIdClase())
                .enqueue(new Callback<List<ComentarioDTO>>() {
            @Override
            public void onResponse(Call<List<ComentarioDTO>> call, Response<List<ComentarioDTO>> response) {
                if (response.isSuccessful()) {
                    comentarios.setValue(response.body());
                    status.setValue(StatusRequest.DONE);
                } else {
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<ComentarioDTO>> call, Throwable t) {
                Log.e("RetrofitErrorComentarios", t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void enviarComentario(ComentarioEnvioDTO comentario) {
        String auth = "Bearer " + SingletonUsuario.getJwt();
        ComentarioServices service = ApiClient.getInstance().getComentarioServices();

        String campoEliminar = comentario.getRespondeAComentario() == 0 ? "respondeAComentario" : null;
        service.crearComentario(auth, convertirResponseBody(comentario, campoEliminar))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            statusEnviarComentario.setValue(StatusRequest.DONE);
                            obtenerComentariosClase();
                        } else {
                            statusEnviarComentario.setValue(StatusRequest.ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("RetrofitError", t.getMessage(), t);
                        statusEnviarComentario.setValue(StatusRequest.ERROR_CONEXION);
                    }
                });
    }

    private RequestBody convertirResponseBody(ComentarioEnvioDTO comentario, String campoEliminar) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ComentarioEnvioDTO> jsonAdapter = moshi.adapter(ComentarioEnvioDTO.class);

        String jsonEnviado = "";
        try {
            String json = jsonAdapter.toJson(comentario);
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.remove(campoEliminar);

            jsonEnviado = jsonObject.toString();
        } catch (JSONException e) {
            Log.e("RetrofitError", "Error al convertir el comentario a JSON", e);
        }

        return RequestBody.create(jsonEnviado,
                MediaType.parse("application/json; charset=utf-8"));
    }

    private void obtenerDocumentosClase(){
        String auth = "Bearer " + SingletonUsuario.getJwt();
        DocumentoServices service = ApiClient.getInstance().getDocumentoServices();
        ArrayList<DocumentoDTO> documentosRecuperados = new ArrayList<>();

        final boolean[] haHabidoError = {false};

        int[] idDocumentos = claseActual.getValue().getDocumentosId();

        for (int i = 0; i < idDocumentos.length; i++) {
            if(!haHabidoError[0]){
                int finalI = i;
                service.obtenerDocumentoDeClase(auth, idDocumentos[i]).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String contentDisposition = response.headers().get("Content-Disposition");
                            DocumentoDTO documento = new DocumentoDTO();
                            String nombre = "Documento";
                            if (contentDisposition != null && contentDisposition.contains("filename=")) {
                                nombre = contentDisposition.split("filename=")[1].replace("\"", "");
                            }
                            documento.setNombre(FileUtil.eliminarExtensionNombre(nombre));
                            documento.setIdDocumento(idDocumentos[finalI]);

                            try {
                                documento.setDocumento(response.body().bytes());
                            } catch (IOException e) {
                                documento.setDocumento(null);
                                haHabidoError[0] = true;
                            }

                            documentosRecuperados.add(documento);

                            if((idDocumentos.length - 1) == finalI){
                                ClaseDTO clase = claseActual.getValue();
                                clase.setDocumentos(documentosRecuperados);
                                claseActual.setValue(clase);
                                recuperarVideo();
                            }
                        } else {
                            status.setValue(StatusRequest.ERROR);
                            haHabidoError[0] = true;
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        status.setValue(StatusRequest.ERROR_CONEXION);
                        haHabidoError[0] = true;
                    }
                });
            }else{
                Log.e("RetrofitErrorDocumentos", "No se pudieron recibir los documentos");
                break;
            }
        }
        if(idDocumentos == null || idDocumentos.length == 0){
            status.setValue(StatusRequest.ERROR);
            Log.e("Error BD", "No tiene asociados documentos");
            recuperarVideo();
        }
    }

    public int descargarDocumento(Context context, Uri treeUri, int posicionDocumento) {
        if (claseActual.getValue().getDocumentos() == null) {
            return -1;
        }

        ArrayList<DocumentoDTO> lista = claseActual.getValue().getDocumentos();
        if(posicionDocumento < 0 || posicionDocumento >= lista.size()){
            return -1;
        }

        byte[] pdfData = lista.get(posicionDocumento).getDocumento();

        try {
            DocumentFile pickedDir = DocumentFile.fromTreeUri(context, treeUri);
            DocumentFile newFile = pickedDir.createFile("application/pdf", lista.get(posicionDocumento).getNombre() + ".pdf");

            OutputStream outputStream = context.getContentResolver().openOutputStream(newFile.getUri());

            outputStream.write(pdfData);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    public void recuperarVideo(){
        status.setValue(StatusRequest.ESPERA);
        VideoGrpc.descargarVideo(claseActual.getValue().getVideoId(), this);
    }

    @Override
    public void notificarReciboExitoso(ByteArrayOutputStream output) {
        byte[] video = output.toByteArray();
        File archivoVideo = FileUtil.convertirAFileArrayByte(InicioSesion.obtenerContexto(), video);
        if(archivoVideo != null){
            status.postValue(StatusRequest.DONE);
            DocumentoDTO documentoDTO = new DocumentoDTO();
            documentoDTO.setFile(archivoVideo);
            documentoDTO.setIdClase(claseActual.getValue().getIdClase());

            ClaseDTO claseModificada = claseActual.getValue();
            claseModificada.setVideoDocumento(documentoDTO);
            claseActual.postValue(claseModificada);
        }else{
            status.postValue(StatusRequest.ERROR);
        }
    }

    @Override
    public void notificarReciboFallido() {
        Log.d("gRPC", "Video recibo fallido desde interfaz");
        status.postValue(StatusRequest.ERROR);
    }
}
