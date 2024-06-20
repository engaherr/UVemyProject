package com.example.uvemyproject.viewmodels;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uvemyproject.BuscarCursos;
import com.example.uvemyproject.R;
import com.example.uvemyproject.TipoCursoAdapter;
import com.example.uvemyproject.databinding.ListaBuscarCursoItemBinding;
import com.example.uvemyproject.dto.EtiquetaDTO;
import com.example.uvemyproject.dto.TipoCursoDTO;

import java.util.List;


public class EtiquetaAdapter extends RecyclerView.Adapter<EtiquetaAdapter.EtiquetaViewHolder> {
    private final Context context;
    private EtiquetaAdapter.OnEtiquetasClickListener etiquetasClickListener;
    private List<EtiquetaDTO> etiquetas;

    public EtiquetaAdapter(Context context, List<EtiquetaDTO> value, OnEtiquetasClickListener etiquetasClickListener) {
        this.context = context;
        this.etiquetas = value;
        this.etiquetasClickListener = etiquetasClickListener;
    }

    public static class EtiquetaViewHolder extends RecyclerView.ViewHolder {
        ListaBuscarCursoItemBinding binding;
        TextView textViewTitulo;
        public EtiquetaViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListaBuscarCursoItemBinding.bind(itemView);
            textViewTitulo = binding.txtViewTitulo;
        }
    }
    @NonNull
    @Override
    public EtiquetaAdapter.EtiquetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_buscar_curso_item, parent, false);
        return new EtiquetaAdapter.EtiquetaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EtiquetaAdapter.EtiquetaViewHolder holder, int position) {
        EtiquetaDTO etiqueta = etiquetas.get(position);
        Log.d("Log","Nombre: "+etiqueta.getNombre());
        if (etiqueta != null) {
            holder.binding.txtViewTitulo.setText(etiqueta.getNombre());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etiquetasClickListener != null) {
                    etiquetasClickListener.OnEtiquetasClickListener(etiqueta.getIdEtiqueta(), etiqueta.getNombre());
                }
            }
        });
    }

    public interface OnEtiquetasClickListener {
        void OnEtiquetasClickListener(int idEtiqueta, String nombreEtiqueta);
    }
    @Override
    public int getItemCount() {
        return etiquetas.size();
    }
}
