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
import java.util.Objects;

import javax.security.auth.callback.Callback;

public class FormularioCursoViewModel extends ViewModel {
    private final MutableLiveData<CrearCursoDTO> cursoActual = new MutableLiveData<CrearCursoDTO>();
    private final MutableLiveData<byte[]> arrayBites = new MutableLiveData<byte[]>();
    private final MutableLiveData<ArrayList<Integer>> listIdEtiquetas = new MutableLiveData<ArrayList<Integer>>();

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
        if (curso != null) {
            curso.setArchivo(arrayBites);
            cursoActual.setValue(curso);
        }
    }

    public void setListEtiquetas(ArrayList<Integer> listIdEtiquetas){
        this.listIdEtiquetas.setValue(listIdEtiquetas);
        CrearCursoDTO curso = cursoActual.getValue();
        if (curso != null) {
            curso.setEtiquetas(listIdEtiquetas);
            cursoActual.setValue(curso);
        }
    }
    public void setCursoActual(CrearCursoDTO cursoActual){
        this.cursoActual.setValue(cursoActual);
    }

    private MultipartBody.Part crearFilePeticion() {
        byte[] fileBytes = arrayBites.getValue();
        //RequestBody requestFile = RequestBody.create(arrayBites.getValue(),MediaType.parse("application/json"));
        //MultipartBody.Part body = MultipartBody.Part.createFormData("file",  "pnf", requestFile);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), fileBytes);
        return MultipartBody.Part.createFormData("file", "archivo.png", requestFile);
    }
    public void guardarCurso(CrearCursoDTO curso){
        CursoServices service = ApiClient.getInstance().getCursoServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        RequestBody tituloPart = RequestBody.create(MediaType.parse("text/plain"), curso.getTitulo());
        RequestBody descripcionPart = RequestBody.create(MediaType.parse("text/plain"), curso.getDescripcion());
        RequestBody objetivosPart = RequestBody.create(MediaType.parse("text/plain"), curso.getObjetivos());
        RequestBody requisitosPart = RequestBody.create(MediaType.parse("text/plain"), curso.getRequisitos());
        ArrayList<RequestBody> etiquetasParts = new ArrayList<>();
        for (Integer etiqueta : Objects.requireNonNull(listIdEtiquetas.getValue())) {
            RequestBody etiquetaPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(etiqueta));
            etiquetasParts.add(etiquetaPart);
        }
        MultipartBody.Part filePart = crearFilePeticion();
        service.crearCurso(auth, tituloPart, descripcionPart, objetivosPart, requisitosPart, etiquetasParts, filePart).enqueue(new retrofit2.Callback<CrearCursoDTO>(){@Override
            public void onResponse(Call<CrearCursoDTO> call, Response<CrearCursoDTO> response) {
                if (response.isSuccessful()) {
                    status.setValue(StatusRequest.ERROR_CONEXION);
                }else{
                    Log.d("Log", response.message()+"H H"+response.code()+"H H"+response.body());
                    status.setValue(StatusRequest.ERROR);
                }
            }

            @Override
            public void onFailure(Call<CrearCursoDTO> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    private RequestBody convertirRequestBodyCrearCurso(CrearCursoDTO curso){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<CrearCursoDTO> jsonAdapter = moshi.adapter(CrearCursoDTO.class);
        String jsonEnviado = "";
        try {
            String json = jsonAdapter.toJson(curso);
            JSONObject jsonObject = new JSONObject(json);
            jsonObject.remove("idUsuario");
            jsonObject.remove("idCurso");
            jsonObject.remove("idDocumento");
            jsonObject.remove("archivo");
            jsonEnviado = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody jsonRequestBody = RequestBody.create(jsonEnviado, MediaType.parse("application/json; charset=utf-8"));
        return jsonRequestBody;
    }
}
