package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ComentarioEnvioDTO implements Parcelable {
    private final int idClase;
    private final int idUsuario;
    private final String descripcion;
    private final int respondeAComentario;

    public ComentarioEnvioDTO(int idClase, int idUsuario, String descripcion, int respondeAComentario) {
        this.idClase = idClase;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.respondeAComentario = respondeAComentario;
    }

    protected ComentarioEnvioDTO(Parcel in) {
        idClase = in.readInt();
        idUsuario = in.readInt();
        descripcion = in.readString();
        respondeAComentario = in.readInt();
    }

    public static final Creator<ComentarioEnvioDTO> CREATOR = new Creator<ComentarioEnvioDTO>() {
        @Override
        public ComentarioEnvioDTO createFromParcel(Parcel in) {
            return new ComentarioEnvioDTO(in);
        }

        @Override
        public ComentarioEnvioDTO[] newArray(int size) {
            return new ComentarioEnvioDTO[size];
        }
    };

    public int getIdClase() {
        return idClase;
    }

    public int getRespondeAComentario() {
        return respondeAComentario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idClase);
        dest.writeString(descripcion);
        dest.writeInt(respondeAComentario);
    }

}
