package com.example.fragmentsiz.hatiralar;

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

public class HatiralarListActivity extends AppCompatActivity {

    SharedPreferences.Editor editor ;
    SharedPreferences ayarlar ;
    String temaRengi;
    FloatingActionButton fab;
    SwipeMenuListView listView;
    AdapterHatiralar adapter;
    TextView textView;
    Realm realm;
    RealmConfiguration configurationHatira;
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
        setContentView(R.layout.activity_hatiralar_list);
        fab=findViewById(R.id.fabHatiraCreate);
        if (temaRengi.matches("koyu")) {
            fab.setImageResource(R.drawable.ic_create_dark);
        }
        else
        {
            fab.setImageResource(R.drawable.ic_create_dark);
        }
        listView=findViewById(R.id.hatiraList);
        realmTanimla();
        tanimla();
        kayitOkuma();
        goster();
        //fabTanim();
    }


    public void realmTanimla(){
        Realm.init(getApplicationContext());
        configurationHatira = new RealmConfiguration.Builder().name("Hatira").build();
        Realm.setDefaultConfiguration(configurationHatira);
        realm=Realm.getDefaultInstance();
    }
    public void sil(final int position){
        final RealmResults<Hatiralar> gelenlist=realm.where(Hatiralar.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Hatiralar hatiralar =gelenlist.get(position);
                hatiralar.deleteFromRealm();
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void tanimla(){
        fab= findViewById(R.id.fabHatiraCreate);
        textView=findViewById(R.id.textView);
    }
    public void goster(){
        RealmResults<Hatiralar> hatiralars=realm.where(Hatiralar.class).findAll();
        if (hatiralars.size()>0)
        {

            adapter = new AdapterHatiralar(hatiralars,getApplicationContext(),temaRengi);
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
    public void kayitOkuma() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), HatiralarKayitActivity.class);
                intent.putExtra("noteId", position);
                intent.putExtra("info", "old");
                startActivity(intent);
            }
        });
    }
    public void fabHatiraCreate(View view){

        Intent intent = new Intent(getApplicationContext(), HatiralarKayitActivity.class);
        intent.putExtra("info","new");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);*/
        finish();
    }
}