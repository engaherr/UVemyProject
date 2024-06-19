package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagenUsuarioDTO implements Parcelable {
    private byte[] data;
    private String type;

    public ImagenUsuarioDTO() {
    }

    protected ImagenUsuarioDTO(Parcel in) {
        data = in.createByteArray();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(data);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImagenUsuarioDTO> CREATOR = new Creator<ImagenUsuarioDTO>() {
        @Override
        public ImagenUsuarioDTO createFromParcel(Parcel in) {
            return new ImagenUsuarioDTO(in);
        }

        @Override
        public ImagenUsuarioDTO[] newArray(int size) {
            return new ImagenUsuarioDTO[size];
        }
    };

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
