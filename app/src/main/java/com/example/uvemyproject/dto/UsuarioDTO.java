package com.example.uvemyproject.dto;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UsuarioDTO implements Parcelable {
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String contrasena;
    //private byte[] imagen;
    private ImagenUsuarioDTO imagen;
    private int[] idsEtiqueta;
    private String jwt;
    private int esAdministrador;

    public UsuarioDTO(){
    }

    protected UsuarioDTO(Parcel in) {
        idUsuario = in.readInt();
        nombres = in.readString();
        apellidos = in.readString();
        correoElectronico = in.readString();
        //imagen = in.createByteArray();
        idsEtiqueta = in.createIntArray();
        jwt = in.readString();
        esAdministrador = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idUsuario);
        dest.writeString(nombres);
        dest.writeString(apellidos);
        dest.writeString(correoElectronico);
        //dest.writeByteArray(imagen);
        dest.writeIntArray(idsEtiqueta);
        dest.writeString(jwt);
        dest.writeInt(esAdministrador);
    }

    public static final Creator<UsuarioDTO> CREATOR = new Creator<UsuarioDTO>() {
        @Override
        public UsuarioDTO createFromParcel(Parcel in) {
            return new UsuarioDTO(in);
        }

        @Override
        public UsuarioDTO[] newArray(int size) {
            return new UsuarioDTO[size];
        }
    };

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    /*public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }*/

    /*public ImagenUsuario getImagen() {
        return imagen;
    }

    public void setImagen(ImagenUsuario imagen) {
        this.imagen = imagen;
    }*/

    public int[] getIdsEtiqueta() {
        return idsEtiqueta;
    }

    public void setIdsEtiqueta(int[] idsEtiqueta) {
        this.idsEtiqueta = idsEtiqueta;
    }

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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getEsAdministrador (){
        return esAdministrador;
    }

    //Set no colocado ya que no es usado

}
