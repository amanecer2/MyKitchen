package com.amanecer.themykitchen.db;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.amanecer.themykitchen.networking.API_Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;


/**
 * Created by amanecer on 19/11/2014.
 */
public class DBHandler   {

    private DBHelper helper;


    public DBHandler(Context context) {
       helper = new DBHelper(context, Constans.DB_FILE_NAME ,  null, Constans.version);
    }




    public boolean isInternetAvailable() {
        try {

            InetAddress ipAddr = InetAddress.getByName("www.google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public void add(){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(API_Constant.recipeName,"fvew");

        try{
            db.insert(Constans.DB_TABLE_RECIPE,null,v);
        }finally {
            if (db.isOpen())
                db.close();
        }
    }

    public void addRecipeWithDetails(ContentValues v){
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.insert(Constans.DB_TABLE_RECIPEWithDetails,null,v);
        }catch (SQLiteException e){
            e.getCause();
        }finally {
            if (db.isOpen())
                db.close();
        }
    }

    public boolean addProductToMyFrige(ContentValues values) {
        boolean flag = true;
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.insert(Constans.DB_TBALE_MYFRIGE, null, values);
        } catch (SQLiteException e) {
            e.getCause();
            flag = false;
        }finally{
            if(db.isOpen())
                db.close();
        }
    return flag;
    }

