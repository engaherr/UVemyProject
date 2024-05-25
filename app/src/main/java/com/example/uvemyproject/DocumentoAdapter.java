package com.example.uvemyproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.DocumentoItemBinding;
import com.example.uvemyproject.dto.DocumentoDTO;

public class DocumentoAdapter extends ListAdapter<DocumentoDTO, DocumentoAdapter.DocumentoViewHolder> {

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

    protected DocumentoAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public DocumentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DocumentoItemBinding binding = DocumentoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new DocumentoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentoViewHolder holder, int position) {
        DocumentoDTO documento = getItem(position);
        holder.bindDocumento(documento, position);
    }
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class DocumentoViewHolder extends RecyclerView.ViewHolder {
        private DocumentoItemBinding binding;

        public DocumentoViewHolder(@NonNull DocumentoItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bindDocumento(DocumentoDTO documento, int posicionDocumento){
            binding.txtViewNombreDocumento.setText(documento.getNombre());
            binding.imgViewEliminar.setOnClickListener(v-> onItemClickListener.onItemClickListener(posicionDocumento));
        }
    }
}
