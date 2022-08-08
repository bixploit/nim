package com.example.fragmentsiz.ayarlar;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fragmentsiz.MainActivity;
import com.example.fragmentsiz.R;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import yuku.ambilwarna.AmbilWarnaDialog;

public class AyarlarActivity extends AppCompatActivity {

    Realm realm;
    RealmConfiguration realmConfigurationKullaniciBilgileri;
    String temaRengi;
    SharedPreferences.Editor editor ;
    SharedPreferences ayarlar ;
    int checkedItem;
    ActionBar actionBar;
    ListView listView;
    LinearLayout linearLayoutAyarlar;
    int imagesN[]={R.drawable.night};
    int imagesD[]={R.drawable.ic_sun_light};
    String ayarlarListesi[]={"Tema"};

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
        setContentView(R.layout.activity_ayarlar);

        if (temaRengi.matches("koyu")) {
            checkedItem=1;
        }
        else
        {
            checkedItem=0;
        }
        realmTanimla();
        tanimla();
        goster();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    public  void goster(){

        MyAdapter adapter=new MyAdapter(getApplicationContext(),ayarlarListesi,imagesN,imagesD);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        temaSec();
                        break;
                }
            }
        });
    }

    public void restartApp () {
        Intent i = new Intent(getApplicationContext(),AyarlarActivity.class);
        startActivity(i);
        finish();
    }

    public void temaSec(){
        final int[] iTema = new int[1];

        String tem[]={"Açık","Koyu"};
        AlertDialog alertDialog=new AlertDialog.Builder(AyarlarActivity.this)
                .setTitle("Tema seç")
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (iTema[0]==0)
                        {
                            temaKoyu();
                        }
                        else if (iTema[0]==1)
                        {
                            temaAcik();
                        }
                    }
                })
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        iTema[0]=2;
                    }
                })
                .setSingleChoiceItems(tem, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                iTema[0]=1;
                                break;
                            case 1:
                                iTema[0]=0;
                                break;
                        }
                    }
                })
                .show();
    }
    public void temaKoyu(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        temaRengi="koyu";
        editor.remove("tema");
        editor.putString("tema", temaRengi);
        editor.commit();
        restartApp();
    }
    public void temaAcik(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        temaRengi="açık";
        editor.remove("tema");
        editor.putString("tema", temaRengi);
        editor.commit();
        restartApp();
    }
    public void tanimla(){
        listView = (ListView) findViewById(R.id.ayarlarList);
        actionBar=getActionBar();
        linearLayoutAyarlar=findViewById(R.id.linearLayoutAyarlar);
    }

     public void realmTanimla(){
         Realm.init(getApplicationContext());
         realmConfigurationKullaniciBilgileri= new RealmConfiguration.Builder().name("KullaniciBilgileri").build();
         Realm.setDefaultConfiguration(realmConfigurationKullaniciBilgileri);
         realm=Realm.getDefaultInstance();
     }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String ayarlarListesiA[];
        int imageNa[];
        int imageDa[];

        MyAdapter(Context c,String ayarlar[],int imagsN[],int imagsD[]){
            super(c,R.layout.layout_ayarlar,R.id.itemText,ayarlar);
            this.context = c;
            this.ayarlarListesiA=ayarlar;
            this.imageNa=imagsN;
            this.imageDa=imagsD;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater= (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row =layoutInflater.inflate(R.layout.layout_ayarlar,parent,false);
            ImageView imageView=row.findViewById(R.id.itemImage);
            TextView textView=row.findViewById(R.id.itemText);

            if (temaRengi.matches("koyu"))
            {
                imageView.setImageResource(imageNa[position]);
                textView.setText(ayarlarListesiA[position]);
                textView.setTextColor(getResources().getColor(R.color.color_5));
            }
            else
            {
                imageView.setImageResource(imageDa[position]);
                textView.setText(ayarlarListesiA[position]);
                textView.setTextColor(getResources().getColor(R.color.color_4));
            }
            return row;
        }
    }
}