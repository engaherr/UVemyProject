package com.example.uvemyproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.databinding.ListaBuscarCursoItemBinding;
import com.example.uvemyproject.databinding.ListaCursosItemBinding;
import com.example.uvemyproject.dto.ListaCursoDTO;
import com.example.uvemyproject.dto.ListaDocumentoDTO;
import com.example.uvemyproject.dto.TipoCursoDTO;

import java.util.List;

public class TipoCursoAdapter extends RecyclerView.Adapter<TipoCursoAdapter.TipoCursoViewHolder> {
    private final Context context;
    private OntipoCursoClickListener ontipoCursoClickListener;
    private List<TipoCursoDTO> tiposCursos;

    public static class TipoCursoViewHolder extends RecyclerView.ViewHolder {
        ListaBuscarCursoItemBinding binding;
        TextView textViewTitulo;

        public TipoCursoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListaBuscarCursoItemBinding.bind(itemView);
            textViewTitulo = binding.txtViewTitulo;
        }
    }

    @Override
    public TipoCursoAdapter.TipoCursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_buscar_curso_item, parent, false);
        return new TipoCursoAdapter.TipoCursoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TipoCursoAdapter.TipoCursoViewHolder holder, int position) {
        TipoCursoDTO curso = tiposCursos.get(position);
        Log.d("Log","Nombre: "+curso.getNombre());
        if (curso != null) {
            holder.binding.txtViewTitulo.setText(curso.getNombre());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ontipoCursoClickListener != null) {
                    ontipoCursoClickListener.OntipoCursoClickListener(curso.getIdTipoCurso(), curso.getNombre());
                }
            }
        });
    }
    public interface OntipoCursoClickListener {
        void OntipoCursoClickListener(int idTipoCurso, String nombreCurso);
    }

    public int getItemCount() {
        return tiposCursos.size();
    }
    public TipoCursoAdapter(Context context, List<TipoCursoDTO> tiposCursos, OntipoCursoClickListener ontipoCursoClickListener) {
        this.context = context;
        this.tiposCursos = tiposCursos;
        this.ontipoCursoClickListener = ontipoCursoClickListener;
    }
}
