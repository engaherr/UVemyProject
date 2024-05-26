package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Objects;

public class ClaseEstadisticaDTO implements Parcelable {
    private String nombre;
    private int cantidadComentarios;

    public ClaseEstadisticaDTO() {
    }

    protected ClaseEstadisticaDTO(Parcel in) {
        nombre = in.readString();
        cantidadComentarios = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeInt(cantidadComentarios);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClaseEstadisticaDTO> CREATOR = new Creator<ClaseEstadisticaDTO>() {
        @Override
        public ClaseEstadisticaDTO createFromParcel(Parcel in) {
            return new ClaseEstadisticaDTO(in);
        }

        @Override
        public ClaseEstadisticaDTO[] newArray(int size) {
            return new ClaseEstadisticaDTO[size];
        }
    };
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaseEstadisticaDTO that = (ClaseEstadisticaDTO) o;
        return cantidadComentarios == that.cantidadComentarios && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, cantidadComentarios);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadComentarios() {
        return cantidadComentarios;
    }

    public void setCantidadComentarios(int cantidadComentarios) {
        this.cantidadComentarios = cantidadComentarios;
    }
}
