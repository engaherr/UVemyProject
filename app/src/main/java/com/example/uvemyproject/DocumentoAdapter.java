package com.example.uvemyproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.ClaseItemBinding;
import com.example.uvemyproject.databinding.DocumentoItemBinding;
import com.example.uvemyproject.dto.ClaseDTO;
import com.example.uvemyproject.dto.DocumentoDTO;
import com.example.uvemyproject.interfaces.INotificacionDocumento;

public class DocumentoAdapter extends ListAdapter<DocumentoDTO, DocumentoAdapter.DocumentoViewHolder> {

    private INotificacionDocumento notificacionDocumento;
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

    protected DocumentoAdapter(INotificacionDocumento notificarDocumento) {
        super(DIFF_CALLBACK);
        this.notificacionDocumento = notificarDocumento;
    }

    @NonNull
    @Override
    public DocumentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DocumentoItemBinding binding = DocumentoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new DocumentoViewHolder(binding, notificacionDocumento);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentoViewHolder holder, int position) {
        DocumentoDTO documento = getItem(position);
        holder.bindDocumento(documento, position);
    }

    public static class DocumentoViewHolder extends RecyclerView.ViewHolder {
        private DocumentoItemBinding binding;
        private INotificacionDocumento notificacionDocumento;

        public DocumentoViewHolder(@NonNull DocumentoItemBinding itemView, INotificacionDocumento notificacionDocumento) {
            super(itemView.getRoot());
            binding = itemView;
            this.notificacionDocumento = notificacionDocumento;
        }

        public void bindDocumento(DocumentoDTO documento, int posicionDocumento){
            binding.txtViewNombreDocumento.setText(documento.getNombre());
            binding.imgViewEliminar.setOnClickListener(v -> notificacionDocumento.eliminarDocumento(posicionDocumento));

        }


    }
}
