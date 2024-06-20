package com.example.uvemyproject.dto;

public class TipoCursoDTO {
    public int idTipoCurso;

    public String nombre;
    public TipoCursoDTO(int idTipoCurso, String nombre) {
        this.idTipoCurso = idTipoCurso;
        this.nombre = nombre;
    }

    public int getIdTipoCurso() {
        return idTipoCurso;
    }

    public void setIdTipoCurso(int idTipoCurso) {
        this.idTipoCurso = idTipoCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @Override
    public String toString() {
        return nombre;
    }
}
