package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EtiquetaDTO implements Parcelable {
    private int idEtiqueta;
    private String nombre;

    public EtiquetaDTO(int idEtiqueta, String nombre) {
        this.idEtiqueta = idEtiqueta;
        this.nombre = nombre;
    }

    protected EtiquetaDTO(Parcel in) {
        idEtiqueta = in.readInt();
        nombre = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idEtiqueta);
        dest.writeString(nombre);
    }

    public static final Creator<EtiquetaDTO> CREATOR = new Creator<EtiquetaDTO>() {
        @Override
        public EtiquetaDTO createFromParcel(Parcel in) {
            return new EtiquetaDTO(in);
        }

        @Override
        public EtiquetaDTO[] newArray(int size) {
            return new EtiquetaDTO[size];
        }
    };

    public int getIdEtiqueta() {
        return idEtiqueta;
    }

    public void setIdEtiqueta(int idEtiqueta) {
        this.idEtiqueta = idEtiqueta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
