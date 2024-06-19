package com.example.uvemyproject.viewmodels;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.DocumentoServices;
import com.example.uvemyproject.api.services.EstadisticaServices;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CursoDTO;
import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.utils.FileUtil;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaseDetallesViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<ClaseDTO> claseActual = new MutableLiveData<>();
    private final MutableLiveData<CursoDTO> cursoActual = new MutableLiveData<>();
    public LiveData<CursoDTO> getCurso(){
        return cursoActual;
    }
    public void setCurso (CursoDTO cursoNuevo){
        cursoActual.setValue(cursoNuevo);
    }
    public LiveData<StatusRequest> getStatus(){
        return status;
    }
    public LiveData<ClaseDTO> getClaseActual() { return claseActual; }

    public void recuperarDetallesClase(int idClase){
        ClaseServices service = ApiClient.getInstance().getClaseServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.obtenerClase(auth, idClase).enqueue(new Callback<ClaseDTO>() {
            @Override
            public void onResponse(Call<ClaseDTO> call, Response<ClaseDTO> response) {
                if(response.isSuccessful()){
                    claseActual.setValue(response.body());
                    obtenerDocumentosClase();
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
                                //Recuperar video y comentarios
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
            recuperarVideo();
            Log.e("Error BD", "No tiene asociados documentos");
        }
    }

    private void recuperarVideo(){
        //Al final debería de recuperar y poner el vide, se debe poner el status done para quitar el progress bar
        status.setValue(StatusRequest.DONE);
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
}
