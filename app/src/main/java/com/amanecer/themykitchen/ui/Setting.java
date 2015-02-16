package com.amanecer.themykitchen.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.amanecer.themykithcen.R;


/**
 * Created by amanecer on 15/12/2014.
 */
@SuppressWarnings("ResourceType")
public class Setting extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.xml.setting);


    }
}
