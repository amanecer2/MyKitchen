package com.amanecer.themykitchen.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amanecer.themykitchen.db.DBHandler;
import com.amanecer.themykitchen.networking.API_Constant;
import com.amanecer.themykitchen.obj.IngridientsAddingObj;
import com.amanecer.themykithcen.R;

import java.util.ArrayList;

import static android.R.layout.simple_dropdown_item_1line;

/**
 * Created by amanecer on 02/12/2014.
 */
public class FindIngridient extends Activity {

    EditText txt_typeIngridient;

    AutoCompleteTextView autoCompleteTextView;

    ImageButton imgBtn_addIngridient;
    ImageButton searchRecipeByIngridients;

    ListView listViewAutocomplte;
    ListView listViewIngridients;

    ArrayList<String> listMyFrige;
    ArrayList<String> listAutocomplte;

    ArrayAdapter<String> adapterAutoComleate;
    ArrayAdapter<String> adapterIngridients;

    Custome_BaseAdapter_IngridientsAdding adapter_ingridientsAdding;
    ArrayList<IngridientsAddingObj> listOfObj;
    IngridientsAddingObj ingridientsAddingObj;


    DBHandler handler;
    /*Custome_Autocomplte autocomplte;*/
   private String textChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_ingridient_activity);

        ingridientsAddingObj = new IngridientsAddingObj();
        handler = new DBHandler(FindIngridient.this);

       /* autocomplte = new Custome_Autocomplte(this);*/
        textChange = "";

        // new the lists;
        listMyFrige      = new ArrayList<String>();
        listAutocomplte  = new ArrayList<String>();
        listAutocomplte = handler.getAllIngridientsAPIByArrayList();
        listOfObj = new ArrayList<IngridientsAddingObj>();

        //handle the autocompleate;
        autoCompleteTextView =(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        //listViewAutocomplte = (ListView)findViewById(R.id.listViewAutocomplte);
        adapterAutoComleate = new ArrayAdapter<String>(this, simple_dropdown_item_1line, listAutocomplte);
        autoCompleteTextView.setAdapter(adapterAutoComleate);
        autoCompleteTextView.setThreshold(2);

        // handle the customeList view
        listViewIngridients = (ListView)findViewById(R.id.listViewIngridients);
        adapter_ingridientsAdding = new Custome_BaseAdapter_IngridientsAdding(this,listOfObj);
        listViewIngridients.setAdapter(adapter_ingridientsAdding);

        //handle the listView of my frige ingridients;
        /*listViewIngridients = (ListView)findViewById(R.id.listViewIngridients);
        adapterIngridients =  new ArrayAdapter<String>(this, simple_list_item_1, listMyFrige);
        listViewIngridients.setAdapter(adapterIngridients);*/



       // handle  press on the ingridient for delete it;
        listViewIngridients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id==R.id.IngridientName){
                    listOfObj.remove(position);
                    adapter_ingridientsAdding.notifyDataSetChanged();

                }


            }
        });










        //handler the ingridients adding;

       imgBtn_addIngridient = (ImageButton)findViewById(R.id.imgBtn_addIngridient);
        imgBtn_addIngridient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = autoCompleteTextView.getText().toString();
                if (!s.equals("")) {
                /*listMyFrige.add(s);
                adapterIngridients.notifyDataSetChanged();*/
                    ingridientsAddingObj = new IngridientsAddingObj(s, false);
                    listOfObj.add(ingridientsAddingObj);
                    adapter_ingridientsAdding.notifyDataSetChanged();
                    autoCompleteTextView.setText("");
                }
            }
        });




        //handle the list and saveinto the DATABASE---myfrige, recored the id; and intent to the next activity;
        searchRecipeByIngridients = (ImageButton)findViewById(R.id.searchRecipeByIngridients);
        searchRecipeByIngridients.setOnClickListener(new View.OnClickListener() {// when pressed add all the ingridient that showed in the listview and serach for recipes;
            @Override
            public void onClick(View v) {
                //check for internet conection;
                if (haveNetworkConnection(FindIngridient.this)){
                    ContentValues values = new ContentValues();
                    Cursor cursor = null;
                    int id=0;
                    String temp ="";
                    String whatToSearch="";
                    String onlySpecificsIngridients="";
                    String notSpecificIngridients="";

                /*for (int i = 0; i < listMyFrige.size(); i++) {
                    temp += listMyFrige.get(i)+",";
                }*/

                    for (int i =0; i<listOfObj.size();i++){
                        String value = listOfObj.get(i).getValue();

                        if(value.indexOf(API_Constant.yummi_specific_ingredient)>-1){
                            onlySpecificsIngridients+= listOfObj.get(i).getValue()+"&20";
                        }else {
                            notSpecificIngridients+=listOfObj.get(i).getValue()+"&20";
                        }

                    }

                    whatToSearch = notSpecificIngridients+onlySpecificsIngridients;

                    for (int i = 0; i < listOfObj.size() ; i++) {
                        temp += listOfObj.get(i).getIngridientName()+",";
                    }

                    values.put(API_Constant.producs,temp);

                    handler.addProductToMyFrige(values);

                    Intent search = new Intent(FindIngridient.this,RecipesMaths.class);
                    search.putExtra(API_Constant.producs,temp); // will send the id of the my frige products;
                    search.putExtra(API_Constant.whatToSearch,whatToSearch);
                    startActivity(search);

                }else {
                    AlertDialog.Builder noCon =new AlertDialog.Builder(FindIngridient.this);
                    noCon.setTitle(getString(R.string.noConnectionTitle));
                    noCon.setMessage(getString(R.string.noConcction));
                    noCon.setCancelable(true);
                    noCon.show();
                }

            }
        });
    }

    private boolean haveNetworkConnection(Activity activity) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                   // if (handler.isInternetAvailable())
                        haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                   // if (handler.isInternetAvailable())
                        haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