    public void addProduct(ContentValues values) {

        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.insert(Constans.DB_TABLE_RECIPE, null, values);
        } catch (SQLiteException e) {
            e.getCause();
        } finally {
            if (db.isOpen())
                db.close();
        }
    }

    public void addRecipe(ContentValues values) {

        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.insert(Constans.DB_TABLE_RECIPE, null, values);
        } catch (SQLiteException e) {
            e.getCause();
        }finally{
            if(db.isOpen())
                db.close();
        }
    }


        public boolean comper_Must_HaveIngridientInRecpieWithMyFrigeByRecipeName(String recipeName, ArrayList<String> myFrigeIngridientList){
        ArrayList<String> ingridientList = new ArrayList<String>();
        Cursor cursorRecipe = getAllTableByCursorByfilterTableName(Constans.DB_TABLE_RECIPE) ;
        Cursor cursorMyFrige = getAllTableByCursorByfilterTableName(Constans.DB_TBALE_MYFRIGE);
        int cursorRecipeColumRecipeName = cursorRecipe.getColumnIndex(API_Constant.recipeName);
         int position = 0;
        for (cursorRecipe.moveToFirst();cursorRecipe.isAfterLast()!= true; cursorRecipe.moveToNext()){
            if(cursorRecipe.getString(cursorRecipeColumRecipeName).equals(recipeName))
               position = cursorRecipe.getPosition();
        }
        int ingridientColumRecipe = cursorRecipe.getColumnIndex(API_Constant.ingredients) ;
        int ingridientColumMyFrige = cursorMyFrige.getColumnIndex(API_Constant.productName);

        cursorMyFrige.moveToPosition(position);
            String arr = cursorMyFrige.getString(ingridientColumRecipe);
            try {
                JSONArray in = new JSONArray(arr);
                for (int i = 0; i < in.length() ; i++) {
                    JSONObject ingridientObject =in.getJSONObject(i);
                   String oneIngridient =  ingridientObject.getString(""+i);
                    ingridientList.add(oneIngridient);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cursorMyFrige.close();cursorRecipe.close(); // close the cursors;

            for (int i = 0; i < ingridientList.size() ; i++) {
                for (int j = 0; j <myFrigeIngridientList.size() ; j++) {
                    if (ingridientList.get(i).contains(myFrigeIngridientList.get(j))) // if one of the recipe in contian the my frige tahn ok;
                       continue; // if there is the ingriedient in my frige than go on
                    else
                        return false; // it mean there is a missing item in my frige and wont alout it;
                }
            }
            return true;
         }

    public ArrayList<String> comperIngridientAndReturnTheMissingIngridientByRecipeName(String recipeName, ArrayList<String> myFrigeIngridientList , int position){
        ArrayList<String> ingridientList = new ArrayList<String>();
        ArrayList<String> missingIngridientList = new ArrayList<String>();
        Cursor cursorRecipe  = getAllTableByCursorByfilterTableName(Constans.DB_TABLE_RECIPE) ;
        Cursor cursorMyFrige = getAllTableByCursorByfilterTableName(Constans.DB_TBALE_MYFRIGE);
        int cursorRecipeColumRecipeName = cursorRecipe.getColumnIndex(API_Constant.recipeName);


        int ingridientColumRecipe = cursorRecipe.getColumnIndex(API_Constant.ingredients) ;
        int ingridientColumMyFrige = cursorMyFrige.getColumnIndex(API_Constant.productName); // there is no productName inn my frige

        cursorRecipe.moveToPosition(position);   // ok work

        try {
            String strArray = cursorRecipe.getString(ingridientColumRecipe);
            JSONArray in = new JSONArray(strArray);
            for (int i = 0; i < in.length() ; i++) {
                ingridientList.add(in.getString(i)); //["str one","str two","",....] this is the array of strings;
            }



        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

        }



        for (int i = 0; i < ingridientList.size() ; i++) {  // run on all ingridient list
            for (int j = 0; j < myFrigeIngridientList.size(); j++) { // run on mylist;

                                        // indexOf - looking for String and if not in return -1;
               // if (ingridientList.get(i).indexOf(myFrigeIngridientList.get(j))!=-1 || ingridientList.get(i).indexOf((" "+myFrigeIngridientList.get(j)))!=-1  )  // if one of the recipe in contian the my frige tahn ok;
                 //if(ingridientList.get(i).contains(myFrigeIngridientList.get(j))>0)
                String ingrident =ingridientList.get(i);
                String myIngrient = myFrigeIngridientList.get(j);
                if( ingrident.indexOf(myIngrient)>0   )/////// THIS IS NOT WORKING for "onion" != "sweet onion" !!!! dont know why.
                    continue; // if there is the ingriedient in my frige than go on
                if (j == myFrigeIngridientList.size()-1)
                    missingIngridientList.add(ingridientList.get(i));

            }
        }
        return missingIngridientList;
    }


    public boolean isHaveAllIngridientsForThisRecipe(ArrayList<String> myFrigeList,ArrayList<String> recipeList){

       /* ArrayList<String> list1 = new ArrayList<String>();// this is myFrige list;
        ArrayList<String> list2 = new ArrayList<String>(); // this is the recipeList
        ArrayList<String> whatContains = new ArrayList<String>();
        list1.add("onion");
        list1.add("sugar");
        list1.add("basil");
        list1.add("lemon");
        list1.add("salt");
        list1.add("tomatoes");
        list1.add("oil");

        list2.add("kosher salt");
        list2.add("fresh basil");
        list2.add("browen sugar");
        list2.add("fresh lemon");
        list2.add("extra oil");
        list2.add("marmelada");*/

        // need to lower case all the lists!!!! cus some time its in "Salt" and "salt"l;


       int count = 0;
        boolean haveAllMyIngridientForTheRecipe = false;
        for (int i = 0; i < recipeList.size() ; i++) {
            for (int j = 0; j < myFrigeList.size(); j++) {
                recipeList.get(i).toLowerCase();
                myFrigeList.get(j).toLowerCase();
                String oneList1 =myFrigeList.get(j);
                String oneList2 =recipeList.get(i);
                String oneListWithSpace = " "+recipeList.get(i);
                if (oneList2.contains(oneList1) /*|| oneList1.contains(oneListWithSpace) */) {
                   // whatContains.add(recipeList.get(i));
                    count++;
                    continue;
                }
            }
        }
       // my frige has to be at least :
        /*
        * the size of the frige size but no les!
         * the size of the recipe size or more
        * */
        if (count== recipeList.size() )
            haveAllMyIngridientForTheRecipe = true;

        return haveAllMyIngridientForTheRecipe;

    }



    public Cursor getAllTableByCursorByfilterTableName(String tableName){
        Cursor cursor;
        cursor = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            cursor = db.query(tableName,null,null,null, null,null,null);
        } catch (Exception e) {
            e.getCause();
        }
        cursor.close();
        return cursor;
    }
/**/
    public ArrayList<Integer> returnRecipePositionInTheCursorThatMatchIngridientByArrayList( ArrayList<String> myIngridients){
        Cursor cursor = null;


        cursor = getAllRecipeByCursor();
        int ingridientColum = cursor.getColumnIndex(API_Constant.ingredients);

        cursor.moveToFirst();
        ArrayList<Integer> theList =new ArrayList<Integer>();;
           try {
               while (cursor.moveToNext()){
                   int counter=0;
                   ArrayList<String> recipeIngridientsList = new ArrayList<String>();

                   //handle the ingridients form the recipe intoo arraylist;
               String in = cursor.getString(ingridientColum);
               JSONArray object = new JSONArray(in);
               for (int i = 0; i < object.length(); i++) {
                   recipeIngridientsList.add(object.getString(i));
               }

               for (int i = 0; i < myIngridients.size() ; i++) {



                  for (int j = 0; j < recipeIngridientsList.size() ; j++) {
                      String recipeOneIngridient = recipeIngridientsList.get(j);
                      String myOneIngridient = myIngridients.get(i);

                      if (recipeOneIngridient.indexOf(myOneIngridient)>-1){
                               counter++;
                               continue;
                      }
                   }
                       // if the recipe includ all my ingirdients so take the position;
                    if(counter==recipeIngridientsList.size())
                        theList.add(cursor.getPosition());
                        continue;

                   }
               }
           }catch (JSONException e){
               e.getCause();
           }

        return theList;
    }

    public ArrayList<String> getAllProductsFromTempString(String temp){
        ArrayList<String> list = new ArrayList<String>();
        String[] splits = temp.split(",");
        for (int i = 0; i < splits.length ; i++) {
            list.add(splits[i]);
        }
        return list;
    }

    public ArrayList<String> getAllIngridientsAPIByArrayList(){
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = null;
        //String[] productsColum = {API_Constant.productName};
        SQLiteDatabase db = helper.getReadableDatabase();
        try {

            cursor = db.query(Constans.DB_TBALE_PRODUCT,null,null,null, null,null,null);
        } catch (Exception e) {
            e.getCause();
        }finally {

        }
        int productNamecolum = cursor.getColumnIndex(API_Constant.productName);
        cursor.moveToFirst();

       for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
             list.add(cursor.getString(productNamecolum));
        }

        cursor.close();
        return list;
    }








    public boolean isUnique(String wicthDBTable,String ColumName,String whatToComper){
        boolean flag = true;
        boolean isUnique = true;
        Cursor cursor1 = null;
        cursor1 = getAllTableByCursorByfilterTableName(Constans.DB_TABLE_RECIPE);
        String someThingInTheDB ="";
        int columName = cursor1.getColumnIndex(ColumName);

        for(cursor1.moveToFirst(); cursor1.isAfterLast()!= true;cursor1.moveToNext()){

            someThingInTheDB = cursor1.getString(columName);
            if (someThingInTheDB.equals(whatToComper)) {
                isUnique = false;
                break;
            }

        }

        return isUnique; // is there is same name it will return flase - not uniuqe, true for yes unique;
    }

    public ArrayList<String> aoutocompleteByArrayList(String input){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<String> list = new ArrayList<String>();
        String selections;
        selections =API_Constant.productName+" LIKE ?"; /*"+"'%" + input + "%'"*/
        String[] argu = {"'%" + input + "%'"};
        String[] colums = {API_Constant.productName};
        String orderBy =API_Constant.productName;

        String in = "'%"+input+"%'";
        String query = " SELECT "+ API_Constant.productName +" FROM "+ Constans.product +" WHERE "  +API_Constant.productName +" LIKE " + in + " ORDER BY "+  API_Constant.productName;

        try {
            cursor  = db.rawQuery(query,null);
           //cursor = db.query(Constans.DB_TBALE_PRODUCT,colums,selections,argu,null,null, orderBy);
           //, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        }catch (SQLiteException e){
            e.getCause();
        }finally {

        }

        while (cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex(API_Constant.productName)));
        }
        return list;
    }

    public  boolean isRecipeNameUnique(String recipeName){
        boolean flag = true;
        boolean isUnique = true;
        Cursor cursor1 = getAllRecipeByCursor();
        int recipeNameColum = cursor1.getColumnIndex(API_Constant.recipeName);
        String recipeNameFromTheDB="";



       for(cursor1.moveToFirst(); cursor1.isAfterLast()!= true;cursor1.moveToNext()){

            recipeNameFromTheDB = cursor1.getString(recipeNameColum);
            if (recipeNameFromTheDB.equals(recipeName)) {
                isUnique = false;
                break;
            }

        }

        if(!cursor1.isClosed())
            cursor1.close();

        return isUnique; // is there is same name it will return flase - not uniuqe, true for yes unique;
    }

    public boolean isRecipeIdUnique(int recipeId){
        boolean flag = true;
        Cursor cursor1 = getAllTableByCursorByfilterTableName(Constans.DB_TABLE_RECIPE);
        int recipeIdFromList=0;

        int recipeIdColum = cursor1.getColumnIndex(API_Constant.recipeId);
        cursor1.moveToFirst();
        flag = cursor1.moveToFirst();
       while (flag){

             try {
                 recipeIdFromList = cursor1.getInt(recipeIdColum);  // it's try becuase some of the recipe are not with ID!;
             }catch (Exception e){
                 e.getCause();
             }
            if (recipeIdFromList==recipeId){
                flag = false;
                break;
            }
          if (flag = cursor1.moveToNext())
             cursor1.moveToNext();
           else
              break;
        }
        cursor1.close();
         return flag;
    }

    public Cursor getAllRecipeByCursor(){

        Cursor getAllCursor =null;
        SQLiteDatabase db = null;
        db = helper.getReadableDatabase();
        try {

            getAllCursor = db.query(Constans.DB_TABLE_RECIPE,null,null,null, null,null,null);

        } catch (Exception e) {
            e.getCause();
        }


        return getAllCursor;
    }



    public int getPositonCursorRecipeWithRecpieName(String recipeName){

       Cursor cursor = null ;


        cursor = getAllRecipeByCursor();//getAllTableByCursorByfilterTableName(Constans.DB_TABLE_RECIPE);

        int position = API_Constant.fail;
        int recipeNameColum = cursor.getColumnIndex(API_Constant.recipeName);

        for(cursor.moveToFirst();cursor.isAfterLast()!=true; cursor.moveToNext()){
           String str = cursor.getString(recipeNameColum);
            try{
                if (cursor.getString(recipeNameColum).equals(recipeName)) {
                    position = cursor.getPosition();
                    break;
                }
            }catch (Exception e){
                e.getCause();
            }
        }


        return position;
    }

