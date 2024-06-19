package com.example.uvemyproject.dto;

public class ListaDocumentoDTO {
    private int idDocumento;
    private ListaArchivoDTO archivo;

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public ListaArchivoDTO getArchivo() {
        return archivo;
    }

    public void setArchivo(ListaArchivoDTO archivo) {
        this.archivo = archivo;
    }
}
