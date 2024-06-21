package com.example.uvemyproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.ListaCursosItemBinding;
import com.example.uvemyproject.dto.ListaCursoDTO;
import com.example.uvemyproject.dto.ListaDocumentoDTO;

import java.util.List;

public class ListadoCursosAdapter extends RecyclerView.Adapter<ListadoCursosAdapter.CursoViewHolder> {
    private List<ListaCursoDTO> cursos;
    private Context context;
    private OnCursoClickListener onCursoClickListener;
    public static class CursoViewHolder extends RecyclerView.ViewHolder {
        ListaCursosItemBinding binding;
        ImageView imageView;
        TextView textViewTitulo;

        public CursoViewHolder(View itemView) {
            super(itemView);
            binding = ListaCursosItemBinding.bind(itemView);
            imageView = binding.imgViewMiniatura;
            textViewTitulo = binding.txtViewTitulo;

        }
    }
    @Override
    public CursoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_cursos_item, parent, false);
        return new CursoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CursoViewHolder holder, int position) {
        ListaCursoDTO curso = cursos.get(position);
        ListaDocumentoDTO documento = curso.getDocumentos().get(0);
        if (documento != null && documento.getArchivo() != null && documento.getArchivo().getData() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(documento.getArchivo().getData(), 0, documento.getArchivo().getData().length);

            holder.binding.txtViewTitulo.setText(curso.getTitulo());
            holder.binding.imgViewMiniatura.setImageBitmap(bitmap);
        } else {
            holder.binding.imgViewMiniatura.setImageResource(R.drawable.delete);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCursoClickListener != null) {
                    onCursoClickListener.onCursoClick(curso.getIdCurso(),  curso.getTitulo(),documento.getArchivo().getData());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    public interface OnCursoClickListener {
        void onCursoClick(int idCurso, String titulo, byte[] archivo);
    }
    public ListadoCursosAdapter(Context context, List<ListaCursoDTO> cursos, OnCursoClickListener listener) {
        this.context = context;
        this.cursos = cursos;
        this.onCursoClickListener = listener;
    }
}
