package com.example.uvemyproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.EstudianteItemBinding;
import com.example.uvemyproject.dto.ClaseDTO;

public class EstudianteAdapter extends ListAdapter<String, EstudianteAdapter.EstudianteViewHolder> {


    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return (oldItem == newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return (oldItem.equals(newItem));
        }
    };
    protected EstudianteAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public EstudianteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EstudianteItemBinding binding = EstudianteItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new EstudianteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EstudianteViewHolder holder, int position) {
        String nombre = getItem(position);
        holder.bindEstudiante(nombre);
    }

    public static class EstudianteViewHolder extends RecyclerView.ViewHolder{

        private EstudianteItemBinding binding;
        public EstudianteViewHolder(@NonNull EstudianteItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
        private void bindEstudiante(String nombre){
            binding.txtViewNombreEstudiante.setText(nombre);
        }
    }
}
