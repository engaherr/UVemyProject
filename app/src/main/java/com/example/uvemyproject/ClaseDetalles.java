package com.example.uvemyproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uvemyproject.databinding.FragmentClaseDetallesBinding;
import com.example.uvemyproject.dto.ComentarioEnvioDTO;
import com.example.uvemyproject.interfaces.INotificacionReciboVideo;
import com.example.uvemyproject.servicio.VideoGrpc;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.viewmodels.ClaseDetallesViewModel;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ClaseDetalles extends Fragment implements INotificacionReciboVideo {
    private FragmentClaseDetallesBinding binding;
    private ClaseDetallesViewModel viewModel;
    private DocumentoAdapter adapter;
    private ComentarioAdapter comentarioAdapter;
    private int documentoSeleccionado = -1;
    private static final int PICK_DIRECTORY_REQUEST_CODE = 1;
    private File videoTempFile;
    private BufferedOutputStream bufferedOutputStream;

    public ClaseDetalles() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClaseDetallesBinding.inflate(getLayoutInflater());
        binding.imgViewRegresar.setOnClickListener( v -> {
            CursoDetallesPrincipal cursoDetalles = new CursoDetallesPrincipal();
            ((MainActivity) getActivity()).cambiarFragmentoPrincipal(cursoDetalles);
        });

        adapter = new DocumentoAdapter(false);
        binding.rcyViewDocumentos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewDocumentos.setAdapter(adapter);
        adapter.setOnItemClickListener((documento, posicion) -> descargarDocumento(posicion));

        viewModel = new ViewModelProvider(this).get(ClaseDetallesViewModel.class);

        observarStatus();
        observarClase();
        observarComentarios();
        observarStatusEnviarComentario();

        binding.lnrLayoutModificarClase.setOnClickListener(v -> cambiarFormularioClase());
        binding.btnEnviarComentario.setOnClickListener(v -> enviarComentario());

        obtenerIdClase();

        return binding.getRoot();
    }

    private void observarVideoStream() {
        viewModel.getStreamVideo().observe(getViewLifecycleOwner(), inputStream -> {
            try {
                Log.d("gRPC", "observarVideoStream: cargando video");
                File tempFile = streamToFile(inputStream);
                BufferedOutputStream bufferedOutputStream =
                        new BufferedOutputStream(Files.newOutputStream(tempFile.toPath()));

                Log.d("gRPC", tempFile.getAbsolutePath());
                binding.videoView.setVideoURI(Uri.fromFile(tempFile));
                MediaController mediaController = new MediaController(getContext());

                binding.videoView.setMediaController(mediaController);
                mediaController.setAnchorView(binding.videoView);

                binding.videoView.setOnPreparedListener(mp -> {
                    Log.d("gRPC", "observarVideoStream: video preparado, iniciando reproducción");
                    binding.videoView.start();
                });

                binding.videoView.setOnErrorListener((mp, what, extra) -> {
                    Log.e("gRPC", "Error al preparar el video: " + what + ", extra: " + extra);
                    Toast.makeText(getContext(), "Error al preparar el video", Toast.LENGTH_SHORT).show();
                    return true;
                });

                Log.d("gRPC", "observarVideoStream: carga video completada");
            } catch (IOException e) {
                Log.e("gRPC", "Error al reproducir el video", e);
                Toast.makeText(getContext(), "Error al cargar el video", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File streamToFile(InputStream inputStream) throws IOException {
        Log.d("gRPC", "streamToFile: creando archivo temporal");
        File tempFile = new File(getContext().getCacheDir(), "video.mp4");
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            Log.d("gRPC", "streamToFile: archivo temporal creado" + tempFile.getAbsolutePath());
            return tempFile;
        }
    }

    private void enviarComentario() {
        String comentario = binding.edtTextComentario.getText().toString();

        if(comentario.isEmpty()){
            binding.edtTextComentario.setError("Escribe algo para enviar el comentario");
        } else {
            binding.edtTextComentario.setText("");
            ComentarioEnvioDTO comentarioEnvioDTO = new ComentarioEnvioDTO(
                    viewModel.getClaseActual().getValue().getIdClase(),
                    SingletonUsuario.getIdUsuario(),
                    comentario,
                    0
            );
            viewModel.enviarComentario(comentarioEnvioDTO);
        }
    }

    private void observarStatusEnviarComentario() {
        viewModel.getStatusEnviarComentario().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case DONE:
                    Toast.makeText(getContext(),"Comentario enviado exitosamente",
                            Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error al enviar el comentario." +
                            " Intente de nuevo más tarde", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor. " +
                                    "Intente más tarde",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void observarStatus(){
        viewModel.getStatus().observe(getViewLifecycleOwner(), status ->{
            switch (status){
                case ERROR:
                    Toast.makeText(getContext(),"Ocurrió un error en el servidor, no se pudieron recuperar los datos de la clase", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR_CONEXION:
                    Toast.makeText(getContext(),"No hay conexión con el servidor", Toast.LENGTH_SHORT).show();
                    break;
            }
            quitarEspera();
        });
    }

    private void observarClase(){
        viewModel.getClaseActual().observe(getViewLifecycleOwner(), claseDTO -> {
            if(claseDTO != null){
                binding.setClase(claseDTO);
                if(claseDTO.getDocumentos() != null && claseDTO.getDocumentos().size() > 0){
                    adapter.submitList(claseDTO.getDocumentos());
                    adapter.notifyDataSetChanged();
                }

                if(claseDTO.getVideoId() != 0) {

                    new Thread(() -> {
                        try {
                            videoTempFile = File.createTempFile(
                                    "video", ".mp4", getContext().getCacheDir());
                            bufferedOutputStream = new BufferedOutputStream(
                                    Files.newOutputStream(videoTempFile.toPath()));
                            ByteArrayInputStream inputStream = VideoGrpc.descargarVideo(
                                    claseDTO.getVideoId(),this);
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                byte[] chunk = new byte[bytesRead];
                                System.arraycopy(buffer, 0, chunk, 0, bytesRead);

                                getActivity().runOnUiThread(() ->{
                                    anadirChunkVideo(chunk);
                                });
                            }
                        } catch (IOException e) {
                            Log.e("gRPC", "Error al descargar el video", e);
                        } finally {
                            try {
                                bufferedOutputStream.close();
                            } catch (IOException e) {
                                Log.e("gRPC", "Error al cerrar el stream", e);
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private void anadirChunkVideo(byte[] chunk){
        if(chunk != null && chunk.length > 0) {
            try {
                bufferedOutputStream.write(chunk);
                bufferedOutputStream.flush();
                if(!binding.videoView.isPlaying() && binding.videoView.getCurrentPosition() == 0) {
                    binding.videoView.setVideoPath(videoTempFile.getAbsolutePath());
                    binding.videoView.start();
                }
            } catch (IOException e) {
                Log.e("gRPC", "Error al escribir el chunk de video", e);
            }
        }
    }

    private void observarComentarios() {
        viewModel.getComentarios().observe(getViewLifecycleOwner(), comentarios -> {
            if(comentarios != null && !comentarios.isEmpty()){
                comentarioAdapter =
                        new ComentarioAdapter(viewModel);
                binding.rcyViewComentarios.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rcyViewComentarios.setAdapter(comentarioAdapter);

                comentarioAdapter.submitList(comentarios);
                comentarioAdapter.notifyDataSetChanged();
            }
        });
    }

    private void obtenerIdClase(){
        Bundle args = getArguments();
        if (args != null) {
            int idClase = args.getInt("id_clase");
            viewModel.recuperarDetallesClase(idClase);
            ponerEspera();
        }
    }

    private void cambiarFormularioClase(){
        FormularioClase formularioClase = new FormularioClase();
        Bundle bundle = new Bundle();
        bundle.putParcelable("clave_clase_dto", viewModel.getClaseActual().getValue());
        formularioClase.setArguments(bundle);
        ((MainActivity) getActivity()).cambiarFragmentoPrincipal(formularioClase);
    }

    private void ponerEspera(){
        binding.progressOverlay.setVisibility(View.VISIBLE);
    }

    private void quitarEspera(){
        binding.progressOverlay.setVisibility(View.GONE);
    }

    private void descargarDocumento(int posicionDocumento){
        documentoSeleccionado = posicionDocumento;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona un directorio"), PICK_DIRECTORY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DIRECTORY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri treeUri = data.getData();
            int respuesta = viewModel.descargarDocumento(getContext(), treeUri, documentoSeleccionado);

            if(respuesta == 0){
                Toast.makeText(getContext(),"Se descargó existosamente el documento", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Hubo un problema al descargar el documento", Toast.LENGTH_SHORT).show();
            }

            documentoSeleccionado = -1;
        }

    }

    @Override
    public void notificarReciboExitoso() {
        Toast.makeText(getContext(),"Video cargado correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notificarReciboFallido() {
        Toast.makeText(getContext(),"Hubo un problema al cargar el video",
                Toast.LENGTH_SHORT).show();
    }
}