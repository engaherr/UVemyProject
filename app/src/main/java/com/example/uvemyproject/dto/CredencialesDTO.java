package com.example.uvemyproject.dto;

public class CredencialesDTO {
    private String correoElectronico;
    private String contrasena;

    public CredencialesDTO(String correoElectronico, String contrasena) {
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }
}
