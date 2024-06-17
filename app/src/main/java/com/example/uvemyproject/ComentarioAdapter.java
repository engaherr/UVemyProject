package com.example.uvemyproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.ComentarioItemBinding;
import com.example.uvemyproject.dto.ComentarioDTO;
import com.example.uvemyproject.dto.ComentarioEnvioDTO;
import com.example.uvemyproject.utils.SingletonUsuario;
import com.example.uvemyproject.viewmodels.ClaseDetallesViewModel;

public class ComentarioAdapter extends ListAdapter<ComentarioDTO, ComentarioAdapter.ComentarioViewHolder> {

    protected final ClaseDetallesViewModel viewModel;

    public static final DiffUtil.ItemCallback<ComentarioDTO> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ComentarioDTO>() {
        @Override
        public boolean areItemsTheSame(@NonNull ComentarioDTO oldItem, @NonNull ComentarioDTO newItem) {
            return (oldItem.getIdComentario() == newItem.getIdComentario());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ComentarioDTO oldItem, @NonNull ComentarioDTO newItem) {
            return (oldItem.equals(newItem));
        }
    };

    protected ComentarioAdapter(ClaseDetallesViewModel claseDetallesViewModel) {
        super(DIFF_CALLBACK);
        viewModel = claseDetallesViewModel;
    }

    @NonNull
    @Override
    public ComentarioAdapter.ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ComentarioItemBinding binding =
                ComentarioItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ComentarioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        ComentarioDTO comentario = getItem(position);
        holder.bind(comentario);
    }

    class ComentarioViewHolder extends RecyclerView.ViewHolder {

        private final ComentarioItemBinding binding;

        public ComentarioViewHolder(@NonNull ComentarioItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ComentarioDTO comentario) {
            binding.txtViewNombreUsuario.setText(comentario.getNombreUsuario());
            binding.txtViewComentarioPrincipal.setText(comentario.getDescripcion());

            if(comentario.getRespuestas().isEmpty()){
                binding.txtViewTituloRespuestas.setVisibility(View.GONE);
                binding.rcyViewRespuestas.setVisibility(View.GONE);
            } else {
                RespuestaAdapter respuestaAdapter = new RespuestaAdapter();
                binding.rcyViewRespuestas.setLayoutManager(
                        new LinearLayoutManager(binding.getRoot().getContext()));
                binding.rcyViewRespuestas.setAdapter(respuestaAdapter);
                respuestaAdapter.submitList(comentario.getRespuestas());
                respuestaAdapter.notifyDataSetChanged();
            }

            binding.btnResponder.setOnClickListener( v -> clickResponder(comentario.getIdComentario()));
        }

        private void clickResponder(int idComentario) {
            binding.lnrLayoutRespuestaNueva.setVisibility(View.VISIBLE);
            binding.btnResponder.setVisibility(View.GONE);
            binding.edtTextRespuestaNueva.requestFocus();

            binding.btnEnviarRespuesta.setOnClickListener( v -> clickEnviar(idComentario));
        }

        private void clickEnviar(int idComentario) {
            String respuesta = binding.edtTextRespuestaNueva.getText().toString().trim();

            if(respuesta.isEmpty()){
                binding.edtTextRespuestaNueva.setError("Escribe algo para enviar una respuesta");
            } else {
                enviarRespuesta(respuesta, idComentario);
            }
        }

        private void enviarRespuesta(String respuesta, int idComentario) {
            binding.edtTextRespuestaNueva.setText("");
            binding.lnrLayoutRespuestaNueva.setVisibility(View.GONE);
            binding.btnResponder.setVisibility(View.VISIBLE);

            ComentarioEnvioDTO comentarioEnvioDTO = new ComentarioEnvioDTO(
                    viewModel.getClaseActual().getValue().getIdClase(),
                    SingletonUsuario.getIdUsuario(),
                    respuesta,
                    idComentario
            );

            viewModel.enviarComentario(comentarioEnvioDTO);
        }
    }
}
