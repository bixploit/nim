package com.example.fragmentsiz.hatiralar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fragmentsiz.R;

import java.util.List;

public class AdapterHatiralar extends BaseAdapter {

    List<Hatiralar> list;
    Context context;
    String temaRengi;

    public AdapterHatiralar(List<Hatiralar> list, Context context,String temaRengi) {
        this.temaRengi=temaRengi;
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView= LayoutInflater.from(context).inflate(R.layout.layout_hatiralar_list,parent,false);
        LinearLayout linearLayout=convertView.findViewById(R.id.linearLayoutHatiraList);
        TextView baslik=convertView.findViewById(R.id.baslikText);
        TextView tarih=convertView.findViewById(R.id.tarihText);
        baslik.setText(list.get(position).getBaslik());
        String kısaTarih=list.get(position).getTarih().toString();
        String ksTarih=kısaTarih.substring(0,19);
        tarih.setText(ksTarih);

        if (temaRengi.matches("koyu"))
        {
            baslik.setTextColor(context.getResources().getColor(R.color.color_5));
            tarih.setTextColor(context.getResources().getColor(R.color.color_5));
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.color_7));

        }
        else
        {
            baslik.setTextColor(context.getResources().getColor(R.color.color_4));
            tarih.setTextColor(context.getResources().getColor(R.color.color_4));
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.color_5));
        }
        return convertView;
    }
}
