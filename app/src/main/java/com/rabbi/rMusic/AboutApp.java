package com.rabbi.rMusic;

import android.app.Activity;
import android.os.Bundle;

public class AboutApp extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
}
