package com.example.fragmentsiz.not;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmentsiz.R;
import com.example.fragmentsiz.hatiralar.HatiralarListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class NoteKayitActivity extends AppCompatActivity {

    SharedPreferences.Editor editor ;
    SharedPreferences ayarlar ;
    String temaRengi;
    Integer position;
    Integer okunacakKayitPozisyonu;
    RealmConfiguration configurationNote;
    ActionBar actionBar;
    Realm realm;
    EditText metinText,baslikText;
    TextView tarihText;
    Intent intent;
    Date tarih ;
    String info;
    FloatingActionButton fabEkle,fabGuncelle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ayarlar = getSharedPreferences("Tema", Context.MODE_PRIVATE);
        editor=ayarlar.edit();
        temaRengi=ayarlar.getString("tema","");
        if (temaRengi.matches("koyu")) {
            setTheme(R.style.darktheme);
        }
        else
        {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_kayit);

        fabEkle=findViewById(R.id.fabNoteEkle);
        fabGuncelle=findViewById(R.id.fabNoteGuncelle);
        if (temaRengi.matches("koyu")) {
            fabEkle.setImageResource(R.drawable.ic_add_dark);
            fabGuncelle.setImageResource(R.drawable.ic_save_dark);
        }
        else
        {
            fabEkle.setImageResource(R.drawable.ic_add_dark);
            fabGuncelle.setImageResource(R.drawable.ic_save_dark);
        }
        tanimla();
        realmTanimla();
        yeniVeyaEskikayit();
    }

    public void yeniVeyaEskikayit(){
        info=intent.getStringExtra("info");
        position =intent.getIntExtra("noteId",0);
        if (info.matches("old"))
        {
            okunacakKayitPozisyonu=position;
            kayitOku(position);
            fabGuncelle.setVisibility(View.VISIBLE);
            fabEkle.setVisibility(View.INVISIBLE);
        }
        else if (info.matches("new"))
        {
            fabGuncelle.setVisibility(View.INVISIBLE);
            fabEkle.setVisibility(View.VISIBLE);
        }
    }
    public void kayitOku(final int position){
        RealmResults<Notes> notes=realm.where(Notes.class).findAll();
        baslikText.setText(notes.get(position).getBaslik()) ;
        metinText.setText(notes.get(position).getMetin());
        actionBar.setTitle(notes.get(position).getBaslik());
        actionBar.setSubtitle(notes.get(position).getTarih().toString().substring(0,19)) ;
    }
    public void guncelle(int position){
        RealmResults<Notes>gelenNote=realm.where(Notes.class).findAll();
        final Notes notes=gelenNote.get(position);
        final String baslik=baslikText.getText().toString();
        final String metin=metinText.getText().toString();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                notes.setBaslik(baslik);
                notes.setMetin(metin);
                notes.setTarih(tarih);
                Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                baslikText.setText("");
                metinText.setText("");
            }
        });
    }
    public void tanimla(){
        actionBar = getSupportActionBar();
        tarih = Calendar.getInstance().getTime();
        metinText=findViewById(R.id.metinText);
        baslikText=findViewById(R.id.baslikText);
        tarihText=findViewById(R.id.tarihText);
        intent =getIntent();
    }
    public void realmTanimla(){
        Realm.init(getApplicationContext());
        configurationNote= new RealmConfiguration.Builder().name("Note").build();
        Realm.setDefaultConfiguration(configurationNote);
        realm=Realm.getDefaultInstance();
    }

    public void ekle(final String baslik, final String metin){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notes notes=realm.createObject(Notes.class);
                notes.setBaslik(baslik);
                notes.setMetin(metin);
                notes.setTarih(tarih);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                baslikText.setText("");
                metinText.setText("");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "Başarısız", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void fabNoteEkle(View view) {

        String baslik=baslikText.getText().toString();
        String metin=metinText.getText().toString();
        ekle(baslik,metin);
    }
    public void fabNoteGuncelle(View view){

        guncelle(okunacakKayitPozisyonu);
    }
}