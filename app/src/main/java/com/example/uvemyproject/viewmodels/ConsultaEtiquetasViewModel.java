package com.example.uvemyproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uvemyproject.api.ApiClient;
import com.example.uvemyproject.api.services.EtiquetaServices;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.utils.StatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultaEtiquetasViewModel extends ViewModel {
    private final MutableLiveData<StatusRequest> status = new MutableLiveData<>();
    private final MutableLiveData<List<EtiquetaDTO>> etiquetas = new MutableLiveData<>();
    private final MutableLiveData<String> jwt = new MutableLiveData<>();
    public MutableLiveData<StatusRequest> getStatus() {
        return status;
    }

    public MutableLiveData<List<EtiquetaDTO>> getEtiquetas() {
        return etiquetas;
    }
    public MutableLiveData<String> getJwt() {
        return jwt;
    }

    public void obtenerEtiquetas() {
        EtiquetaServices service = ApiClient.getInstance().getEtiquetaServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        service.getEtiquetas(auth).enqueue(new Callback<List<EtiquetaDTO>>() {
            @Override
            public void onResponse(Call<List<EtiquetaDTO>> call, Response<List<EtiquetaDTO>> response) {
                if(response.isSuccessful()) {
                    etiquetas.postValue(response.body());
                    status.setValue(StatusRequest.DONE);
                } else {
                    Log.e("RetrofitError", "Error al obtener etiquetas");
                    status.setValue(StatusRequest.ERROR_CONSULTA);
                }
            }

            @Override
            public void onFailure(Call<List<EtiquetaDTO>> call, Throwable t) {
                Log.e("RetrofitError", "Error de red o excepción: " + t.getMessage(), t);
                status.setValue(StatusRequest.ERROR_CONEXION);
            }
        });
    }

    public void eliminarEtiquetasSeleccionadas(List<Integer> idsEtiquetas) {
        EtiquetaServices service = ApiClient.getInstance().getEtiquetaServices();
        String auth = "Bearer " + SingletonUsuario.getJwt();

        int totalEtiquetas = idsEtiquetas.size();
        int[] eliminacionesExitosas = {0};
        int[] eliminacionesFallidas = {0};
        int[] forbidden = {0};

        for (int idEtiqueta : idsEtiquetas) {
            service.eliminarEtiqueta(idEtiqueta, auth).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("ConsultaEtiquetasViewModel", "Etiqueta eliminada exitosamente: " + idEtiqueta);
                        eliminacionesExitosas[0]++;
                    } else if (response.code() == 403) {
                        Log.e("RetrofitError", "Forbidden al eliminar etiqueta: " + idEtiqueta);
                        forbidden[0]++;
                    } else {
                        Log.e("RetrofitError", "Error al eliminar etiqueta: " + idEtiqueta);
                        status.setValue(StatusRequest.ERROR_ELIMINACION);
                        eliminacionesFallidas[0]++;
                    }
                    verificarEliminacionesCompletas(totalEtiquetas, eliminacionesExitosas[0],
                            eliminacionesFallidas[0], forbidden[0]);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("RetrofitError", "Error de red o excepción al eliminar etiqueta: " + idEtiqueta, t);
                    eliminacionesFallidas[0]++;
                    status.setValue(StatusRequest.ERROR_CONEXION);
                    verificarEliminacionesCompletas(totalEtiquetas, eliminacionesExitosas[0],
                            eliminacionesFallidas[0], forbidden[0]);
                }
            });
        }
    }

    private void verificarEliminacionesCompletas(int totalEtiquetas, int eliminacionesExitosas,
                                                 int eliminacionesFallidas, int forbidden) {
        if (eliminacionesExitosas + eliminacionesFallidas + forbidden == totalEtiquetas) {
            if (forbidden > 0) {
                status.setValue(StatusRequest.FORBIDDEN);
            } else if (eliminacionesFallidas > 0) {
                status.setValue(StatusRequest.ERROR);
            } else {
                status.setValue(StatusRequest.NO_CONTENT);
                obtenerEtiquetas();
            }
        }
    }

}