package com.example.uvemyproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.ClaseItemBinding;
import com.example.uvemyproject.dto.ClaseDTO;

public class ClaseAdapter extends ListAdapter<ClaseDTO, ClaseAdapter.ClaseViewHolder> {

    private static final DiffUtil.ItemCallback<ClaseDTO> DIFF_CALLBACK = new DiffUtil.ItemCallback<ClaseDTO>() {
        @Override
        public boolean areItemsTheSame(@NonNull ClaseDTO oldItem, @NonNull ClaseDTO newItem) {
            return (oldItem.getIdClase() == newItem.getIdClase());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ClaseDTO oldItem, @NonNull ClaseDTO newItem) {
            return (oldItem.equals(newItem));
        }
    };
    protected ClaseAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClaseItemBinding binding = ClaseItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ClaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaseViewHolder holder, int position) {
        ClaseDTO clase = getItem(position);
        holder.bindClase(clase, position);
    }

    public static class ClaseViewHolder extends RecyclerView.ViewHolder {
        private ClaseItemBinding binding;
        public ClaseViewHolder(@NonNull ClaseItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bindClase(ClaseDTO clase, int posicion){
            binding.txtViewNumeroClase.setText("Clase " + (posicion + 1));
            binding.txtViewNombreClase.setText(clase.getNombre());
        }
    }

}
