package com.pmo.sewaapp.models;

public class usermodel {
    public String uid,nama,nohp,alamat,email,level;
    public long lastOnline;

    public usermodel() {
    }

    public usermodel(String uid, String nama, String nohp, String alamat, String email, String level, long lastOnline) {
        this.uid = uid;
        this.nama = nama;
        this.nohp = nohp;
        this.alamat = alamat;
        this.email = email;
        this.level = level;
        this.lastOnline = lastOnline;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }
}
