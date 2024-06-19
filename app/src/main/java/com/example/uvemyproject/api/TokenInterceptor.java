package com.example.uvemyproject.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.uvemyproject.utils.SingletonUsuario;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (response.header("Set-Authorization") != null) {
            String tokenNuevo = response.header("Set-Authorization").toString();
            Log.i("TOKEEEEN", tokenNuevo);
            //SingletonUsuario.setJwt();
        }

        return response;
    }
}
