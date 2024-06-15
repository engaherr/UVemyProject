package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class UsuarioRegistroDTO extends UsuarioDTO implements Parcelable {

    private String codigoVerificacion;

    public UsuarioRegistroDTO() {
        super();
    }

    protected UsuarioRegistroDTO(Parcel in) {
        super(in);
        codigoVerificacion = in.readString();
    }

    public static final Creator<UsuarioRegistroDTO> CREATOR = new Creator<UsuarioRegistroDTO>() {
        @Override
        public UsuarioRegistroDTO createFromParcel(Parcel in) {
            return new UsuarioRegistroDTO(in);
        }

        @Override
        public UsuarioRegistroDTO[] newArray(int size) {
            return new UsuarioRegistroDTO[size];
        }
    };

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(codigoVerificacion);
    }

}
