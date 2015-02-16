package com.amanecer.themykitchen.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.amanecer.themykitchen.db.DBHandler;
import com.amanecer.themykitchen.networking.API_Constant;
import com.amanecer.themykitchen.networking.Asyc_API;
import com.amanecer.themykitchen.networking.Asyc_API_GetRecipeById;
import com.amanecer.themykithcen.R;


public class TheRecipe extends Activity {
    DBHandler handler;
    Cursor cursor;
    Asyc_API asyc_api;
    Asyc_API_GetRecipeById api_getRecipeById;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_recipe);

        handler = new DBHandler(this);

        cursor=null;
        cursor=handler.getAllRecipeByCursor();
        String findByID ="";

        asyc_api = new Asyc_API(this);
        api_getRecipeById = new Asyc_API_GetRecipeById(this);

        Intent intent = getIntent();
        String recipeName = intent.getStringExtra(API_Constant.recipeName);

        if (recipeName!=null){
            int recipeNameColum = cursor.getColumnIndex(API_Constant.recipeName);
            int recipeIdColum = cursor.getColumnIndex(API_Constant.recipeId);

            cursor.moveToFirst(); String recipeId ="";
            while (cursor.moveToNext()){
                if (cursor.getString(recipeNameColum).equals(recipeName)){
                    recipeId = cursor.getString(recipeIdColum);
                    break;
                }
            }
            findByID = "http://api.yummly.com/v1/api/recipe/"+recipeId+"?_app_id=e16b46c1&_app_key=6aaf699217a04fbbd896e22f77aafbd7";
            api_getRecipeById.execute(findByID,recipeId);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_the_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
