package com.amanecer.themykitchen.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.amanecer.themykitchen.networking.API_Constant;


/**
 * Created by amanecer on 19/11/2014.
 */
public class DBHelper extends SQLiteOpenHelper{


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase  db){

        /*
        *
        *
 CREATE TABLE recipe(_id INTEGER PRIMARY KEY,recipeId TEXT ,recipeName TEXT    ,ingredients TEXT    ,favorite TEXT    ,rating INTEGER ,PATH TEXT    ,totalTimeInSeconds INTEGER ,course TEXT    ,cuisine TEXT    ,flavors TEXT    ,myIngredients TEXT);

CREATE TABLE DB_TABLE_RECIPEWithDetails(_id INTEGER PRIMARY KEY,recipeId TEXT,recipeName TEXT,nutritionEstimates TEXT,ingredients TEXT,images TEXT,source TEXT,favorite TEXT,rating INTEGER ,PATH TEXT,totalTimeInSeconds INTEGER ,course TEXT,cuisine TEXT,flavors TEXT,myIngredients TEXT)   ;

CREATE TABLE product(_id INTEGER PRIMARY KEY  ,productId TEXT,productName TEXT,PATH TEXT,favorite TEXT)                ;

CREATE TABLE DB_TBALE_MYFRIGE(_id INTEGER PRIMARY KEY ,productId TEXT,productWithCount TEXT ,producs TEXT,count TEXT,favorite TEXT)               ;
        *
        *
        * the table that gets recipeId is in TEXT becuaes it conaitns the name of the recuipes and the id num. some
        * of them dont have INTEGER;
        * */



        String tableRecipe = "CREATE TABLE "+Constans.DB_TABLE_RECIPE+"("
                +   Constans._id                         +" INTEGER PRIMARY KEY,"
                +   API_Constant.recipeId                +" TEXT    ,"
                +   API_Constant.recipeName              +" TEXT    ,"
                +   API_Constant.ingredients             +" TEXT    ,"
                +   Constans.favorite                    +" TEXT    ,"
                +   API_Constant.rating                  +" INTEGER ,"
                +   Constans.PATH                        +" TEXT    ,"
                +   API_Constant.totalTimeInSeconds      +" INTEGER ,"
                +   API_Constant.course                  +" TEXT    ,"
                +   API_Constant.cuisine                 +" TEXT    ,"
                +   API_Constant.flavors                 +" TEXT    ,"
                +   API_Constant.myIngredients           +" TEXT);";



        String tableRecipeWithWithDetails = "CREATE TABLE "+Constans.DB_TABLE_RECIPEWithDetails + "("
                +   Constans._id                         +" INTEGER PRIMARY KEY,"
                +   API_Constant.recipeId                +" TEXT    ," //
                +   API_Constant.recipeName              +" TEXT    ," //
                +   API_Constant.nutritionEstimates      +" TEXT    ," //
                +   API_Constant.ingredients             +" TEXT    ," //
                +   API_Constant.images                  +" TEXT    ," //
                +   API_Constant.source                  +" TEXT    ," //
                +   Constans.favorite                    +" TEXT    ,"
                +   API_Constant.rating                  +" INTEGER ," //
                +   Constans.PATH                        +" TEXT    ," //
                +   API_Constant.totalTimeInSeconds      +" INTEGER ," //
                +   API_Constant.course                  +" TEXT    ," //
                +   API_Constant.cuisine                 +" TEXT    ," //
                +   API_Constant.flavors                 +" TEXT    ,"
                +   API_Constant.myIngredients           +" TEXT)   ;"; //



        String tableProduct = "CREATE TABLE "+Constans.DB_TBALE_PRODUCT + "("
                +   Constans._id                +" INTEGER PRIMARY KEY  ,"
                +   API_Constant.productId      +" INTEGER                 ," //
                +   API_Constant.productName    +" TEXT                 ," //
                +   API_Constant.PATH           +" TEXT                 ," //
                +   Constans.favorite           +" TEXT)                ;"; //


        String tableMyFrige = "CREATE TABLE "+Constans.DB_TBALE_MYFRIGE+"("
                +   Constans._id                     +" INTEGER PRIMARY KEY ,"
                +   API_Constant.MyFrigeId           +" INTEGER                ," //
                +   API_Constant.productWithCount    +" TEXT                ," //
                +   API_Constant.producs             +" TEXT                ," //
                +   API_Constant.count               +" TEXT                ," //
                +   Constans.favorite                +" TEXT)               ;"; //

          try {
             db.execSQL(tableRecipe);
             db.execSQL(tableProduct);
             db.execSQL(tableMyFrige);
             db.execSQL(tableRecipeWithWithDetails);
         }catch (SQLiteException e) {
              e.getCause();
         }
        }






    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
