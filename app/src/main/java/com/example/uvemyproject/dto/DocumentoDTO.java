package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.io.File;
import java.util.Objects;

public class DocumentoDTO implements Parcelable {
    private int idDocumento;
    private String nombre;
    private int idClase;
    private int idTipoArchivo;
    private transient File file;
    private transient byte[] documento;
    public DocumentoDTO() {
    }

    public DocumentoDTO(int idDocumento, String nombre, int idClase, int idTipoArchivo, File file, byte[] documento) {
        this.idDocumento = idDocumento;
        this.nombre = nombre;
        this.idClase = idClase;
        this.idTipoArchivo = idTipoArchivo;
        this.file = file;
        this.documento = documento;
    }

    protected DocumentoDTO(Parcel in) {
        idDocumento = in.readInt();
        nombre = in.readString();
        idClase = in.readInt();
        idTipoArchivo = in.readInt();
        String filePath = in.readString();
        if (filePath != null) {
            file = new File(filePath);
        }
        int documentoLength = in.readInt();
        if (documentoLength > 0) {
            documento = new byte[documentoLength];
            in.readByteArray(documento);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idDocumento);
        dest.writeString(nombre);
        dest.writeInt(idClase);
        dest.writeInt(idTipoArchivo);
        dest.writeString(file != null ? file.getAbsolutePath() : null);
        dest.writeInt(documento != null ? documento.length : 0);
        if (documento != null && documento.length > 0) {
            dest.writeByteArray(documento);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DocumentoDTO> CREATOR = new Creator<DocumentoDTO>() {
        @Override
        public DocumentoDTO createFromParcel(Parcel in) {
            return new DocumentoDTO(in);
        }

        @Override
        public DocumentoDTO[] newArray(int size) {
            return new DocumentoDTO[size];
        }
    };

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
