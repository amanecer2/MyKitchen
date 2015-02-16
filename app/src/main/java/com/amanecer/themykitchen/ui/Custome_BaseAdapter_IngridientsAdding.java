package com.amanecer.themykitchen.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.amanecer.themykitchen.obj.IngridientsAddingObj;
import com.amanecer.themykithcen.R;

import java.util.ArrayList;

/**
 * Created by amanecer on 14/12/2014.
 */
public class Custome_BaseAdapter_IngridientsAdding extends BaseAdapter {

    Activity activity;
    ArrayList<IngridientsAddingObj> list;

    public Custome_BaseAdapter_IngridientsAdding(Activity activity, ArrayList<IngridientsAddingObj> list) {
        this.activity = activity;
        this.list = list;
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        xmlsAdding xmls =null;
        if(convertView==null){
        xmls = new xmlsAdding();
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.adding_ingridients_row,parent,false);

            xmls.ingridientName = (TextView) convertView.findViewById(R.id.IngridientName);
            xmls.obligatory = (CheckBox)convertView.findViewById(R.id.obligatoryIngridient);

            convertView.setTag(xmls);
        }else {
            convertView.getTag();
        }
    try {
        final IngridientsAddingObj obj = list.get(position);

        xmls.ingridientName.setText(obj.getIngridientName());
        xmls.obligatory.setChecked(obj.isObligatory());


        final xmlsAdding finalXmls = xmls;
        xmls.obligatory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = list.get(position).getIngridientName();
                obj.setObligatory(finalXmls.obligatory.isChecked());
                obj.returnTheValue();
            }
        });

        xmls.ingridientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }catch (Exception e){
        e.getCause();
    }

        return convertView;

    }
    private class xmlsAdding{
        TextView ingridientName;
        CheckBox obligatory;

    }
}
