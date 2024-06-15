package com.example.uvemyproject.dto;

import java.util.ArrayList;
import java.util.List;

public class CursoDTO {
    private int idCurso;
    private String descripcion;
    private String objetivos;
    private String requisitos;
    private int idUsuario;
    private List<Etiqueta> etiquetas;
    private double calificacion;
    private String rol;
    private String profesor;
    private List<ClaseDTO> clases;

}
