package com.pmo.sewaapp.models;

public class tokomodel {
    String idtoko,namatoko,pemiliktoko,bannertoko;

    public tokomodel() {
    }

    public tokomodel(String idtoko, String namatoko, String pemiliktoko, String bannertoko) {
        this.idtoko = idtoko;
        this.namatoko = namatoko;
        this.pemiliktoko = pemiliktoko;
        this.bannertoko = bannertoko;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public String getPemiliktoko() {
        return pemiliktoko;
    }

    public void setPemiliktoko(String pemiliktoko) {
        this.pemiliktoko = pemiliktoko;
    }

    public String getBannertoko() {
        return bannertoko;
    }

    public void setBannertoko(String bannertoko) {
        this.bannertoko = bannertoko;
    }
}
