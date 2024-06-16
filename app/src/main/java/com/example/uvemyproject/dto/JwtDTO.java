package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class JwtDTO implements Parcelable {
    private String jwt;

    public JwtDTO(String jwt) {
        this.jwt = jwt;
    }

    protected JwtDTO(Parcel in) {
        jwt = in.readString();
    }

    public static final Creator<JwtDTO> CREATOR = new Creator<JwtDTO>() {
        @Override
        public JwtDTO createFromParcel(Parcel in) {
            return new JwtDTO(in);
        }

        @Override
        public JwtDTO[] newArray(int size) {
            return new JwtDTO[size];
        }
    };

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(jwt);
    }
}
