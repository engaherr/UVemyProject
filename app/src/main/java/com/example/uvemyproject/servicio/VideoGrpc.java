package com.example.uvemyproject.servicio;

import android.util.Log;

import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.interfaces.INotificacionEnvioVideo;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.google.protobuf.ByteString;
import com.proto.uvemyproject.Documento;
import com.proto.uvemyproject.VideoServiceGrpc;

import java.io.InputStream;
import java.nio.file.Files;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class VideoGrpc {
    private static ManagedChannel channel;
    private static VideoServiceGrpc.VideoServiceStub stub;
    private static int tamanioChunks = 18 * 1024;

    private static VideoServiceGrpc.VideoServiceStub obtenerStub(){
        if(stub == null){
            String host = "192.168.100.23";
            int puerto = 3001;
            channel = ManagedChannelBuilder.forAddress(host, puerto).usePlaintext().build();
            stub = VideoServiceGrpc.newStub(channel);
        }
        return stub;
    }

    public static void enviarVideo(DocumentoDTO videoPorEnviar, int idClase, INotificacionEnvioVideo notificacion){
        String auth = "Bearer " + SingletonUsuario.getJwt();

        Documento.DocumentoVideo video = Documento.DocumentoVideo.newBuilder()
                .setIdClase(idClase).setIdVideo(videoPorEnviar.getIdDocumento())
                .setNombre(videoPorEnviar.getNombre())
                .setJwt(auth).build();

        Documento.VideoPartesEnvio peticionInicial = Documento.VideoPartesEnvio.newBuilder()
                .setDatosVideo(video).build();

        VideoServiceGrpc.VideoServiceStub stub = obtenerStub();

        StreamObserver<Documento.EnvioVideoRespuesta> responseObserver = new StreamObserver<Documento.EnvioVideoRespuesta>() {
            @Override
            public void onNext(Documento.EnvioVideoRespuesta value) {
                if (value.getRespuesta() == 200) {
                    Log.i("El envío fue exitoso.", "");
                    notificacion.envioExitosoVideo();
                } else {
                    Log.i("Ocurrió un error en el envío.", "");
                    notificacion.envioErroneoVideo();
                }
            }

            @Override
            public void onError(Throwable t) {
                notificacion.envioErroneoVideo();
                Log.i("Error GRPC en envio", t.getMessage());
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                Log.i("Uvemy.grpc", "Envio Exitoso");
            }
        };
        StreamObserver<Documento.VideoPartesEnvio> requestObserver;
        if(videoPorEnviar.getIdDocumento() == 0){
            requestObserver = stub.enviarVideoClase(responseObserver);
        }else{
            requestObserver = stub.actualizarVideoClase(responseObserver);
        }

        try {
            requestObserver.onNext(peticionInicial);

            InputStream inputStream = Files.newInputStream(videoPorEnviar.getFile().toPath());
            byte[] buffer = new byte[tamanioChunks];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                Documento.VideoPartesEnvio chunk = Documento.VideoPartesEnvio.newBuilder()
                        .setChunks(ByteString.copyFrom(buffer, 0, bytesRead))
                        .build();
                requestObserver.onNext(chunk);
            }
            inputStream.close();

            requestObserver.onCompleted();
        } catch (Exception e) {
            Log.i("Error GRPC en catch", e.getMessage());
            requestObserver.onError(e);
        }
    }
}
