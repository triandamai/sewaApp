package com.pmo.sewaapp.models;

public class transaksimodel {
    String idTransaksi,IDPENYEWA,IDBARANG,IDTOKO,alamat,buktipembayaran,status,jumlah,total,tanggal,harga,tglambil,tanggalkembali;

    public transaksimodel() {
    }

    public transaksimodel(String idTransaksi, String IDPENYEWA, String IDBARANG, String IDTOKO, String alamat, String buktipembayaran, String status, String jumlah, String total, String tanggal, String harga, String tglambil, String tanggalkembali) {
        this.idTransaksi = idTransaksi;
        this.IDPENYEWA = IDPENYEWA;
        this.IDBARANG = IDBARANG;
        this.IDTOKO = IDTOKO;
        this.alamat = alamat;
        this.buktipembayaran = buktipembayaran;
        this.status = status;
        this.jumlah = jumlah;
        this.total = total;
        this.tanggal = tanggal;
        this.harga = harga;
        this.tglambil = tglambil;
        this.tanggalkembali = tanggalkembali;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getIDPENYEWA() {
        return IDPENYEWA;
    }

    public void setIDPENYEWA(String IDPENYEWA) {
        this.IDPENYEWA = IDPENYEWA;
    }

    public String getIDBARANG() {
        return IDBARANG;
    }

    public void setIDBARANG(String IDBARANG) {
        this.IDBARANG = IDBARANG;
    }

    public String getIDTOKO() {
        return IDTOKO;
    }

    public void setIDTOKO(String IDTOKO) {
        this.IDTOKO = IDTOKO;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBuktipembayaran() {
        return buktipembayaran;
    }

    public void setBuktipembayaran(String buktipembayaran) {
        this.buktipembayaran = buktipembayaran;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTglambil() {
        return tglambil;
    }

    public void setTglambil(String tglambil) {
        this.tglambil = tglambil;
    }

    public String getTanggalkembali() {
        return tanggalkembali;
    }

    public void setTanggalkembali(String tanggalkembali) {
        this.tanggalkembali = tanggalkembali;
    }
}
