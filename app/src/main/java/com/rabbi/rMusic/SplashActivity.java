package com.rabbi.rMusic;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class SplashActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        
       Thread thread = new Thread(new Runnable(){

             @Override
             public void run() {
                try {
                   Thread.sleep(1000);
                   startActivity(new Intent(getApplicationContext(),MainActivity.class));                   
                   overridePendingTransition(0,0);
                   finish();
                } catch (InterruptedException e) {}
             }
                        
        });
        
        thread.start();
        
    }
    
}
