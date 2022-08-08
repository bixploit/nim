package com.example.fragmentsiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fragmentsiz.ayarlar.AyarlarActivity;
import com.example.fragmentsiz.hatiralar.HatiralarListActivity;
import com.example.fragmentsiz.not.NoteListActivity;
import com.example.fragmentsiz.yapilacaklar.YapilacaklarListActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences.Editor editor ;
    SharedPreferences ayarlar ;
    String temaRengi;
    ListView listView;
    int imagesN[]={R.drawable.ic_to_do_dark,R.drawable.ic_not_dark,R.drawable.ic_hatira_dark};
    int imagesD[]={R.drawable.ic_to_do_light,R.drawable.ic_not_light,R.drawable.ic_hatira_light};
    String listeItem[]={"Yapılacaklar","Notlarım","Hatıralarım"};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.ayarlar,menu);
        if (temaRengi.matches("koyu")) {
            menu.findItem(R.id.action_ayarlar).setTitle(Html.fromHtml("<font color='#ffffff'>Settings</font>"));
        }return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.action_ayarlar)
        {
            Intent intent = new Intent(getApplicationContext(), AyarlarActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
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
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.kindList);

        MyAdapter adapter=new MyAdapter(getApplicationContext(),listeItem,imagesN,imagesD);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i)
                {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), YapilacaklarListActivity.class);
                        startActivity(intent);

                        break;
                    case 1 :
                        Intent intent1 = new Intent(getApplicationContext(), NoteListActivity.class);
                        startActivity(intent1);

                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), HatiralarListActivity.class);
                        startActivity(intent2);

                        break;
                }
            }
        });
    }
    class MyAdapter extends  ArrayAdapter<String>{

        Context context;
        String ayarlarListesiA[];
        int imageNa[];
        int imageDa[];

        MyAdapter(Context c,String ayarlar[],int imagsN[],int imagsD[]){
            super(c,R.layout.layout_ilk_liste,R.id.itemText,ayarlar);
            this.context = c;
            this.ayarlarListesiA=ayarlar;
            this.imageNa=imagsN;
            this.imageDa=imagsD;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater= (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row =layoutInflater.inflate(R.layout.layout_ilk_liste,parent,false);
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