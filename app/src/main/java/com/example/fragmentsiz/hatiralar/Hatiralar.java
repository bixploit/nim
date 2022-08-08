package com.example.fragmentsiz.hatiralar;

import java.util.Date;

import io.realm.RealmObject;

public class Hatiralar extends RealmObject {
    private String metin;
    private String baslik;
    private String kisi;
    private Date tarih;
    private byte[] resim;

    public byte[] getResim() {
        return resim;
    }

    public void setResim(byte[] resim) {
        this.resim = resim;
    }

    public String getMetin() {
        return metin;
    }

    public void setMetin(String metin) {
        this.metin = metin;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String tarih) {
        this.baslik = tarih;
    }

    public String getKisi() {
        return kisi;
    }

    public void setKisi(String kisi) {
        this.kisi = kisi;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    @Override
    public String toString() {
        return "Hatiralar{" +
                "metin='" + metin + '\'' +
                ", baslik='" + baslik + '\'' +
                ", kisi='" + kisi + '\'' +
                ", tarih=" + tarih +
                ", resim=" + resim +
                '}';
    }
}
