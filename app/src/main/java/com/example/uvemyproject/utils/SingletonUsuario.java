package com.example.uvemyproject.utils;

public class SingletonUsuario {
    private static String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3ByaW1hcnlzaWQiOjEsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6Im1lbHVzQGdtYWlsLmNvbSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2dpdmVubmFtZSI6InMiLCJpc3MiOiJVVmVteVNlcnZpZG9ySldUIiwiYXVkIjoiVXN1YXJpb3NVVmVteUpXVCIsImlhdCI6MTcxNjY2MjYwMywiZXhwIjoxNzE2NzA1ODAzfQ.8ce_-FhfCWnT3pWuxMFpP9-gDKK0heLU4MciNW-AJjY";
    public static String getJwt() {
        return jwt;
    }

    public static void setJwt(String jwt) {
        SingletonUsuario.jwt = jwt;
    }
}
