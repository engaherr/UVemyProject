package com.example.uvemyproject;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.UsuarioItemBinding;
import com.example.uvemyproject.dto.UsuarioDTO;

public class UsuarioAdapter extends ListAdapter<UsuarioDTO, UsuarioAdapter.UsuarioViewHolder> {

    private static final DiffUtil.ItemCallback<UsuarioDTO> DIFF_CALLBACK = new DiffUtil.ItemCallback<UsuarioDTO>() {
        @Override
        public boolean areItemsTheSame(@NonNull UsuarioDTO oldItem, @NonNull UsuarioDTO newItem) {
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull UsuarioDTO oldItem, @NonNull UsuarioDTO newItem) {
            return oldItem.equals(newItem);
        }
    };

    protected UsuarioAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UsuarioItemBinding binding = UsuarioItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UsuarioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        UsuarioDTO usuario = getItem(position);
        holder.bind(usuario);
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        private final UsuarioItemBinding binding;

        public UsuarioViewHolder(@NonNull UsuarioItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UsuarioDTO usuario) {
            binding.txtViewNombreUsuario.setText(usuario.getNombres());
            binding.txtViewApellidoUsuario.setText(usuario.getApellidos());
            binding.txtViewCorreoUsuario.setText(usuario.getCorreoElectronico());
        }
    }
}
