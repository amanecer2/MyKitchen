package com.amanecer.themykitchen.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanecer.themykitchen.db.Constans;
import com.amanecer.themykitchen.db.DBHandler;
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

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by amanecer on 08/12/2014.
 */
public class Asyc_API_GetRecipeById extends AsyncTask<String ,Integer,String> {

    DBHandler handler;
    ProgressDialog dialog;
    Asyc_Pic asyc_pic;

    Activity activity;

    ImageView   recipeImg;
    ImageButton btnUrl;
    TextView    recipeName;
    TextView    rate;
    TextView    numberOfServing;
    TextView    txt_ingridients;
    TextView    Sweet;
    TextView    Salty;
    TextView    Bitter;
    TextView    Sour;
    TextView    Meaty;
    TextView    Piquant;
    TextView txt_theRecipeTotalTime;
    String recipeID;

    public Asyc_API_GetRecipeById(Activity activity) {
        this.activity = activity;
        handler = new DBHandler(activity);
        asyc_pic = new Asyc_Pic(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        xmlBunding();
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("");
        dialog.setTitle("");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String response = sendHttpRequest(params[0]);
        recipeID = params[1];
        searchRecipeByIdAndAddToTable(response);
      return response;



    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

       postOnTheActivityByResponse(response);


        dialog.dismiss();
    }


    private void xmlBunding(){
        //recipeImg       = (ImageView)activity.findViewById(R.id.img_theRecipeImg);
        btnUrl          = (ImageButton)activity.findViewById(R.id.imgBtn_searcRecipes);    ;
        recipeName      = (TextView)activity.findViewById(R.id.txt_theRecipeName);   ;
        rate          =  (TextView)activity.findViewById(R.id.txt_theRecipeRating);    ;
        numberOfServing = (TextView)activity.findViewById(R.id.txt_theRecipeNumberOfserving);    ;
        txt_ingridients      =(TextView) activity.findViewById(R.id.txt_theRecipeIngridients);   ;
        txt_ingridients.setMovementMethod(new ScrollingMovementMethod());
        Sweet            = (TextView)activity.findViewById(R.id.txt_flavor_Sweet);   ;
        Salty           = (TextView)activity.findViewById(R.id.txt_flavor_Salty);    ;
        Bitter         = (TextView)activity.findViewById(R.id.txt_falvor_Bitter);     ;
        Sour            = (TextView)activity.findViewById(R.id.txt_flavor_Sour);    ;
        Meaty          = (TextView)activity.findViewById(R.id.txt_flavor_Meaty);     ;
        Piquant         =(TextView) activity.findViewById(R.id.txt_flavor_Piquant);    ;
        txt_theRecipeTotalTime = (TextView)activity.findViewById(R.id.txt_theRecipeTotalTime);
    }

    private  boolean postOnTheActivityByResponse(String response){
        String hostedMediumUrl="";
        try {
            JSONObject ob = new JSONObject(response);

            txt_theRecipeTotalTime.setText(ob.getString("totalTime"));// by min;

            try {
                JSONArray images = ob.getJSONArray("images");
                JSONObject oneImg = images.getJSONObject(0);
                //JSONObject imageUrlsBySize = oneImg.getJSONObject("imageUrlsBySize");
                hostedMediumUrl = oneImg.getString("hostedMediumUrl");
                asyc_pic.execute(hostedMediumUrl);// set the img;
            }catch (JSONException e){
                e.getCause();
            }


            recipeName.setText(ob.getString("name"));

            JSONObject source = ob.getJSONObject("source");
            String sourceRecipeUrl = source.getString("sourceRecipeUrl");// this is the url for webpage;
            /*btnUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // intent internet! menifest;
                }
            });*/

            JSONArray ingredientLines  = ob.getJSONArray("ingredientLines");
            String text="";
            for (int i = 0; i <ingredientLines.length() ; i++) {
                text+= ingredientLines.getString(i)+"\n";
            }
            txt_ingridients.setText(text);
            int numberOfServings = ob.getInt("numberOfServings");
            numberOfServing.setText(""+numberOfServings);

            JSONObject flavors = ob.getJSONObject("flavors");

                double sw = flavors.getDouble("Sweet");
                double p  = flavors.getDouble("Piquant");
                double b  = flavors.getDouble("Bitter");
                double so = flavors.getDouble("Sour");
                double m  = flavors.getDouble("Meaty");
                double sa = flavors.getDouble("Salty");



            Sweet.setText   (String.valueOf(sw));
            Salty.setText   (String.valueOf(sa));;
            Bitter.setText  (String.valueOf(b));;
            Sour.setText    (String.valueOf(so));
            Meaty.setText   (String.valueOf(m));;
            Piquant.setText (String.valueOf(p));;

        }catch (JSONException e){
            e.getCause();
        }



        return true;
    }


    private  boolean postOnActivity(String arrToString){
        boolean flag = true;

        Cursor cursor = null;
        cursor = handler.getAllTableByCursorByfilterTableName(Constans.DB_TABLE_RECIPEWithDetails);

        // all of the above are "toString" need to deString to json;
        int recipeIdColum = cursor.getColumnIndex(API_Constant.recipeId);
        int recipeNameColum = cursor.getColumnIndex(API_Constant.recipeName);
        int totalTimeColum = cursor.getColumnIndex(API_Constant.totalTimeInSeconds);
        int ingridientsColum = cursor.getColumnIndex(API_Constant.ingredients);
        int flavorColum = cursor.getColumnIndex(API_Constant.flavors); //
        int imgColum = cursor.getColumnIndex(API_Constant.images);
        int urlColum = cursor.getColumnIndex(API_Constant.source);
        int ratingColum = cursor.getColumnIndex(API_Constant.rating);
        int postion = 0;
        cursor.moveToNext();
        while (cursor.moveToNext()) {
            if (cursor.getString(recipeIdColum).equals(recipeID)) {
                postion = cursor.getPosition();
                break;
            }
        }
        cursor.moveToPosition(postion);
        String recipeName = cursor.getString(recipeNameColum);
        String indridientsInToString = cursor.getString(ingridientsColum);
        // need to un string to json object;
        // and after that to list of Strings;
        try {
            JSONObject ingri = new JSONObject(indridientsInToString);
            ;
            JSONArray in = ingri.getJSONArray("ingredientLines");
            String[] ingridientsList = new String[in.length()];
            String text ="";
            for (int i = 0; i <in.length() ; i++) {
                ingridientsList[i] = in.getString(i) ;
                text+="*"+in.getString(i)+"\n";
            }
            txt_ingridients.setText(text);
        }catch (JSONException e){
            e.getCause();
        }


        int rating  = cursor.getInt(ratingColum);
        rate.setText(rating);

        String flavorInTostring = cursor.getString(flavorColum);
        try{
            JSONObject flavorsJSOn = new JSONObject(flavorInTostring);
            Sweet.setText(flavorsJSOn.getInt(API_Constant.sweet));
            Salty.setText(flavorsJSOn.getInt(API_Constant.salty));;
            Bitter.setText(flavorsJSOn.getInt(API_Constant.bitter));;
            Sour.setText(flavorsJSOn.getInt(API_Constant.sour));;
            Meaty.setText(flavorsJSOn.getInt(API_Constant.meaty));;
            Piquant.setText(flavorsJSOn.getInt(API_Constant.piquant));;
        }catch (JSONException e){
            e.getCause();
        }

        int numberOfServingFromSql = cursor.getInt(totalTimeColum);
        numberOfServing.setText(numberOfServingFromSql);

        String img = cursor.getString(imgColum);          // we need the    sourceRecipeUrl object;
        try{
            JSONObject jsonImg = new JSONObject(img);
            JSONArray imgsArr = jsonImg.getJSONArray("images");
            JSONObject imgs = imgsArr.optJSONObject(0);
            JSONObject imageUrlsBySize = imgs.getJSONObject("imageUrlsBySize");
            String hostedMediumUrl = imageUrlsBySize.getString("hostedMediumUrl");

            asyc_pic.execute(hostedMediumUrl);

        }catch (JSONException e){
            e.getCause();
        }
        String url = cursor.getString(urlColum);
        try {
            JSONObject source = new JSONObject(url);
            String sourceRecipeUrl = source.getString("sourceRecipeUrl");
            btnUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }catch (JSONException e){
            e.getCause();
        }
        return flag;
    }


    private boolean searchRecipeByIdAndAddToTable(String response){
        boolean flag = true;
        try {
            ContentValues v = new ContentValues();

            JSONObject object = new JSONObject(response);
            JSONArray nutritionEstimates = object.getJSONArray("nutritionEstimates");
            String nutritionEstimate = nutritionEstimates.toString();
            v.put(API_Constant.nutritionEstimates,nutritionEstimate);

            int totalTime = object.getInt("totalTimeInSeconds"); // maybe null;

           // txt_theRecipeTotalTime.setText(String.valueOf(totalTime));
            v.put(API_Constant.totalTimeInSeconds,totalTime);

            JSONArray images = object.getJSONArray("images");
            String image = images.toString();
            v.put(API_Constant.images,image);
           /* JSONObject imgs = images.getJSONObject(0);
            JSONObject imageUrlsBySize = imgs.getJSONObject("imageUrlsBySize");
            String hostedMediumUrl = imageUrlsBySize.getString("hostedMediumUrl");
            asyc_pic.execute(hostedMediumUrl);*/


            String name = object.getString("name");
            v.put(API_Constant.recipeName,name);
           // recipeName.setText(name);

            JSONObject source = object.getJSONObject("source");
            String PATH = source.toString();
            v.put(API_Constant.PATH,PATH);
           // String sourceRecipeUrl = source.getString("sourceRecipeUrl");
            /*btnUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent web = new Intent(); // go to v web page or to a activity with webView!;
                }
            });*/

            String id = object.getString("id");
            v.put(API_Constant.recipeId,id);

            JSONArray ingredientLines = object.getJSONArray("ingredientLines");
            String ingredientLine = ingredientLines.toString();
            v.put(API_Constant.ingredients,ingredientLine);
            String recipeIngredients="";
          /* String text="";
            for (int i = 0; i < ingredientLines.length() ; i++) {
                text+= ingredientLines.getString(i)+"\n";
            }
            txt_ingridients.setText(text);
*/
           // int numberOfServings = object.getInt("numberOfServings");  // need to add in the CREATE Table.. there is no colum;
            ///v.put(API_Constant.numberOfServings,numberOfServings);
          //  numberOfServing.setText(numberOfServings);



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
           // rate.setText(""+rating);
            handler.addRecipeWithDetails(v);

        }catch (Exception e){
            e.getCause();
            flag = false;
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
            if(httpCon.getResponseCode() != HTTP_OK){
                Log.e("", "Cannot Connect to : " + urlString + "/n code:" + httpCon.getResponseCode());
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
