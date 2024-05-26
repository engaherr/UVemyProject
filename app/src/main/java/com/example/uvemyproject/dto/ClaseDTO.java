package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.Objects;

import kotlin.jvm.Transient;

public class ClaseDTO implements Parcelable {

    private int idClase;
    private String descripcion;
    private String nombre;
    private int idCurso;
    @Json(name = "documentosId")
    private int[] documentosId;
    @Json(name = "videoId")
    private int videoId;
    private transient ArrayList<DocumentoDTO> documentos;
    private transient DocumentoDTO videoDocumento;

    public ClaseDTO() {
    }

    public ClaseDTO(int idClase, String descripcion, String nombre) {
        this.idClase = idClase;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    public ClaseDTO(int idClase, String descripcion, String nombre, int idCurso, int[] documentosId, int videoId, ArrayList<DocumentoDTO> documentos, DocumentoDTO videoDocumento) {
        this.idClase = idClase;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.idCurso = idCurso;
        this.documentosId = documentosId;
        this.videoId = videoId;
        this.documentos = documentos;
        this.videoDocumento = videoDocumento;
    }

    protected ClaseDTO(Parcel in) {
        idClase = in.readInt();
        descripcion = in.readString();
        nombre = in.readString();
        idCurso = in.readInt();
        documentosId = in.createIntArray();
        videoId = in.readInt();
        documentos = in.createTypedArrayList(DocumentoDTO.CREATOR);
        videoDocumento = in.readParcelable(DocumentoDTO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idClase);
        dest.writeString(descripcion);
        dest.writeString(nombre);
        dest.writeInt(idCurso);
        dest.writeIntArray(documentosId);
        dest.writeInt(videoId);
        dest.writeTypedList(documentos);
        dest.writeParcelable(videoDocumento, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClaseDTO> CREATOR = new Creator<ClaseDTO>() {
        @Override
        public ClaseDTO createFromParcel(Parcel in) {
            return new ClaseDTO(in);
        }

        @Override
        public ClaseDTO[] newArray(int size) {
            return new ClaseDTO[size];
        }
    };

    public DocumentoDTO getVideoDocumento() {
        return videoDocumento;
    }

    public void setVideoDocumento(DocumentoDTO videoDocumento) {
        this.videoDocumento = videoDocumento;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
    public int[] getDocumentosId() {
        return documentosId;
    }

    public void setDocumentosId(int[] documentosId) {
        this.documentosId = documentosId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public ArrayList<DocumentoDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(ArrayList<DocumentoDTO> documentos) {
        this.documentos = documentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaseDTO clase = (ClaseDTO) o;
        return idClase == clase.idClase && Objects.equals(descripcion, clase.descripcion) && Objects.equals(nombre, clase.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClase, descripcion, nombre);
    }

}
