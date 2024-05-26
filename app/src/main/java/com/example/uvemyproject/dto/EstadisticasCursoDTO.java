package com.example.uvemyproject.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EstadisticasCursoDTO implements Parcelable {
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

    public EstadisticasCursoDTO() {
    }

    protected EstadisticasCursoDTO(Parcel in) {
        nombreCurso = in.readString();
        calificacion = in.readDouble();
        promedioComentarios = in.readDouble();
        estudiantesInscritos = in.readInt();
        etiquetasCoinciden = in.createStringArrayList();
        estudiantesCurso = in.createStringArrayList();
        clasesConComentarios = in.createTypedArrayList(ClaseEstadisticaDTO.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreCurso);
        dest.writeDouble(calificacion);
        dest.writeDouble(promedioComentarios);
        dest.writeInt(estudiantesInscritos);
        dest.writeStringList(etiquetasCoinciden);
        dest.writeStringList(estudiantesCurso);
        dest.writeTypedList(clasesConComentarios);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EstadisticasCursoDTO> CREATOR = new Creator<EstadisticasCursoDTO>() {
        @Override
        public EstadisticasCursoDTO createFromParcel(Parcel in) {
            return new EstadisticasCursoDTO(in);
        }

        @Override
        public EstadisticasCursoDTO[] newArray(int size) {
            return new EstadisticasCursoDTO[size];
        }
    };

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

}
