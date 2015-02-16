package com.amanecer.themykitchen.networking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by amanecer on 14/12/2014.
 */
public class Asyc_API_SearchRecipes extends AsyncTask<String,Integer,String> {

    DBHandler handler;
    Activity activity;

    ProgressDialog dialog;
    Custome_BaseAdapter_Recipes custome_baseAdapter_recipes;

    ListView listView_recipesFuond;
    static ArrayList<Recipes> list;

    String ingridientsFromTheUI;
    String[] ingridients;

    Resources resources;

    public Asyc_API_SearchRecipes(Activity activity) {
        this.activity = activity;
        handler = new DBHandler(activity);
        resources= activity.getResources();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity, ProgressDialog.STYLE_HORIZONTAL);
        //String title = resources.getString(R.string.Searching);
        //String message = resources.getString(R.string.searchingForRecipe);
        dialog.setMessage(API_Constant.searchingForRecipe);
        dialog.setTitle(API_Constant.Searching);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];

        String ingridients = params[1];


        String response = sendHttpRequest(url);
        if (response.equals(""))
            ingridients= API_Constant.noConecction;
        if (searchRecpiesAndAddToTable(response)==false){
            ingridients = API_Constant.recipeNoMath;
        }
        return ingridients;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String myFrigeIngridients) {
        super.onPostExecute(myFrigeIngridients);
        AlertDialog.Builder builderNoRecipe =new AlertDialog.Builder(activity);
        AlertDialog.Builder builderNoConet =new AlertDialog.Builder(activity);

        if (myFrigeIngridients.equals(API_Constant.recipeNoMath)) {
            dialog.dismiss();

            builderNoRecipe.setTitle(activity.getString(R.string.noRecipesFoundTitle));
            builderNoRecipe.setMessage(activity.getString(R.string.noRecipesFound));
            builderNoRecipe.setPositiveButton(activity.getString(R.string.goBack), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            });
            builderNoRecipe.show();

        }else if (myFrigeIngridients.equals(API_Constant.noConecction)){
            dialog.dismiss();

            builderNoConet.setTitle(activity.getString(R.string.noConcctionTitle));
            builderNoConet.setMessage(activity.getString(R.string.noConcction));
            builderNoConet.setPositiveButton(activity.getString(R.string.goBack), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            });
            builderNoConet.show();
        }else {
            //handle the listview
            list = new ArrayList<Recipes>();
             custome_baseAdapter_recipes = new Custome_BaseAdapter_Recipes(activity, list);
            listView_recipesFuond = (ListView) activity.findViewById(R.id.listView_recipesFuond);
            listView_recipesFuond.setAdapter(custome_baseAdapter_recipes);

            postRecipesOnListView(myFrigeIngridients);
            dialog.dismiss();
            listView_recipesFuond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(activity, "click bu API " + list.get(position).getRecipeName(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }


    public void postRecipesOnListView(String myFrigeIngridients) {
        Cursor cursor = null;

        //using this for comper ingridients with the recipe ingridients;
        ArrayList<String> ingridientsList = new ArrayList<String>();
        String[] ingridients = myFrigeIngridients.split(",");
        for (int i = 0; i < ingridients.length; i++) {
            ingridientsList.add(ingridients[i]);
        }


        cursor = handler.getAllRecipeByCursor();
        int ingridientColum = cursor.getColumnIndex(API_Constant.ingredients);
        int recipeNameColum = cursor.getColumnIndex(API_Constant.recipeName);
        int recipeIngridientsColum = cursor.getColumnIndex(API_Constant.producs);
        int recipePATHCulom = cursor.getColumnIndex(API_Constant.PATH);
        int recipeRating = cursor.getColumnIndex(API_Constant.rating);
        int recipeTotalTime = cursor.getColumnIndex(API_Constant.totalTimeInSeconds);
        int recipeIdcolum = cursor.getColumnIndex(API_Constant.recipeId);

        cursor.moveToFirst();
        try {
            while (cursor.moveToNext()) {

                ArrayList<String> recipeIngridientsList = new ArrayList<String>();
                //handle the ingridients form the recipe intoo arraylist;
                String in = cursor.getString(ingridientColum);
                JSONArray object = new JSONArray(in);
                for (int i = 0; i < object.length(); i++) {
                    recipeIngridientsList.add(object.getString(i));
                }

                if (handler.isHaveAllIngridientsForThisRecipe(ingridientsList,recipeIngridientsList)) {
                    String PATH = cursor.getString(recipePATHCulom);
                    int rating = cursor.getInt(recipeRating);
                    String recipeName = cursor.getString(recipeNameColum);
                    String describe = activity.getString(R.string.timeToMake)+" : " + cursor.getInt(recipeTotalTime) + "."+activity.getString(R.string.ingridients)+" : " + ingridientsFromTheUI;
                    String recipeId = cursor.getString(recipeIdcolum);
                    Recipes recipes = new Recipes(recipeName, describe, PATH, false, rating, recipeId);

                    list.add(recipes);
                }else
                    continue;
            }
        } catch (JSONException e) {
            e.getCause();
        }
        custome_baseAdapter_recipes.notifyDataSetChanged();
        if (custome_baseAdapter_recipes.isEmpty()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);


            String title = activity.getString(R.string.problem);
            String message = activity.getString(R.string.problemMessage);
            String goBack = activity.getString(R.string.goBack);
            builder1.setTitle(title);
            builder1.setMessage(message);
            builder1.setPositiveButton(goBack, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            });
            builder1.show();
        }

    }





    private boolean searchRecpiesAndAddToTable(String response){
        boolean flag = true;
        try {  // this json for search recipes.. need to add for recipe by id;
            JSONObject object = new JSONObject(response);
            JSONArray matches = object.getJSONArray(API_Constant.matches);
            if (matches.length() == 0){
                flag = false;
                 return false;
           }
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
            flag = false;
            return false;

            //there is no json with mathes; add to data base.
        }
        return flag;
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

            try {

                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("HEAD");
                con.setConnectTimeout(5000); //set timeout to 5 seconds
                con.getErrorStream();
                if(httpCon.getResponseCode() != HTTP_OK){
                    Log.e("", "Cannot Connect to : " + urlString + "/n code:" + httpCon.getResponseCode());
                    return "";
                }
            }catch (SocketException e){
                return "";
            }  catch (java.io.IOException e) {


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
