package com.pmo.sewaapp.models;

public class kategorimodel {
    String idkategori,namakategori;

    public kategorimodel(String idkategori, String namakategori) {
        this.idkategori = idkategori;
        this.namakategori = namakategori;
    }

    public kategorimodel() {
    }

    public String getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(String idkategori) {
        this.idkategori = idkategori;
    }

    public String getNamakategori() {
        return namakategori;
    }

    public void setNamakategori(String namakategori) {
        this.namakategori = namakategori;
    }
}
