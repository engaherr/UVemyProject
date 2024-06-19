package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.ClaseServices;
import com.example.uvemyproject.api.services.CursoServices;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.CrearCursoDTO;
import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.security.auth.callback.Callback;

public class FormularioCursoViewModel extends ViewModel {
    private final MutableLiveData<CrearCursoDTO> cursoActual = new MutableLiveData<CrearCursoDTO>();
    private final MutableLiveData<byte[]> arrayBites = new MutableLiveData<byte[]>();
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public LiveData<CrearCursoDTO> getCursoActual(){
        if (cursoActual.getValue() == null) {
            cursoActual.setValue(new CrearCursoDTO());
        }
        return cursoActual;
    }

    public void setArrayBites(byte[] arrayBites){
        this.arrayBites.setValue(arrayBites);
        CrearCursoDTO curso = cursoActual.getValue();
        if (curso == null) {
            curso.setArchivo(arrayBites);
            cursoActual.setValue(curso);
        } else {
            Log.d("Log","SetArray");
            cursoActual.getValue().setArchivo(arrayBites);
        }
    }

    public void setCursoActual(CrearCursoDTO cursoActual){
        this.cursoActual.setValue(cursoActual);
    }

    private MultipartBody.Part crearFilePeticion() {
        byte[] fileBytes = cursoActual.getValue().getArchivo();
        if(fileBytes != null){
            Log.d("Log","Array"+fileBytes.length);
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), fileBytes);
        return MultipartBody.Part.createFormData("file", "archivo.png", requestFile);
    }
    public void guardarCurso(CrearCursoDTO curso){
        CursoServices service = ApiClient.getInstance().getCursoServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        RequestBody tituloPart = createPartFromString(curso.getTitulo());
        RequestBody descripcionPart = createPartFromString(curso.getDescripcion());
        RequestBody objetivosPart = createPartFromString(curso.getObjetivos());
        RequestBody requisitosPart = createPartFromString(curso.getRequisitos());
        ArrayList<RequestBody> etiquetasParts = createEtiquetasParts(curso.getEtiquetas());
        MultipartBody.Part filePart = crearFilePeticion();
        service.crearCurso(auth, tituloPart, descripcionPart, objetivosPart, requisitosPart, etiquetasParts, filePart).enqueue(new retrofit2.Callback<CrearCursoDTO>(){@Override
            public void onResponse(Call<CrearCursoDTO> call, Response<CrearCursoDTO> response) {
                if (response.isSuccessful()) {
                    status.setValue(StatusRequest.DONE);
                    Log.d("Log", response.message()+" "+response.code()+" "+response.body());
                }else{
                    Log.d("Log", response.message()+" "+response.code()+" "+response.body());
                    status.setValue(StatusRequest.ERROR);
                }
            }
            @Override
            public void onFailure(Call<CrearCursoDTO> call, Throwable t) {
                Log.e("Log", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void modificarCurso(CrearCursoDTO curso){
        Log.d("Log", "Modificar "+curso.getIdCurso()+" idCurso");
        CursoServices service = ApiClient.getInstance().getCursoServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        RequestBody idCursoPart = createPartFromString(String.valueOf(curso.getIdCurso()));
        RequestBody tituloPart = createPartFromString(curso.getTitulo());
        RequestBody descripcionPart = createPartFromString(curso.getDescripcion());
        RequestBody objetivosPart = createPartFromString(curso.getObjetivos());
        RequestBody requisitosPart = createPartFromString(curso.getRequisitos());
        RequestBody idDocumentoPart = createPartFromString(String.valueOf(curso.getIdDocumento()));
        ArrayList<RequestBody> etiquetasParts = createEtiquetasParts(curso.getEtiquetas());
        MultipartBody.Part filePart = crearFilePeticion();
        service.modificarCurso(auth, curso.getIdCurso(), idCursoPart, idDocumentoPart, tituloPart, descripcionPart, objetivosPart, requisitosPart, etiquetasParts, filePart).enqueue(new retrofit2.Callback<CrearCursoDTO>(){@Override
        public void onResponse(Call<CrearCursoDTO> call, Response<CrearCursoDTO> response) {
            if (response.isSuccessful()) {
                status.setValue(StatusRequest.DONE);
                Log.d("Log", response.message()+" "+response.code()+" "+response.body());
            }else{
                Log.d("Log", response.message()+" "+response.code()+" "+response.body());
                status.setValue(StatusRequest.ERROR);
            }
        }
            @Override
            public void onFailure(Call<CrearCursoDTO> call, Throwable t) {
                Log.e("Log", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void eliminarCurso(){
        Log.d("Log", "Guardar");
        CursoServices service = ApiClient.getInstance().getCursoServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();
        service.eliminarCurso(auth, cursoActual.getValue().getIdCurso()).enqueue(new retrofit2.Callback<CrearCursoDTO>(){@Override
        public void onResponse(Call<CrearCursoDTO> call, Response<CrearCursoDTO> response) {
            if (response.isSuccessful()) {
                status.setValue(StatusRequest.DONE);
                Log.d("Log", response.message()+" "+response.code()+" "+response.body());
            }else{
                Log.d("Log", response.message()+" "+response.code()+" "+response.body());
                status.setValue(StatusRequest.ERROR);
            }
        }
            @Override
            public void onFailure(Call<CrearCursoDTO> call, Throwable t) {
                Log.e("Log", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private RequestBody createPartFromString(String value) {
        return RequestBody.create(value,MediaType.parse("text/plain"));
    }

    private ArrayList<RequestBody> createEtiquetasParts(List<Integer> listIdEtiquetas) {
        ArrayList<RequestBody> etiquetasParts = new ArrayList<>();
        for (Integer etiqueta : Objects.requireNonNull(listIdEtiquetas)) {
            RequestBody etiquetaPart = createPartFromString(String.valueOf(etiqueta));
            etiquetasParts.add(etiquetaPart);
        }
        return etiquetasParts;
    }

}
