package com.example.fragmentsiz.not;

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

public class NoteListActivity extends AppCompatActivity {

    SharedPreferences.Editor editor ;
    SharedPreferences ayarlar ;
    String temaRengi;
    Realm realm;
    AdapterNotes adapterNotes;
    SwipeMenuListView listView  ;
    FloatingActionButton fab;
    RealmConfiguration configurationNote;
    TextView textView;
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
        setContentView(R.layout.activity_note_list);
        fab=findViewById(R.id.fabNotlarCreate);
        if (temaRengi.matches("koyu")) {
            fab.setImageResource(R.drawable.ic_create_dark);
        }
        else
        {
            fab.setImageResource(R.drawable.ic_create_dark);
        }
        realmTanimla();
        tanimlamalar();
        fabTanim();
        goster();
        kayitOkuma();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void fabTanim(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NoteKayitActivity.class);
                intent.putExtra("info","new");
                startActivity(intent);
            }
        });
    }
    public void realmTanimla(){
        Realm.init(getApplicationContext());
        configurationNote= new RealmConfiguration.Builder().name("Note").build();
        Realm.setDefaultConfiguration(configurationNote);
        realm=Realm.getDefaultInstance();
    }
    public void tanimlamalar(){
        listView=findViewById(R.id.notList);
        textView=findViewById(R.id.textView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void goster(){
        RealmResults<Notes> notes=realm.where(Notes.class).findAll();
        if (notes.size()>0)
        {
            adapterNotes = new AdapterNotes(notes,getApplicationContext(),temaRengi);
            listView.setAdapter(adapterNotes);

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
    public void sil(final int position){
        final RealmResults<Notes>gelenlist=realm.where(Notes.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notes notes =gelenlist.get(position);
                notes.deleteFromRealm();
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
    public void kayitOkuma()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),NoteKayitActivity.class);
                intent.putExtra("noteId",position);
                intent.putExtra("info","old");
                startActivity(intent);
            }
        });
    }
}