package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class ComentarioDTO implements Parcelable {
    private final int idComentario;
    private final int idClase;
    private final String nombreUsuario;
    private final String descripcion;
    private final List<ComentarioDTO> respuestas;

    protected ComentarioDTO(Parcel in) {
        idComentario = in.readInt();
        idClase = in.readInt();
        nombreUsuario = in.readString();
        descripcion = in.readString();
        respuestas = in.createTypedArrayList(ComentarioDTO.CREATOR);
    }

    public static final Creator<ComentarioDTO> CREATOR = new Creator<ComentarioDTO>() {
        @Override
        public ComentarioDTO createFromParcel(Parcel in) {
            return new ComentarioDTO(in);
        }

        @Override
        public ComentarioDTO[] newArray(int size) {
            return new ComentarioDTO[size];
        }
    };

    public int getIdComentario() {
        return idComentario;
    }

    public int getIdClase() {
        return idClase;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<ComentarioDTO> getRespuestas() {
        return respuestas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComentarioDTO that = (ComentarioDTO) o;
        return idComentario == that.idComentario && idClase == that.idClase && Objects.equals(nombreUsuario, that.nombreUsuario) && Objects.equals(descripcion, that.descripcion) && Objects.equals(respuestas, that.respuestas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idComentario, idClase, nombreUsuario, descripcion, respuestas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idComentario);
        dest.writeInt(idClase);
        dest.writeString(nombreUsuario);
        dest.writeString(descripcion);
    }
}
