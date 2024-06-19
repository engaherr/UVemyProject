package com.example.uvemyproject.dto;

import java.util.List;

public class ListaCursoDTO {
    private int idCurso;
    private String titulo;
    private List<ListaDocumentoDTO> documentos;

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<ListaDocumentoDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<ListaDocumentoDTO> documentos) {
        this.documentos = documentos;
    }
}