public ArrayList<String> getAllIngridientsFromMyFrigeByArrayList(String myFrigeId){
    SQLiteDatabase db = helper.getReadableDatabase();
    ArrayList<String> list = new ArrayList<String>();
     Cursor cursor = null;
    try {
        cursor = db.query(Constans.DB_TBALE_MYFRIGE,null,null,null, null,null,null);
    }catch (SQLiteException e) {
        e.getCause();
    }finally {

    }
    int myFrigeColum = cursor.getColumnIndex(API_Constant.MyFrigeId);
    int myFrigeIngridientsColum = cursor.getColumnIndex(API_Constant.producs );
    for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
      String my= cursor.getString(myFrigeColum);
        if (my.equals(myFrigeId)){
            String ingridients = cursor.getString(myFrigeIngridientsColum);
            String[] ingri = ingridients.split(",");// the ingridients will get in as a all string with concut.
            for (int i = 0; i < ingri.length ; i++) {
                list.add(ingri[i]);
            }

        }

    }
    return list;
}


    public void addIngridientsToMyFrigeTable(ContentValues values){

        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.insert(Constans.DB_TBALE_MYFRIGE,null,values);
        }catch (SQLiteException e){
            e.getCause();
        }finally {
            if (db.isOpen())
                db.close();
        }
    }

    public ArrayList<String> getAllRecipeIngredientsFilterIdByArrayList (int recipeId){
        ArrayList<String> list = new ArrayList<String>();
        Cursor ingredientsCursor = null;
        /*SQLiteDatabase db = helper.getReadableDatabase();
        try {
            ingredientsCursor =db.query(Constans.DB_TABLE_RECIPE,null,null,null, null,null,null);
        } catch (Exception e) {
            e.getCause();
        }finally {
            if ((db.isOpen()))
                db.close();
        }*/
        ingredientsCursor = getAllTableByCursorByfilterTableName(Constans.DB_TABLE_RECIPE);
        int columIngredients = ingredientsCursor.getColumnIndex(API_Constant.ingredients);
        int coloumRecipeId = ingredientsCursor.getColumnIndex(API_Constant.recipeId);
        int id = ingredientsCursor.getInt(coloumRecipeId);
        int position = 0;
        for (int i = 0; i < ingredientsCursor.getCount(); i++) {
        ingredientsCursor.moveToPosition(i);

            if (id==recipeId)
                position = i;
            break;
        }
        ingredientsCursor.moveToPosition(position);
        String strIngredientsArray = ingredientsCursor.getString(columIngredients);
        try {
            JSONArray ingredientsArray = new JSONArray(strIngredientsArray);
            for (int i = 0; i < ingredientsArray.length() ; i++) {
                JSONObject oneOfTheIngrident = ingredientsArray.getJSONObject(i);
                String oneIngrident = oneOfTheIngrident.getString(String.valueOf(i));
                list.add(oneIngrident);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
        }

    public int findTheIntegerInString(String str){
        String strInt = "";
        for (int i = 0; i < str.length(); i++) {
            if(Character.isDigit(str.charAt(i)))
                strInt +=str.charAt(i);
        }
        try {
            return Integer.parseInt(strInt);
        }catch (Exception e){
            e.getCause();
        }
        return Constans.fail;
    }

    public String findTheIntegerInStringReturnString(String str){
        String strInt = "";
        for (int i = 0; i < str.length(); i++) {
            if(Character.isDigit(str.charAt(i)))
                strInt +=str.charAt(i);
        }
        try {
            return strInt;
        }catch (Exception e){
            e.getCause();
        }
        return "noID";
    }

    public void addApiIngridients(ContentValues v) {

        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.insert(Constans.DB_TBALE_PRODUCT,null,v);
        }catch (SQLiteException e){
            e.getCause();
        }finally {
            if (db.isOpen())
                db.close();
        }

    }
}
