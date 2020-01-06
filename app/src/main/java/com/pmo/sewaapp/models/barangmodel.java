package com.pmo.sewaapp.models;

public class barangmodel {
    String idbarang , kategori,idtoko,stoktersedia,stokasli,hargasewa;

    public barangmodel() {

    }

    public barangmodel(String idbarang, String kategori, String idtoko, String stoktersedia, String stokasli, String hargasewa) {
        this.idbarang = idbarang;
        this.kategori = kategori;
        this.idtoko = idtoko;
        this.stoktersedia = stoktersedia;
        this.stokasli = stokasli;
        this.hargasewa = hargasewa;
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
}

