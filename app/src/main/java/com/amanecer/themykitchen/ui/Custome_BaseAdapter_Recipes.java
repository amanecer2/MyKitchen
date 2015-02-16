package com.amanecer.themykitchen.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amanecer.themykitchen.networking.API_Constant;
import com.amanecer.themykitchen.networking.Asyc_API_GetRecipeById;
import com.amanecer.themykitchen.networking.Asyc_Pic;
import com.amanecer.themykitchen.networking.Asyc_Pic_ListView_Recipes_found;
import com.amanecer.themykitchen.obj.Recipes;
import com.amanecer.themykithcen.R;

import java.util.ArrayList;

/**
 * Created by amanecer on 22/12/2014.
 */
public class Custome_BaseAdapter_Recipes extends BaseAdapter {

    Activity activity;
    ArrayList<Recipes> list;
    Asyc_Pic asyc_pic;
    Asyc_API_GetRecipeById api_getRecipeById;


    public Custome_BaseAdapter_Recipes(Activity activity, ArrayList<Recipes> list) {
        this.activity = activity;
        this.list = list;
        asyc_pic = new Asyc_Pic(activity);
        // api_getRecipeById = new Asyc_API_GetRecipeById(activity);
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
        Xmls xmls = null; // creating the instance of the calss Xmls
        final int getPosition = position;
        if (convertView==null){
            xmls = new Xmls();
            LayoutInflater inflater = activity.getLayoutInflater(); // this to get the xml to java;
            convertView = inflater.inflate(R.layout.listview_recipe_layout, parent, false);// bundding the xml to the java;

            xmls.recipeName = (TextView)convertView.findViewById(R.id.recipeName);
            xmls.describe = (TextView)convertView.findViewById(R.id.describe);
            xmls.rating = (RatingBar)convertView.findViewById(R.id.recipeRating);
            xmls.img = (ImageView)convertView.findViewById(R.id.Path);
            xmls.gotAllIngridients=(CheckBox)convertView.findViewById(R.id.gotAllIngridients);
            xmls.layout = (LinearLayout)convertView.findViewById(R.id.listview_layout);

            convertView.setTag(xmls); // save this bundding
        }else {
            convertView.getTag(); // if your not new than get all the saved tags;
        }


        try {
            final Recipes recipes = list.get(position);  // the list is class of the xml but in java -- aka: String,boolean,int....;
            Asyc_Pic_ListView_Recipes_found asyc_pic = new Asyc_Pic_ListView_Recipes_found(activity);
            asyc_pic.execute(recipes.getPATH());
            xmls.recipeName.setText(recipes.getRecipeName());
            xmls.describe.setText(recipes.getDescribe());
            xmls.rating.setMax(5);

            float num = Float.valueOf(String.valueOf(recipes.getRating()));
            xmls.rating.setRating(num);
            xmls.gotAllIngridients.setChecked(recipes.isGotAllIngridients());

            xmls.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // to set event when press on this row;
                    Toast.makeText(activity, "click by custome" + list.get(position).getRecipeName(), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder =new AlertDialog.Builder(activity);
                    builder.setTitle(activity.getString(R.string.recipeChooseTitle));
                    builder.setMessage(activity.getString(R.string.recipeChooseMessge)+list.get(position).getRecipeName());
                    builder.setCancelable(true);
                    builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(activity, TheRecipe.class);
                                    String recipeName = list.get(position).getRecipeName();
                                    intent.putExtra(API_Constant.recipeName, recipeName);

                                    activity.startActivity(intent);
                                }
                            }
                    );

                    builder.setNegativeButton(activity.getString(R.string.goBack), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            });
        }catch (Exception e){
            e.getCause();

        }
        return convertView;
    }

    public static class Xmls{  // this class represent the xml for the row;
        public LinearLayout layout;
        public TextView recipeName;
        public TextView describe;
        public ImageView img;
        public RatingBar rating;
        public CheckBox gotAllIngridients;

    }

}