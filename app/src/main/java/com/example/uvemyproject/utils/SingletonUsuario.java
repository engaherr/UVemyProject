package com.example.uvemyproject.utils;

public class SingletonUsuario {
    private static String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3ByaW1hcnlzaWQiOjEsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6Im1lbHVzQGdtYWlsLmNvbSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2dpdmVubmFtZSI6InMiLCJpc3MiOiJVVmVteVNlcnZpZG9ySldUIiwiYXVkIjoiVXN1YXJpb3NVVmVteUpXVCIsImlhdCI6MTcxNzk4NjA5NywiZXhwIjoxNzE4MDU5Mjk3fQ.owCoQZDe3Ua9kjJDx6vVnB2Igq_--DEmUvrcBDb0xng";
    private static int idUsuario = 1;

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
}
