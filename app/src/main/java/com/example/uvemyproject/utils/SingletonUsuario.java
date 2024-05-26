package com.example.uvemyproject.utils;

public class SingletonUsuario {
    private static String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3ByaW1hcnlzaWQiOjgsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6InZAZ21haWwuY29tIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvZ2l2ZW5uYW1lIjoidiIsImlzcyI6IlVWZW15U2Vydmlkb3JKV1QiLCJhdWQiOiJVc3Vhcmlvc1VWZW15SldUIiwiaWF0IjoxNzE2NzQxNzA5LCJleHAiOjE3MTY3ODQ5MDl9.BgYt8M60UVPN4bUgQ5q4QOZTTA7iQ0oPuzStwqGg_aw";
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
