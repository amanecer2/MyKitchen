package com.amanecer.themykitchen.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.amanecer.themykitchen.db.Constans;
import com.amanecer.themykitchen.db.DBHandler;
import com.amanecer.themykitchen.obj.Recipes;
import com.amanecer.themykitchen.ui.Custome_BaseAdapter_Recipes;

import com.amanecer.themykithcen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by amanecer on 19/11/2014.
 */




public class Asyc_API extends AsyncTask<String,Integer,String> {


    DBHandler handler;
    Activity activity;
    ProgressDialog dialog;


    ListView listView_recipesFuond;
    ArrayList<Recipes> list;

    String ingridientsFromTheUI;
    String[] ingridients;
    public Asyc_API(Activity act) {
            this.activity = act;
           handler = new DBHandler(act);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // i will use this for dwonload all the ingridient cuz it's take a lot of time
        // need to be run only once - sharde preference;

        dialog = new ProgressDialog(activity,ProgressDialog.STYLE_HORIZONTAL);
        //String message = Resources.getSystem().getString(R.string.abc_action_mode_done);

        dialog.setMessage(activity.getString(R.string.IngridientDwonload));
        dialog.setTitle(activity.getString(R.string.goingToBeAlot));
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();

    }

    @Override
    protected String doInBackground(String... urls) {
        String wichtMethod = urls[0];
        String url = urls[1];
        String whatToReturn = "";
        String ingridients="";
       try {
           ingridients = urls[2];
       }catch (Exception e) {
           e.getCause();
           //there is no urls[2
       }


        String response = "";
        response = sendHttpRequest(url);


        if (wichtMethod.equals(API_Constant.searchRecipes)){  // this search recipes and add to table recipe
            searchRecpiesAndAddToTable(response);
            whatToReturn = ingridients;
          }
      else if(wichtMethod.equals(API_Constant.searchRecipeById)) { // this seard a specific recipe by id and add to recpie and create a objects;
            searchRecipeByIdAndAddToTable(response);
            whatToReturn = wichtMethod;
        }
      else if (wichtMethod.equals(API_Constant.method_getAllIngridientAPI)) {
            searchAllIngridientsInTheAPIAddToTable(response);   // only in the begainign!!!!!!
            whatToReturn = wichtMethod;
        }

        return whatToReturn;

    }



    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String whatToReturn) {
          ingridientsFromTheUI = whatToReturn;
         ingridients = whatToReturn.split(",");
        if (whatToReturn.equals(API_Constant.method_getAllIngridientAPI)) {
            dialog.dismiss();
        }
        else if (ingridients!=null){
            postRecipesOnListView();
           dialog.dismiss();
         }
        else if (whatToReturn.equals(API_Constant.searchRecipeById)) {
            postOnTheRecipeActivity();
            dialog.dismiss();
        }


     /*   Cursor cursor1 = null;
        ArrayList<String> myList = new ArrayList<String>();
        myList.add("onion");myList.add("basil");myList.add("sugar");
        String recipeId = "Tomato_-olive_-and-mozzarella-salad-with-basil-vinaigrette-309565";
        String recipeName ="Tomato Salad";

        cursor1 = handler.getAllTableByCursorByfilterTableName(Constans.DB_TABLE_RECIPE);
        int pos = handler.getPositonCursorRecipeWithRecpieName(recipeName);
        if (pos!=API_Constant.fail) {
           // cursor1.moveToPosition(pos);
            handler.comperIngridientAndReturnTheMissingIngridientByRecipeName(recipeName, myList, pos);
        }*/
    }

    private void postOnTheRecipeActivity() {

    }


    public void postRecipesOnListView(){
        Cursor cursor = null;

        //using this for comper ingridients with the recipe ingridients;
        ArrayList<String> ingridientsList = new ArrayList<String>();
        for (int i = 0; i < ingridients.length ; i++) {
            ingridientsList.add(ingridients[i]);
        }


        //handke the listview
        list = new ArrayList<Recipes>();
        Custome_BaseAdapter_Recipes custome_baseAdapter_recipes = new Custome_BaseAdapter_Recipes(activity,list);
        listView_recipesFuond = (ListView)activity.findViewById(R.id.listView_recipesFuond);
        listView_recipesFuond.setAdapter(custome_baseAdapter_recipes);


        try {
            cursor = handler.getAllRecipeByCursor();
            int recipeNameColum =  cursor.getColumnIndex(API_Constant.recipeName);
            int recipeIngridientsColum = cursor.getColumnIndex(API_Constant.producs);
            int recipePATHCulom = cursor.getColumnIndex(API_Constant.PATH);
            int recipeRating = cursor.getColumnIndex(API_Constant.rating);
            int recipeTotalTime = cursor.getColumnIndex(API_Constant.totalTimeInSeconds);
            int recipeIdcolum = cursor.getColumnIndex(API_Constant.recipeId);
            cursor.moveToFirst();
            // making the arrayList<Recipes> for the listView;
            while (cursor.moveToNext()){

               String PATH =  cursor.getString(recipePATHCulom);
                int rating = cursor.getInt(recipeRating);
               String recipeName= cursor.getString(recipeNameColum);
               String describe = "Time : "+cursor.getInt(recipeTotalTime)+".Ingridients : "+ingridientsFromTheUI;

                String recipeId = cursor.getString(recipeIdcolum);

                Recipes recipes = new Recipes(recipeName,describe,PATH,false,rating,recipeId);
               // boolean haveAll = handler.comper_Must_HaveIngridientInRecpieWithMyFrigeByRecipeName(cursor.getString(recipeNameColum),ingridientsList);
                //recipes.setGotAllIngridients(haveAll);/// bullshit.. shomething went wrong in the method "must/....";
                list.add(recipes);
            }

        }catch (Exception e){
            e.getCause();
        }



    }


    public void searchAllIngridientsInTheAPIAddToTable(String response){

        String allIngridient="";
        ContentValues v =new ContentValues();
        int first = response.indexOf('[');// we look for the end of the "metadata";
        String js = "{\"data\":"+ response.substring(first-1,response.length()-3)+"}"; // we add to the begain the json correct;        js = js.substring(0,js.length()-2)+"}";// we add to the end "}" to make the json corret;


        try {
            JSONObject json = new JSONObject(js);
            JSONArray data = json.getJSONArray("data"); //{} its allways a json... than its array of data....;
            dialog.setMax(data.length());

            for (int i = 0; i < data.length() ; i++) {

                JSONObject one = data.getJSONObject(i);
                allIngridient = one.getString(API_Constant.searchValue); // workinign. need to be on the on create cus its lots of time...
                v.put(API_Constant.productName,allIngridient);
                handler.addApiIngridients(v);
                dialog.setProgress(i);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void searchRecipeByIdAndAddToTable(String response){

        try {
            ContentValues v = new ContentValues();

            JSONObject object = new JSONObject(response);
            JSONArray nutritionEstimates = object.getJSONArray("nutritionEstimates");
            String nutritionEstimate = nutritionEstimates.toString();
            v.put(API_Constant.nutritionEstimates,nutritionEstimate);

            int totalTime = object.getInt("totalTime"); // maybe null;
            v.put(API_Constant.totalTimeInSeconds,totalTime);

            JSONArray images = object.getJSONArray("images");
            String image = images.toString();
            v.put(API_Constant.images,image);

            String name = object.getString("name");
            v.put(API_Constant.recipeName,name);

            JSONObject source = object.getJSONObject("source");
            String PATH = source.toString();
            v.put(API_Constant.PATH,PATH);

            String id = object.getString("id");
            v.put(API_Constant.recipeId,id);

            JSONArray ingredientLines = object.getJSONArray("ingredientLines");
            String ingredientLine = ingredientLines.toString();
            v.put(API_Constant.ingredients,ingredientLine);
            String recipeIngredients="";
            for (int i = 0; i < ingredientLines.length() ; i++) {
                JSONObject in  = ingredientLines.getJSONObject(i);


            }


            int numberOfServings = object.getInt("numberOfServings");
            v.put(API_Constant.numberOfServings,numberOfServings);

           int totalTimeInSeconds = object.getInt("totalTimeInSeconds");
            v.put(API_Constant.totalTimeInSeconds,totalTimeInSeconds);

            JSONObject attributes = object.getJSONObject("attributes");
            try {
               JSONArray cuisine = attributes.getJSONArray("cuisine");
                String cui = cuisine.toString();
                v.put(API_Constant.cuisine,cui);
            }catch (Exception e){
                // there is no cuisine;
            }
            try {
                JSONArray course = attributes.getJSONArray("course");
                String coy = course.toString();
                v.put(API_Constant.course,coy);
            }catch (Exception e){
                //there is no course;
            }

            JSONObject flavors = object.getJSONObject("flavors");
            String fla = flavors.toString();
            v.put(API_Constant.flavors,fla);

            int rating = object.getInt("rating");
            v.put(API_Constant.rating,rating);
        }catch (Exception e){
            e.getCause();
        }


    }

    private void searchRecpiesAndAddToTable(String response){

        try {  // this json for search recipes.. need to add for recipe by id;
            JSONObject object = new JSONObject(response);
            JSONArray matches = object.getJSONArray(API_Constant.matches);

            for (int i=0 ; i<matches.length(); i++){
                ContentValues values = new ContentValues();
                String ingredient= "",smallImageUrl="",course="",cuisine="";
                JSONObject defualt = matches.getJSONObject(i);

                /*JSONObject imageUrlsBySize = defualt.getJSONObject(API_Constant.imageUrlsBySize);
                String src = imageUrlsBySize.toString();
                values.put(Constans.src,src);*/

                String recipeName = defualt.getString(API_Constant.recipeName);


               if( !handler.isRecipeNameUnique(recipeName))
                    continue;

                values.put(API_Constant.recipeName,recipeName);

                String id = defualt.getString(API_Constant.id);
                //String theId = handler.findTheIntegerInStringReturnString(id);
                values.put(API_Constant.recipeId, id);

                try {
                    JSONArray ingredients = defualt.getJSONArray(API_Constant.ingredients);
                    ingredient = ingredients.toString();
                    values.put(API_Constant.ingredients,ingredient);
                } catch (JSONException e){
                e.printStackTrace();
            }



                try {
                    JSONArray smallImageUrls = defualt.getJSONArray(API_Constant.smallImageUrls);
                    smallImageUrl = smallImageUrls.toString();
                    values.put(Constans.PATH, smallImageUrl);
                }catch (JSONException e){
                e.printStackTrace();
                 }

                try {
                    int totalTimeInSeconds = defualt.getInt(API_Constant.totalTimeInSeconds);
                    values.put(API_Constant.totalTimeInSeconds, totalTimeInSeconds);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                JSONObject attributes = defualt.getJSONObject(API_Constant.attributes); //
                try {
                    JSONArray courses = attributes.getJSONArray(API_Constant.course);//
                    course = courses.toString();
                    values.put(API_Constant.course,course);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // there is no course array
                }

                try {
                    JSONArray cuisines = attributes.getJSONArray(API_Constant.cuisine);//
                    cuisine = cuisines.toString();
                    values.put(API_Constant.cuisine,cuisine);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // there is no cuisine array
                }

                try {
                    if(defualt.getJSONObject(API_Constant.flavors)!=null){

                        JSONObject flavors = defualt.getJSONObject(API_Constant.flavors);

                        int piquant = flavors.getInt(API_Constant.piquant);
                        int meaty = flavors.getInt(API_Constant.meaty);
                        int sour = flavors.getInt(API_Constant.sour);
                        int bitter = flavors.getInt(API_Constant.bitter);
                        int salty = flavors.getInt(API_Constant.salty);
                        int sweet = flavors.getInt(API_Constant.sweet);

                        String fla = piquant+","+meaty+","+sour+","+bitter+","+salty+","+sweet+",";
                        values.put(API_Constant.flavors,fla);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }


                int rating = defualt.getInt(API_Constant.rating);
                values.put(API_Constant.rating,rating);

                handler.addRecipe(values);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            //there is no json with mathes; add to data base.
        }

    }


    private String sendHttpRequest(String urlString) {/// this method to get the json from the api;
        BufferedReader input = null;
        HttpURLConnection httpCon = null;
        InputStream input_stream =null;
        InputStreamReader input_stream_reader = null;
        StringBuilder response = new StringBuilder();
        try{
            URL url = new URL(urlString);
            httpCon = (HttpURLConnection)url.openConnection();
            if(httpCon.getResponseCode() != HTTP_OK){
                Log.e("", "Cannot Connect to : " + urlString+"/n code:"+httpCon.getResponseCode());
                return null;
            }

            input_stream = httpCon.getInputStream();
            input_stream_reader = new InputStreamReader(input_stream);
            input = new BufferedReader(input_stream_reader);
            String line ;
            while ((line = input.readLine())!= null){
                response.append(line +"\n");
            }



        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(input!=null){
                try {
                    input_stream_reader.close();
                    input_stream.close();
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(httpCon != null){
                    httpCon.disconnect();
                }
            }
        }
        return response.toString();
    }


}
