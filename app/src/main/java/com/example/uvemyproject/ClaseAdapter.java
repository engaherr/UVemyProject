package com.example.uvemyproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.ClaseItemBinding;
import com.example.uvemyproject.dto.ClaseDTO;

public class ClaseAdapter extends ListAdapter<ClaseDTO, ClaseAdapter.ClaseViewHolder> {

    private String rolCurso;
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

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClickListener(ClaseDTO clase, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class ClaseViewHolder extends RecyclerView.ViewHolder {
        private ClaseItemBinding binding;
        public ClaseViewHolder(@NonNull ClaseItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bindClase(ClaseDTO clase, int posicion){
            binding.txtViewNumeroClase.setText("Clase " + (posicion + 1));
            binding.txtViewNombreClase.setText(clase.getNombre());
            switch (rolCurso){
                case "Profesor":
                    binding.btnVerMas.setVisibility(View.VISIBLE);
                    break;
                case "Estudiante":
                    binding.btnVerMas.setVisibility(View.VISIBLE);
                    break;
                case "Usuario":
                    binding.btnVerMas.setVisibility(View.INVISIBLE);
                    break;
                default:
                    binding.btnVerMas.setVisibility(View.GONE);
                    break;
            }
            binding.btnVerMas.setOnClickListener( v ->{
                onItemClickListener.onItemClickListener(clase, posicion);
            });
        }
    }

    public void setRolCurso(String rolCurso) {
        this.rolCurso = rolCurso;
    }

}
