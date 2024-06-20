package com.example.uvemyproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.RespuestaItemBinding;
import com.example.uvemyproject.dto.ComentarioDTO;

public class RespuestaAdapter extends ListAdapter<ComentarioDTO, RespuestaAdapter.RespuestaViewHolder> {
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

    protected RespuestaAdapter() { super(DIFF_CALLBACK); }

    @NonNull
    @Override
    public RespuestaAdapter.RespuestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RespuestaItemBinding binding =
                RespuestaItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new RespuestaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RespuestaAdapter.RespuestaViewHolder holder, int position) {
        ComentarioDTO comentario = getItem(position);
        holder.bind(comentario);
    }

    static class RespuestaViewHolder extends RecyclerView.ViewHolder {

        private final RespuestaItemBinding binding;

        public RespuestaViewHolder(@NonNull RespuestaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ComentarioDTO respuesta) {
            binding.txtViewNombreUsuario.setText(respuesta.getNombreUsuario());
            binding.txtViewRespuesta.setText(respuesta.getDescripcion());
        }
    }
}
