package com.example.uvemyproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.DocumentoItemBinding;
import com.example.uvemyproject.dto.DocumentoDTO;

public class DocumentoAdapter extends ListAdapter<DocumentoDTO, DocumentoAdapter.DocumentoViewHolder> {

    private boolean puedeEliminar = true;
    private static final DiffUtil.ItemCallback<DocumentoDTO> DIFF_CALLBACK = new DiffUtil.ItemCallback<DocumentoDTO>() {
        @Override
        public boolean areItemsTheSame(@NonNull DocumentoDTO oldItem, @NonNull DocumentoDTO newItem) {
            return (oldItem.getIdClase() == newItem.getIdClase());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DocumentoDTO oldItem, @NonNull DocumentoDTO newItem) {
            return (oldItem.equals(newItem));
        }
    };


    protected DocumentoAdapter(boolean puedeEliminar) {
        super(DIFF_CALLBACK);
        this.puedeEliminar = puedeEliminar;
    }

    @NonNull
    @Override
    public DocumentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DocumentoItemBinding binding = DocumentoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new DocumentoViewHolder(binding, puedeEliminar);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentoViewHolder holder, int position) {
        DocumentoDTO documento = getItem(position);
        holder.bindDocumento(documento, position);
    }
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClickListener(DocumentoDTO documento, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class DocumentoViewHolder extends RecyclerView.ViewHolder {
        private DocumentoItemBinding binding;

        public DocumentoViewHolder(@NonNull DocumentoItemBinding itemView, boolean puedeEliminar) {
            super(itemView.getRoot());
            binding = itemView;
            if(!puedeEliminar){
                binding.imgViewEliminar.setVisibility(View.INVISIBLE);
                binding.imgViewDescargar.setVisibility(View.VISIBLE);
            }
        }

        public void bindDocumento(DocumentoDTO documento, int posicionDocumento){
            binding.txtViewNombreDocumento.setText(documento.getNombre());
            if(puedeEliminar){
                binding.imgViewEliminar.setOnClickListener(v-> onItemClickListener.onItemClickListener(documento, posicionDocumento));
            }else{
                binding.imgViewDescargar.setOnClickListener(v-> onItemClickListener.onItemClickListener(documento, posicionDocumento));
            }
        }
    }
}
