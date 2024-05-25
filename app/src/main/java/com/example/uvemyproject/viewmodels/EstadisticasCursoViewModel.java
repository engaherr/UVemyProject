package com.example.uvemyproject.viewmodels;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.EstadisticaServices;
import com.example.uvemyproject.dto.EstadisticasCursoDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import java.io.IOException;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadisticasCursoViewModel extends ViewModel {
    private String nombreReporte = "";
    private final MutableLiveData<byte[]> reportePdf = new MutableLiveData<>();
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();

    private final MutableLiveData<EstadisticasCursoDTO> estadisticasCurso = new MutableLiveData<>();
    public LiveData<byte[]> getReporte(){
        return reportePdf;
    }
    public LiveData<StatusRequest> getStatus(){
        return status;
    }
    public LiveData<EstadisticasCursoDTO> getEstadisticas(){
        return estadisticasCurso;
    }
    public void recuperarEstadisticasCurso(){
        EstadisticaServices service = ApiClient.getInstance().getEstadisticaServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        service.obtenerEstadisticasCurso(1, auth).enqueue(new Callback<EstadisticasCursoDTO>() {
            @Override
            public void onResponse(Call<EstadisticasCursoDTO> call, Response<EstadisticasCursoDTO> response) {
                if (response.isSuccessful()) {
                    estadisticasCurso.setValue(response.body());
                    status.setValue(StatusRequest.DONE);
                }else{
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<EstadisticasCursoDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void recuperarDocumentoEstadisticas(){
        EstadisticaServices service = ApiClient.getInstance().getEstadisticaServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.obtenerReporteCurso(1, auth).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String contentDisposition = response.headers().get("Content-Disposition");
                    if (contentDisposition != null && contentDisposition.contains("filename=")) {
                        nombreReporte = contentDisposition.split("filename=")[1].replace("\"", "");
                    }else{
                        nombreReporte= "Reporte.pdf";
                    }

                    try {
                        reportePdf.setValue(response.body().bytes());
                    } catch (IOException e) {
                        reportePdf.setValue(null);
                    }
                } else {
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });

    }

    public int descargarReporte(Context context, Uri treeUri) {
        if (reportePdf.getValue() == null) {
            return -1;
        }
        byte[] pdfData = reportePdf.getValue();

        try {
            DocumentFile pickedDir = DocumentFile.fromTreeUri(context, treeUri);
            DocumentFile newFile = pickedDir.createFile("application/pdf", nombreReporte);

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

