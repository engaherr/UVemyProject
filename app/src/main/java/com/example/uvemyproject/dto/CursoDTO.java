package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CursoDTO implements Parcelable {
    private int idCurso;
    private String Titulo;
    private String descripcion;
    private String objetivos;
    private String requisitos;
    private int idUsuario;
    private List<EtiquetaDTO> etiquetas;
    private String calificacion;
    private String rol;
    private String profesor;
    private List<ClaseDTO> clases;
    private int idDocumento;
    public CursoDTO(){

    }
    protected CursoDTO(Parcel in) {
        idCurso = in.readInt();
        Titulo = in.readString();
        descripcion = in.readString();
        objetivos = in.readString();
        requisitos = in.readString();
        idUsuario = in.readInt();
        etiquetas = in.createTypedArrayList(EtiquetaDTO.CREATOR);
        calificacion = in.readString();
        rol = in.readString();
        profesor = in.readString();
        clases = in.createTypedArrayList(ClaseDTO.CREATOR);
        idDocumento = in.readInt();
        int archivoLength = in.readInt();
        archivo = new byte[archivoLength];
        in.readByteArray(archivo);
    }

    public static final Creator<CursoDTO> CREATOR = new Creator<CursoDTO>() {
        @Override
        public CursoDTO createFromParcel(Parcel in) {
            return new CursoDTO(in);
        }

        @Override
        public CursoDTO[] newArray(int size) {
            return new CursoDTO[size];
        }
    };

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

    private byte[] archivo;
    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
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

    public List<EtiquetaDTO> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<EtiquetaDTO> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public List<ClaseDTO> getClases() {
        return clases;
    }

    public void setClases(List<ClaseDTO> clases) {
        this.clases = clases;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idCurso);
        dest.writeString(Titulo);
        dest.writeString(descripcion);
        dest.writeString(objetivos);
        dest.writeString(requisitos);
        dest.writeInt(idUsuario);
        dest.writeTypedList(etiquetas);
        dest.writeString(calificacion);
        dest.writeString(rol);
        dest.writeString(profesor);
        dest.writeTypedList(clases);
        dest.writeInt(idDocumento);
        dest.writeByteArray(archivo);
    }
}
