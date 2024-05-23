package com.example.uvemyproject.dto;

import java.util.Objects;

public class ClaseEstadisticaDTO {
    private String nombre;
    private int cantidadComentarios;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaseEstadisticaDTO that = (ClaseEstadisticaDTO) o;
        return cantidadComentarios == that.cantidadComentarios && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, cantidadComentarios);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadComentarios() {
        return cantidadComentarios;
    }

    public void setCantidadComentarios(int cantidadComentarios) {
        this.cantidadComentarios = cantidadComentarios;
    }
}
