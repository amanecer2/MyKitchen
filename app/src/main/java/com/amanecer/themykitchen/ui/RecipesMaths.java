package com.amanecer.themykitchen.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;

import com.amanecer.themykitchen.db.DBHandler;
import com.amanecer.themykitchen.networking.API_Constant;
import com.amanecer.themykitchen.networking.Asyc_API;
import com.amanecer.themykitchen.networking.Asyc_API_SearchRecipes;
import com.amanecer.themykitchen.networking.Asyc_Pic;
import com.amanecer.themykithcen.R;

import java.util.ArrayList;

/**
 * Created by amanecer on 03/12/2014.
 */
public class RecipesMaths extends Activity{
    Asyc_API asyc_api;
    Asyc_Pic asyc_pic;
    DBHandler handler;


    ArrayList<String> ingridientsList;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_found);

        handler = new DBHandler(this);

        asyc_api = new Asyc_API(this);
        asyc_pic = new Asyc_Pic(this);


        ingridientsList =new ArrayList<String>();



        Intent intent = getIntent();
        String products = intent.getStringExtra(API_Constant.producs);
        String whatTosearch = intent.getStringExtra(API_Constant.whatToSearch);
        if (products!=null){
            ingridientsList = handler.getAllProductsFromTempString(products);
            String in = "";String sendTheIngridients = "";



            for (int i =0; i<ingridientsList.size();i++){
                in+=ingridientsList.get(i)+"+";

            }
            String[] cutTheSpace = whatTosearch.split(" ");
            String noSpaces = "";
            for (int i = 0; i <cutTheSpace.length ; i++) {
                noSpaces+= cutTheSpace[i]+"%20";
            }

            // the API doesn't support space only %20 or +;
            String url = API_Constant.yummi_searchRecipes+noSpaces+API_Constant.yummi_howManyRecipesToSearch;
            Asyc_API_SearchRecipes asyc_api_searchRecipes = new Asyc_API_SearchRecipes(this);
           asyc_api_searchRecipes.execute(url,products);
        }




    }

    public void listviewStarter(){

    }

}
