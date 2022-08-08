package com.example.fragmentsiz.yapilacaklar;

import java.util.Date;

import io.realm.RealmObject;

public class Yapilacaklar extends RealmObject {

    private String baslik;
    private String hedefTarih;
    private Date tarih;
    private String metin;

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getHedefTarih() {
        return hedefTarih;
    }

    public void setHedefTarih(String hedefTarih) {
        this.hedefTarih = hedefTarih;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public String getMetin() {
        return metin;
    }

    public void setMetin(String metin) {
        this.metin = metin;
    }

    @Override
    public String toString() {
        return "Yapilacaklar{" +
                "baslik='" + baslik + '\'' +
                ", hedefTarih=" + hedefTarih +
                ", tarih=" + tarih +
                ", metin='" + metin + '\'' +
                '}';
    }
}
