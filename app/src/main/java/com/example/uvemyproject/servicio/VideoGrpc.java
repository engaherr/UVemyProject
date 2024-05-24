package com.example.uvemyproject.servicio;

import static android.provider.Settings.System.getString;

import com.example.uvemyproject.R;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class VideoGrpc {
    private static int tamanioChunks = 18 * 1024;
    public static void enviarVideo(){
        String host = "192.168.100.23";
        int puerto = 3000;
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, puerto).usePlaintext().build();




    }
}
