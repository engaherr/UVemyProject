package com.example.uvemyproject.dto;

import java.util.List;

public class UsuarioBusquedaDTO {
    private List<UsuarioDTO> data;
    private int total;
    private int totalPages;
    private int currentPage;

    public List<UsuarioDTO> getData() {
        return data;
    }

    public void setData(List<UsuarioDTO> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public String toString() {
        return "UsuarioBusquedaDTO{" +
                "data=" + data +
                ", total=" + total +
                ", totalPages=" + totalPages +
                ", currentPage=" + currentPage +
                '}';
    }
}
