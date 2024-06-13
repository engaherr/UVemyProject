package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CrearCursoDTO implements Parcelable {
    public int idCurso;
    public String titulo;
    public String descripcion;
    public String objetivos;
    public String requisitos;
    public int idUsuario;
    public List<Integer> etiquetas;
    public int idDocumento;
    public byte[] archivo;

    public CrearCursoDTO(){

    }
    protected CrearCursoDTO(Parcel in) {
        idCurso = in.readInt();
        titulo = in.readString();
        descripcion = in.readString();
        objetivos = in.readString();
        requisitos = in.readString();
        idUsuario = in.readInt();
        etiquetas = new ArrayList<>();
        int etiquetasLength = in.readInt();
        for (int i = 0; i < etiquetasLength; i++) {
            etiquetas.add(in.readInt());
        }
        idDocumento = in.readInt();
        archivo = in.createByteArray();
    }

    public static final Creator<CrearCursoDTO> CREATOR = new Creator<CrearCursoDTO>() {
        @Override
        public CrearCursoDTO createFromParcel(Parcel in) {
            return new CrearCursoDTO(in);
        }

        @Override
        public CrearCursoDTO[] newArray(int size) {
            return new CrearCursoDTO[size];
        }
    };

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Integer> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<Integer> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idCurso);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(objetivos);
        dest.writeString(requisitos);
        dest.writeInt(idUsuario);
        if (etiquetas != null) {
            dest.writeIntArray(etiquetas.stream().mapToInt(Integer::intValue).toArray());
        } else {
            dest.writeInt(0); // Indica que no hay etiquetas
        }
        dest.writeInt(idDocumento);
        dest.writeByteArray(archivo);
    }
}
