package com.example.uvemyproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.ComentarioItemBinding;
import com.example.uvemyproject.dto.ComentarioDTO;

public class ComentarioAdapter extends ListAdapter<ComentarioDTO, ComentarioAdapter.ComentarioViewHolder> {

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

    protected ComentarioAdapter() {
        super(DIFF_CALLBACK);
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

    static class ComentarioViewHolder extends RecyclerView.ViewHolder {

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
            }
            //TODO: Agregar lista de ComentarioDTO's a recyclerview
            binding.btnResponder.setOnClickListener( v -> clickResponder());
        }

        private void clickResponder() {
            binding.lnrLayoutRespuestaNueva.setVisibility(View.VISIBLE);
            binding.btnResponder.setVisibility(View.GONE);

            binding.btnSend.setOnClickListener( v -> clickEnviar());
        }

        private void clickEnviar() {
            //TODO: Enviar petición a la API para guardar respuesta
        }
    }
}
