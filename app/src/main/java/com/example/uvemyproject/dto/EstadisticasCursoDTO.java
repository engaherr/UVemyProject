package com.example.uvemyproject.dto;

import com.squareup.moshi.Json;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class EstadisticasCursoDTO {
    @Json(name="nombre")
    private String nombreCurso;
    @Json(name="calificacionCurso")
    private double calificacion;
    private double promedioComentarios;
    private int estudiantesInscritos;
    private List<String> etiquetasCoinciden;
    @Json(name="estudiantes")
    private List<String> estudiantesCurso;
    @Json(name="clases")
    private List<ClaseEstadisticaDTO> clasesConComentarios;

    public List<ClaseEstadisticaDTO> getClasesConComentarios() {
        return clasesConComentarios;
    }

    public void setClasesConComentarios(List<ClaseEstadisticaDTO> clasesConComentarios) {
        this.clasesConComentarios = clasesConComentarios;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public double getPromedioComentarios() {
        return promedioComentarios;
    }

    public void setPromedioComentarios(double promedioComentarios) {
        this.promedioComentarios = promedioComentarios;
    }

    public int getEstudiantesInscritos() {
        return estudiantesInscritos;
    }

    public void setEstudiantesInscritos(int estudiantesInscritos) {
        this.estudiantesInscritos = estudiantesInscritos;
    }

    public List<String> getEtiquetasCoinciden() {
        return etiquetasCoinciden;
    }

    public void setEtiquetasCoinciden(List<String> etiquetasCoinciden) {
        this.etiquetasCoinciden = etiquetasCoinciden;
    }

    public List<String> getEstudiantesCurso() {
        return estudiantesCurso;
    }

    public void setEstudiantesCurso(List<String> estudiantesCurso) {
        this.estudiantesCurso = estudiantesCurso;
    }

    public String getCalificacionString(){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(calificacion);
    }

    public String getPromedioComentariosString(){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(promedioComentarios);
    }
    public String getEstudiantesInscritosString(){
        return String.valueOf(estudiantesInscritos);
    }

    public String getEtiquetasString(){
        String joined = String.join(", ", etiquetasCoinciden);
        return joined;
    }

    public static class ClaseEstadisticaDTO {
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
}
