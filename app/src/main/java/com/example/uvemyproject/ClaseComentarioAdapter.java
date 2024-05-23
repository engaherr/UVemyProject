package com.example.uvemyproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.ClaseComentarioItemBinding;
import com.example.uvemyproject.dto.ClaseEstadisticaDTO;

public class ClaseComentarioAdapter extends ListAdapter<ClaseEstadisticaDTO, ClaseComentarioAdapter.ClaseComentarioViewHolder> {
    private static final DiffUtil.ItemCallback<ClaseEstadisticaDTO> DIFF_CALLBACK = new DiffUtil.ItemCallback<ClaseEstadisticaDTO>() {
        @Override
        public boolean areItemsTheSame(@NonNull ClaseEstadisticaDTO oldItem, @NonNull ClaseEstadisticaDTO newItem) {
            return (oldItem.getNombre() == newItem.getNombre());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ClaseEstadisticaDTO oldItem, @NonNull ClaseEstadisticaDTO newItem) {
            return (oldItem.equals(newItem));
        }
    };
    protected ClaseComentarioAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ClaseComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClaseComentarioItemBinding binding = ClaseComentarioItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ClaseComentarioAdapter.ClaseComentarioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaseComentarioViewHolder holder, int position) {
        ClaseEstadisticaDTO clase = getItem(position);
        holder.bindClaseComentario(clase, position);
    }

    public static class ClaseComentarioViewHolder extends RecyclerView.ViewHolder {
        private ClaseComentarioItemBinding binding;
        public ClaseComentarioViewHolder(@NonNull ClaseComentarioItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bindClaseComentario(ClaseEstadisticaDTO clase, int posicion){
            binding.txtViewNombreClase.setText(clase.getNombre());
            binding.txtViewComentariosTotales.setText(String.valueOf(clase.getCantidadComentarios()));
            binding.txtViewNumeroClase.setText("Clase " + (posicion + 1));
        }
    }
}
