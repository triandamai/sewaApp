package com.pmo.sewaapp.models;

public class barangmodel {
    String idbarang , kategori,idtoko,stoktersedia,stokasli,hargasewa,gambar,nama;

    public barangmodel() {

    }

    public barangmodel(String idbarang, String kategori, String idtoko, String stoktersedia, String stokasli, String hargasewa, String gambar, String nama) {
        this.idbarang = idbarang;
        this.kategori = kategori;
        this.idtoko = idtoko;
        this.stoktersedia = stoktersedia;
        this.stokasli = stokasli;
        this.hargasewa = hargasewa;
        this.gambar = gambar;
        this.nama = nama;
    }

    public String getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public String getStoktersedia() {
        return stoktersedia;
    }

    public void setStoktersedia(String stoktersedia) {
        this.stoktersedia = stoktersedia;
    }

    public String getStokasli() {
        return stokasli;
    }

    public void setStokasli(String stokasli) {
        this.stokasli = stokasli;
    }

    public String getHargasewa() {
        return hargasewa;
    }

    public void setHargasewa(String hargasewa) {
        this.hargasewa = hargasewa;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}

