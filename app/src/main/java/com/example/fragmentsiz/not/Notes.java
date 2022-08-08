package com.example.fragmentsiz.not;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class Notes extends RealmObject {


    private String baslik;
    private String metin;
    private Date tarih;
    //private String tema;

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getMetin() {
        return metin;
    }

    public void setMetin(String metin) {
        this.metin = metin;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "baslik='" + baslik + '\'' +
                ", metin='" + metin + '\'' +
                ", tarih='" + tarih + '\'' +
                '}';
    }
}
