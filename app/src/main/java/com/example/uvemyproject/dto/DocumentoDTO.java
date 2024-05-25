package com.example.uvemyproject.dto;

import com.squareup.moshi.Json;

import java.io.File;
import java.util.Objects;

public class DocumentoDTO {
    private int idDocumento;
    private String nombre;
    private int idClase;
    private int idTipoArchivo;
    private transient File file;
    private transient byte[] documento;
    public File getFile() {
        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentoDTO that = (DocumentoDTO) o;
        return idDocumento == that.idDocumento && idClase == that.idClase && idTipoArchivo == that.idTipoArchivo && Objects.equals(nombre, that.nombre) && Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocumento, nombre, idClase, idTipoArchivo, file);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public DocumentoDTO() {
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int getIdTipoArchivo() {
        return idTipoArchivo;
    }

    public void setIdTipoArchivo(int idTipoArchivo) {
        this.idTipoArchivo = idTipoArchivo;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }
}
