package com.example.uvemyproject;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.example.uvemyproject.viewmodels.CursoClaseDetallesViewModel;
import com.example.uvemyproject.viewmodels.EstadisticasCursoViewModel;
import com.example.uvemyproject.viewmodels.FormularioDetallesClaseViewModel;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ClaseDetalles extends Fragment implements INotificacionReciboVideo {
    private boolean videoRecuperado = false;
    private FragmentClaseDetallesBinding binding;
    private ClaseDetallesViewModel viewModel;
    private FormularioDetallesClaseViewModel viewModelCompartido;
    private CursoClaseDetallesViewModel viewModelCompartidoCurso;
    private DocumentoAdapter adapter;
    private ComentarioAdapter comentarioAdapter;
    private int documentoSeleccionado = -1;
    private static final int PICK_DIRECTORY_REQUEST_CODE = 1;

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
            Bundle bundle = new Bundle();
            bundle.putParcelable("clave_curso", viewModelCompartido.obtenerCurso().getValue());
            cursoDetalles.setArguments(bundle);
            ((MainActivity) getActivity()).cambiarFragmentoPrincipal(cursoDetalles);
        });

        adapter = new DocumentoAdapter(false);
        binding.rcyViewDocumentos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcyViewDocumentos.setAdapter(adapter);
        adapter.setOnItemClickListener((documento, posicion) -> descargarDocumento(posicion));

        viewModel = new ViewModelProvider(this).get(ClaseDetallesViewModel.class);

        viewModelCompartido = new ViewModelProvider(requireActivity()).get(FormularioDetallesClaseViewModel.class);
        viewModelCompartidoCurso = new ViewModelProvider(requireActivity()).get(CursoClaseDetallesViewModel.class);

        observarStatus();
        observarClase();
        observarComentarios();
        observarStatusEnviarComentario();

        binding.lnrLayoutModificarClase.setOnClickListener(v -> cambiarFormularioClase());
        binding.btnEnviarComentario.setOnClickListener(v -> enviarComentario());

        obtenerIdClase();
        return binding.getRoot();
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
                
                viewModelCompartido.setClase(claseDTO);

                if(!videoRecuperado && claseDTO.getVideoId() != 0) {
                    ponerEspera();
                    videoRecuperado = true;
                    VideoGrpc.descargarVideo(claseDTO.getVideoId(), this);
                }
            }
        });
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
        if(viewModelCompartidoCurso.obtenerCurso().getValue() != null){
            viewModel.setCurso(viewModelCompartidoCurso.obtenerCurso().getValue());
            viewModelCompartido.setCurso(viewModelCompartidoCurso.obtenerCurso().getValue());
        }

        Bundle args = getArguments();
        if (args != null) {
            int esEstudiante = args.getInt("es_estudiante");
            if(esEstudiante > 0){
                binding.lnrLayoutModificarClase.setVisibility(View.INVISIBLE);
            }
            int idClase = args.getInt("id_clase");
            viewModel.recuperarDetallesClase(idClase);
            ponerEspera();
        }
    }

    private void cambiarFormularioClase(){
        FormularioClase formularioClase = new FormularioClase(true);
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
    public void notificarReciboExitoso(ByteArrayOutputStream outputStream) {
        byte[] byteArray = outputStream.toByteArray();
        writeByteArrayToCacheAndPlay(getContext(), byteArray, binding.videoView);
        Toast.makeText(getContext(),"Video cargado correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notificarReciboFallido() {
        Toast.makeText(getContext(),"Hubo un problema al cargar el video",
                Toast.LENGTH_SHORT).show();
    }

    public void writeByteArrayToCacheAndPlay(Context context, byte[] byteArray, VideoView videoView) {
        FileOutputStream fileOutputStream = null;
        try {
            File cacheDir = context.getCacheDir();
            File videoFile = new File(cacheDir, "video.mp4");
            fileOutputStream = new FileOutputStream(videoFile);

            fileOutputStream.write(byteArray);
            fileOutputStream.flush();

            Log.v("FileWrite", "Archivo escrito con éxito: " + videoFile.getAbsolutePath());

            if (videoFile.exists()) {
                Log.d("FileWrite", "El archivo existe: " + videoFile.getAbsolutePath());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playVideo(videoView, videoFile);
                    }
                });

            } else {
                Log.e("FileWrite", "El archivo no se ha creado.");
            }
        } catch (IOException e) {
            Log.e("FileWrite", "Error escribiendo el archivo: " + e.getMessage(), e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.e("FileWrite", "Error cerrando FileOutputStream: " + e.getMessage(), e);
                }
            }
        }
    }

    private void playVideo(VideoView videoView, File videoFile) {
        videoView.setVideoURI(Uri.fromFile(videoFile));

        MediaController mediaController = new MediaController(videoView.getContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();
        quitarEspera();
    }
}