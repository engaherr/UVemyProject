package com.example.uvemyproject.dto;

import java.util.ArrayList;
import java.util.List;

public class CursoDTO {
    private int idCurso;
    private String nombre;
    private String rol;
    private List<ClaseDTO> clases;

    public List<ClaseDTO> getClases() {
        return clases;
    }

    public void setClases(List<ClaseDTO> clases) {
        this.clases = clases;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
