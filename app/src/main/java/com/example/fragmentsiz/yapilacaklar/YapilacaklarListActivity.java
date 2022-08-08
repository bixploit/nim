package com.example.fragmentsiz.yapilacaklar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.fragmentsiz.MainActivity;
import com.example.fragmentsiz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class YapilacaklarListActivity extends AppCompatActivity {

    SharedPreferences.Editor editor ;
    SharedPreferences ayarlar ;
    String temaRengi;
    SwipeMenuListView listView;
    AdapterYapilacaklar adapter;
    Realm realm;
    TextView textView;
    FloatingActionButton fab;
    RealmConfiguration configurationYapilacak;
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
        setContentView(R.layout.activity_yapilacaklar_list);
        fab=findViewById(R.id.fabYapilacaklarCreate);
        if (temaRengi.matches("koyu")) {
            fab.setImageResource(R.drawable.ic_create_dark);
        }
        else
        {
            fab.setImageResource(R.drawable.ic_create_dark);
        }
        realmTanimla();
        tanimla();
        fabClick();
        goster();
        kayitOkuma();
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
    public void realmTanimla(){
        Realm.init(getApplicationContext());
        configurationYapilacak= new RealmConfiguration.Builder().name("Yapilacaklar").build();
        Realm.setDefaultConfiguration(configurationYapilacak);
        realm=Realm.getDefaultInstance();
    }
    public void fabClick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),YapilacaklarKayitActivity.class);
                intent.putExtra("info","new");
                startActivity(intent);
            }
        });
    }
    public void sil(final int position){
        final RealmResults<Yapilacaklar> gelenlist=realm.where(Yapilacaklar.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Yapilacaklar yapilacaklar =gelenlist.get(position);
                yapilacaklar.deleteFromRealm();
                if (gelenlist.size()==0) {
                    listView.setVisibility(View.INVISIBLE);
                    textView.setText("Herhangi bir kaydınız bulunmamaktadır");
                }
                else if (gelenlist.size()>0)
                {
                    goster();
                }
            }
        });
    }
    public void tanimla(){

        textView=findViewById(R.id.textView);
        listView=findViewById(R.id.yapilacaklarList);
    }
    public void goster(){
        RealmResults<Yapilacaklar> yapilacaklars=realm.where(Yapilacaklar.class).findAll();
        if (yapilacaklars.size()>0)
        {
            adapter = new AdapterYapilacaklar(yapilacaklars,getApplicationContext(),temaRengi);
            listView.setAdapter(adapter);

            SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    // set item width
                    deleteItem.setWidth(170);
                    // set a icon
                    deleteItem.setIcon(R.drawable.del);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };
            listView.setMenuCreator(creator);

            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index){
                        case 0:
                            sil(position);
                            break;
                    }
                    return false;
                }
            });
        }
    }
    public void kayitOkuma()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),YapilacaklarKayitActivity.class);
                intent.putExtra("noteId",position);
                intent.putExtra("info","old");
                startActivity(intent);
            }
        });
    }
}