package com.amanecer.themykitchen.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.amanecer.themykitchen.db.Constans;
import com.amanecer.themykitchen.db.DBHandler;
import com.amanecer.themykitchen.networking.API_Constant;
import com.amanecer.themykitchen.networking.Asyc_API;
import com.amanecer.themykithcen.R;

import java.util.Locale;


public class My  extends Activity {

    Asyc_API asyc_api;
    Button searchByIngridients;
    SharedPreferences preferences;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recipe);
        preferences = getSharedPreferences(Constans.myPref,MODE_PRIVATE);
        setLocale();



    }

    public void setLocale(){    //to set programly the language;
        // get from shard the configuration by the key="language"-->iw or en;
        language= preferences.getString(Constans.language,Constans.iw);

        ///handle Locle object;
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        //handle configuration object;
        Configuration configuration = new Configuration();
        configuration.locale = locale;

        //update the Locale configuration;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        // listener; the listener just do a   onStrat to the layout;
        onConfigurationChanged(configuration);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {

        onStart();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();



        setContentView(R.layout.my_kitchen);
        DBHandler handler = new DBHandler(this);

        // at the first time we need to dwonload all the ingridiet to the API

        cheekIfHaveAllIngridients();// dwonload the ingridients, and if they are exixtes so dont.. only in the begianing;



        searchByIngridients = (Button)findViewById(R.id.homePage_searchByIngridients);

        searchByIngridients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(My.this, FindIngridient.class);
                startActivity(intent);
            }
        });

        Button set = (Button)findViewById(R.id.setting);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My.this,Setting.class);
                startActivityForResult(intent,Constans.resultCode);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void cheekIfHaveAllIngridients(){
        SharedPreferences.Editor editor = preferences.edit();
        Boolean config= false;
        config =  preferences.getBoolean(Constans.gotAllIngridients,true); // if there is not -config = true. have -- config = false;
        if (config) {
            asyc_api = new Asyc_API(My.this);
            String url = API_Constant.getYummi_ingridients;
            String method = API_Constant.method_getAllIngridientAPI;
            String[] send = {method, url};
            asyc_api.execute(send);
            editor.putBoolean(Constans.gotAllIngridients, false);
            editor.commit();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setLocale();


    }



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
        if (id == id){
            Intent intent = new Intent(My.this, Setting.class);
            startActivity(intent);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}


