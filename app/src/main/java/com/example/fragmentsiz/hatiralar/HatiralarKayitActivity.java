package com.example.fragmentsiz.hatiralar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmentsiz.MainActivity;
import com.example.fragmentsiz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class HatiralarKayitActivity extends AppCompatActivity {

    SharedPreferences.Editor editor ;
    SharedPreferences ayarlar ;
    String temaRengi;
    ImageView imageView;
    Bitmap selectImage;
    Integer position;
    Integer okunacakKayitPozisyonu;
    MenuInflater menuInflater2;
    ActionBar actionBar;
    Realm realm;
    EditText metinText,baslikText,kisiText;
    Intent intent;
    Date tarih ;
    RealmConfiguration configurationHatira;
    String info;
    FloatingActionButton fabHatiraEkle,fabHatiraGuncelle;

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
        setContentView(R.layout.activity_hatiralar_kayit);
        fabHatiraEkle=findViewById(R.id.fabHatiraEkle);
        fabHatiraGuncelle=findViewById(R.id.fabHatiraGuncelle);
        if (temaRengi.matches("koyu")) {
            fabHatiraEkle.setImageResource(R.drawable.ic_add_dark);
            fabHatiraGuncelle.setImageResource(R.drawable.ic_add_dark);
        }
        else
        {
            fabHatiraEkle.setImageResource(R.drawable.ic_add_dark);
            fabHatiraGuncelle.setImageResource(R.drawable.ic_add_dark);
        }
        realmTanimla();
        tanimla();
        yeniVeyaEskikayit();
    }

    public void yeniVeyaEskikayit(){
        info=intent.getStringExtra("info");
        if (info.matches("old"))
        {
            position =intent.getIntExtra("noteId",0);
            okunacakKayitPozisyonu=position;
            kayitOku(position);
            fabHatiraGuncelle.setVisibility(View.VISIBLE);
            fabHatiraEkle.setVisibility(View.INVISIBLE);
        }
        else if (info.matches("new"))
        {
            fabHatiraGuncelle.setVisibility(View.INVISIBLE);
            fabHatiraEkle.setVisibility(View.VISIBLE);
        }
    }

    public void kayitOku(final int position){
        RealmResults<Hatiralar> hatiralars=realm.where(Hatiralar.class).findAll();
        baslikText.setText(hatiralars.get(position).getBaslik()) ;
        metinText.setText(hatiralars.get(position).getMetin());
        kisiText.setText(hatiralars.get(position).getKisi());
        byte[] bytes = hatiralars.get(position).getResim();
        if (bytes!=null){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        imageView.setImageBitmap(bitmap);}
        actionBar.setTitle(hatiralars.get(position).getBaslik());
        actionBar.setSubtitle(hatiralars.get(position).getTarih().toString().substring(0,19)) ;

    }
    public void guncelle(int position){
        RealmResults<Hatiralar>gelenNote=realm.where(Hatiralar.class).findAll();
        final Hatiralar hatiralar=gelenNote.get(position);
        final String baslik=baslikText.getText().toString();
        final String metin=metinText.getText().toString();
        final String kisi=kisiText.getText().toString() ;
        if(selectImage!=null) {
            Bitmap smallerImage = makeSmallerImage(selectImage, 1000);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallerImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            final byte[] byteArray = outputStream.toByteArray();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    hatiralar.setBaslik(baslik);
                    hatiralar.setMetin(metin);
                    hatiralar.setTarih(tarih);
                    hatiralar.setKisi(kisi);
                    hatiralar.setResim(byteArray);
                    Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                    baslikText.setText("");
                    metinText.setText("");
                    kisiText.setText("");

                }
            });
        }
        else{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    hatiralar.setBaslik(baslik);
                    hatiralar.setMetin(metin);
                    hatiralar.setTarih(tarih);
                    hatiralar.setKisi(kisi);
                    Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                    baslikText.setText("");
                    metinText.setText("");
                    kisiText.setText("");
                    Intent intent = new Intent(getApplicationContext(), HatiralarListActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
    public void tanimla(){
        imageView=findViewById(R.id.imageView);
        menuInflater2 =getMenuInflater();
        actionBar = getSupportActionBar();
        tarih = Calendar.getInstance().getTime();
        metinText=findViewById(R.id.metinText);
        baslikText=findViewById(R.id.baslikText);
        kisiText=findViewById(R.id.kisiText);
        intent =getIntent();
    }
    public void realmTanimla(){
        Realm.init(getApplicationContext());
        configurationHatira = new RealmConfiguration.Builder().name("Hatira").build();
        Realm.setDefaultConfiguration(configurationHatira);
        realm=Realm.getDefaultInstance();
    }

    public void ekle(final String baslik, final String metin,final String kisi){


        if(selectImage!=null) {
            Bitmap smallerImage = makeSmallerImage(selectImage, 1000);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallerImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            final byte[] byteArray = outputStream.toByteArray();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Hatiralar hatiralar=realm.createObject(Hatiralar.class);
                hatiralar.setBaslik(baslik);
                hatiralar.setMetin(metin);
                hatiralar.setTarih(tarih);
                hatiralar.setKisi(kisi);
                if (byteArray.length!=0) {
                    hatiralar.setResim(byteArray);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                baslikText.setText("");
                metinText.setText("");
                kisiText.setText("");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "Başarısız", Toast.LENGTH_SHORT).show();
            }
        });
        }
        else {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Hatiralar hatiralar=realm.createObject(Hatiralar.class);
                    hatiralar.setBaslik(baslik);
                    hatiralar.setMetin(metin);
                    hatiralar.setTarih(tarih);
                    hatiralar.setKisi(kisi);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                    baslikText.setText("");
                    metinText.setText("");
                    kisiText.setText("");
                    Intent intent = new Intent(getApplicationContext(),HatiralarListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(getApplicationContext(), "Başarısız", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater2.inflate(R.menu.resimekle,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.resim_ekle:
                resimekle();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    public void resimekle(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else
        {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode ==1)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode==RESULT_OK && data!=null)
        {
            Uri imageData = data.getData();

            try
            {

                if (Build.VERSION.SDK_INT >=28)
                {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectImage);
                }
                else
                {
                    selectImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    imageView.setImageBitmap(selectImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public  Bitmap makeSmallerImage(Bitmap image , int maximumsize)
    {
        int width =image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width/(float) height;
        if (bitmapRatio>1)  // if width of the image is bigger than it's height, do it
        {
            width = maximumsize;
            height = (int) (width/bitmapRatio);
        }
        else if (bitmapRatio<1) // if height of the image is bigger than it's width, do it
        {
            height = maximumsize;
            width = (int)(height*bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }

     public void fabHatiraEkle(View view) {

        String baslik=baslikText.getText().toString();
        String metin=metinText.getText().toString();
        String kisi = kisiText.getText().toString();

         ekle(baslik,metin,kisi);

    }
    public void fabHatiraGuncelle(View view){

        guncelle(okunacakKayitPozisyonu);
    }
}