package com.example.uvemyproject.utils;

import java.util.List;

public class SingletonUsuario {
    private static String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3ByaW1hcnlzaWQiOjEsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6Im1lbHVzQGdtYWlsLmNvbSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2dpdmVubmFtZSI6InMiLCJpc3MiOiJVVmVteVNlcnZpZG9ySldUIiwiYXVkIjoiVXN1YXJpb3NVVmVteUpXVCIsImlhdCI6MTcxNjc4NTAwNiwiZXhwIjoxNzE2ODI4MjA2fQ.iyxahMDFPV390momxo-LO_8wdXPicpZu9dZddFMl-uI";
    private static int idUsuario = 1;

    private static String nombres;
    private static String apellidos;
    private static String correoElectronico;
    private static int[] idsEtiqueta;
    private static int esAdministrador;

    public static String getJwt() {
        return jwt;
    }

    public static void setJwt(String jwt) {
        SingletonUsuario.jwt = jwt;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(int idUsuario) {
        SingletonUsuario.idUsuario = idUsuario;
    }

    public static String getNombres() {
        return nombres;
    }

    public static void setNombres(String nombres) {
        SingletonUsuario.nombres = nombres;
    }

    public static String getApellidos() {
        return apellidos;
    }

    public static void setApellidos(String apellidos) {
        SingletonUsuario.apellidos = apellidos;
    }

    public static String getCorreoElectronico() {
        return correoElectronico;
    }

    public static void setCorreoElectronico(String correoElectronico) {
        SingletonUsuario.correoElectronico = correoElectronico;
    }

    public static int[] getIdsEtiqueta() {
        return idsEtiqueta;
    }

    public static void setIdsEtiqueta(int[] idsEtiqueta) {
        SingletonUsuario.idsEtiqueta = idsEtiqueta;
    }

    public static int getEsAdministrador(){
        return esAdministrador;
    }

    public static void setEsAdministrador(int esAdministrador){
        SingletonUsuario.esAdministrador = esAdministrador;
    }
}
