package com.example.fragmentsiz.yapilacaklar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fragmentsiz.R;
import com.example.fragmentsiz.hatiralar.HatiralarListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class YapilacaklarKayitActivity extends AppCompatActivity
{
    SharedPreferences.Editor editor ;
    SharedPreferences ayarlar ;
    String temaRengi;
    Calendar calendar;
    int year,month,dayOfMonth,hourOfDay,minute;
    Integer position;
    Integer okunacakKayitPozisyonu;
    ActionBar actionBar;
    Realm realm;
    EditText metinText,baslikText;
    TextView dateTxt,timeTxt;
    TextView tarihText;
    Intent intent;
    Date tarih ;
    String info,hedefTarih;
    ListView listView;
    RealmConfiguration configurationYapilacak;
    FloatingActionButton fabEkle,fabGuncelle;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    int imagesN[]={R.drawable.ic_calender_dark};
    int imagesD[]={R.drawable.ic_calender_light};
    String hedefZ[]={"Hedef zaman"};
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
        setContentView(R.layout.activity_yapilacaklar_kayit);

        fabEkle=findViewById(R.id.fabYapilacaklarEkle);
        fabGuncelle=findViewById(R.id.fabYapilacaklarGuncelle);
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
        goster();
    }

    public void goster()
    {
        MyAdapterYapilacaklar adapter=new MyAdapterYapilacaklar(getApplicationContext(),hedefZ,imagesD,imagesN);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        calender();
                        break;
                }
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
    public void calender(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(YapilacaklarKayitActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateTxt.setText(day + "/" + month + "/" + year);
                        hedefTarih=hourOfDay+"."+minute+ " "+day + "/" + month + "/" + year;
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
        timePicker();
    }
    public void timePicker(){

        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        timePickerDialog=new TimePickerDialog(YapilacaklarKayitActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeTxt.setText(hourOfDay+"."+minute);
            }
        }, hourOfDay, minute, true);
        calendar.set(year,month,dayOfMonth,hourOfDay,minute);
        timePickerDialog.show();
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
        RealmResults<Yapilacaklar> yapilacaklars=realm.where(Yapilacaklar.class).findAll();
        baslikText.setText(yapilacaklars.get(position).getBaslik()) ;
        metinText.setText(yapilacaklars.get(position).getMetin());
        dateTxt.setText(yapilacaklars.get(position).getHedefTarih());
        //colorDrawable=new ColorDrawable(Color.parseColor("#fff"));
        // actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(yapilacaklars.get(position).getBaslik());
        actionBar.setSubtitle(yapilacaklars.get(position).getTarih().toString().substring(0,19)) ;
    }
    public void guncelle(int position){
        RealmResults<Yapilacaklar>gelenNote=realm.where(Yapilacaklar.class).findAll();
        final Yapilacaklar yapilacaklar=gelenNote.get(position);
        final String baslik=baslikText.getText().toString();
        final String metin=metinText.getText().toString();


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                yapilacaklar.setBaslik(baslik);
                yapilacaklar.setMetin(metin);
                yapilacaklar.setTarih(tarih);
                Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                baslikText.setText("");
                metinText.setText("");
            }
        });
    }
    public void tanimla(){
        timeTxt=findViewById(R.id.timeTxt);
        dateTxt=findViewById(R.id.dateTxt);
        listView=findViewById(R.id.listView);
        actionBar = getSupportActionBar();
        tarih = Calendar.getInstance().getTime();
        metinText=findViewById(R.id.metinText);
        baslikText=findViewById(R.id.baslikText);
        tarihText=findViewById(R.id.tarihText);
        intent =getIntent();
    }
    public void realmTanimla(){
        Realm.init(getApplicationContext());
        configurationYapilacak= new RealmConfiguration.Builder().name("Yapilacaklar").build();
        Realm.setDefaultConfiguration(configurationYapilacak);
        realm=Realm.getDefaultInstance();
    }

    public void ekle(final String baslik, final String metin,final String hedefTarih){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Yapilacaklar yapilacaklar=realm.createObject(Yapilacaklar.class);
                yapilacaklar.setBaslik(baslik);
                yapilacaklar.setMetin(metin);
                yapilacaklar.setTarih(tarih);
                yapilacaklar.setHedefTarih(hedefTarih);
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

    public void fabYapilacaklarEkle(View view) {

        String baslik=baslikText.getText().toString();
        String metin=metinText.getText().toString();
        ekle(baslik,metin,hedefTarih);
    }
    public void fabYapilacaklarGuncelle(View view){

        guncelle(okunacakKayitPozisyonu);
    }

    class MyAdapterYapilacaklar extends ArrayAdapter<String> {

        Context context;
        String ayarlarListesiA[];
        int imagesN[];
        int imagesD[];

        MyAdapterYapilacaklar(Context c, String ayarlar[], int imagsD[],int imagsN[]) {
            super(c, R.layout.layout_yapilacaklar_kayit, R.id.itemText, ayarlar);
            this.context = c;
            this.ayarlarListesiA = ayarlar;
            this.imagesD = imagsD;
            this.imagesN = imagsN;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.layout_yapilacaklar_kayit, parent, false);
            ImageView imageView = row.findViewById(R.id.itemImage);
            TextView textView = row.findViewById(R.id.itemText);
            if (temaRengi.matches("koyu"))
            {
                imageView.setImageResource(imagesN[position]);
                textView.setText(ayarlarListesiA[position]);
                textView.setTextColor(getResources().getColor(R.color.color_5));
            }else{
                imageView.setImageResource(imagesD[position]);
                textView.setText(ayarlarListesiA[position]);
                textView.setTextColor(getResources().getColor(R.color.color_4));
            }

            return row;
        }
    }

}