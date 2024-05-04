package com.example.uvemyproject;

import java.util.Objects;

public class Clase {
    private int idClase;
    private String descripcion;
    private String nombre;

    public Clase(int idClase, String descripcion, String nombre) {
        this.idClase = idClase;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clase clase = (Clase) o;
        return idClase == clase.idClase && Objects.equals(descripcion, clase.descripcion) && Objects.equals(nombre, clase.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClase, descripcion, nombre);
    }
}
