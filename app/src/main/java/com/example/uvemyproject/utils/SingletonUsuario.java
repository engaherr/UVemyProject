package com.example.uvemyproject.utils;

import java.util.List;

public class SingletonUsuario {
    private static String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3ByaW1hcnlzaWQiOjYsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6ImVucmlxdWVAZWplbXBsby5jb20iLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9naXZlbm5hbWUiOiJ1c3VhcmlvRWplbXBsbyIsImlzcyI6IlVWZW15U2Vydmlkb3JKV1QiLCJhdWQiOiJVc3Vhcmlvc1VWZW15SldUIiwiaWF0IjoxNzE4MzQ2NjYxLCJleHAiOjE3MTgzNDc4NjF9.P-LSUJHdHMCEAV7zAYTj_j_hf4AjvpTVouzY23g9nDI";
    private static int idUsuario = 1;

    private static String nombres;
    private static String apellidos;
    private static String correoElectronico;
    private static int[] idsEtiqueta;

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
}
