package com.example.uvemyproject.dto;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.Objects;

import kotlin.jvm.Transient;

public class ClaseDTO {

    private int idClase;
    private String descripcion;
    private String nombre;
    private int idCurso;
    @Json(name = "documentosId")
    private int[] documentosId;
    @Json(name = "videoId")
    private int videoId;
    private transient ArrayList<DocumentoDTO> documentos;

    public ClaseDTO() {
    }

    public ClaseDTO(int idClase, String descripcion, String nombre) {
        this.idClase = idClase;
        this.descripcion = descripcion;
        this.nombre = nombre;
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
