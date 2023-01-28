package com.rabbi.rMusic;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class PlayMusic extends Activity implements View.OnClickListener{
      
    private MediaPlayer mp;
    private ImageView playBtn,pauseBtn,forwardBtn,backwardBtn,nextBtn,prevBtn;
    private SeekBar seekbar;
    private boolean is_music_change = false;
    
    private TextView now_playing;
    private Thread update_seekbar;
    
   private String current_song;
   private ArrayList<File> song_list;
   private int position;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        current_song = bundle.getString("current_song");
        song_list = bundle.getParcelableArrayList("song_list");
        position = bundle.getInt("position");
        
        setContentView(R.layout.play_music);
        
        
        
        playBtn = findViewById(R.id.play);
        forwardBtn = findViewById(R.id.forward);
        backwardBtn = findViewById(R.id.backward);
        nextBtn = findViewById(R.id.next);
        prevBtn = findViewById(R.id.prev);
        seekbar = findViewById(R.id.seekbar);
        now_playing = findViewById(R.id.current_song);
        
        now_playing.setText(current_song);
        
        playBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        backwardBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        
        
        if(mp != null){
           Toast.makeText(getApplication(), "Not Null", Toast.LENGTH_SHORT).show();
        }
        
        Uri uri = Uri.parse(song_list.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),uri);
        mp.start();
        
        seekbar.setMax(mp.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

             @Override
             public void onProgressChanged(SeekBar seekbar, int progress, boolean isUserChange) {
                if(isUserChange){
                   mp.seekTo(progress);
                }
             }

             @Override
             public void onStartTrackingTouch(SeekBar sb) {
                  mp.seekTo(sb.getProgress());
             }

             @Override
             public void onStopTrackingTouch(SeekBar sb) {
                //mp.seekTo(sb.getProgress());
                // Play Next Song Automatically When User Skip To End of Music
                if(sb.getProgress() == mp.getDuration()){                   
                   mp.stop();
                   mp.release();
                   seekbar.setProgress(0);          
                   if(position < song_list.size()-1){
                      position = position+1;
                   }
                   else{
                      position = 0;             
                   }

                   Uri uri = Uri.parse(song_list.get(position).toString());
                   current_song = song_list.get(position).getName().toString();
                   now_playing.setText(current_song);
                   mp = MediaPlayer.create(getApplicationContext(),uri);
                   mp.start();
                   seekbar.setMax(mp.getDuration());
                }
             }
                        
        });
        
        // Update Seekbar
        update_seekbar = new Thread(new Runnable(){

             public void run() {
                int currentPosition = 0;
                try{
                   while(currentPosition<mp.getDuration()){
                      currentPosition = mp.getCurrentPosition();
                      seekbar.setProgress(currentPosition);
                      Thread.sleep(800);
                   }
                   
                }
                catch (Exception e){
                   //Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                   e.printStackTrace();
                }
             }
           
        });
       update_seekbar.start();
       
       // Play Next Song Automatically
       mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
       mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
             @Override
             public void onCompletion(MediaPlayer mediaPlayer) {
                try {

                   mp.stop();
                   mp.release();
                   seekbar.setProgress(0);          
                   if(position < song_list.size()-1){
                      position = position+1;
                   }
                   else{
                      position = 0;             
                   }

                   Uri uri = Uri.parse(song_list.get(position).toString());
                   current_song = song_list.get(position).getName().toString();
                   now_playing.setText(current_song);
                   mp = MediaPlayer.create(getApplicationContext(),uri);
                   mp.start();
                   seekbar.setMax(mp.getDuration());
                   
                } catch (Exception e) {
                   e.printStackTrace();
                }
             }
          });
       
    }

    @Override
    public void onClick(View btn) {
       // Play-Pause Music
       if(btn.getId() == R.id.play){
          if(mp.isPlaying()){             
             mp.pause();                          
             playBtn.setImageResource(R.drawable.play_white);             
          }
          else{            
            mp.start();
            playBtn.setImageResource(R.drawable.pause_white);                                                                  
          }
       }
       
       // Skip To Forward 
       else if(btn.getId() == R.id.forward){
          mp.seekTo(mp.getCurrentPosition()+5000);
       }
       
       // Skip To Backward
       else if(btn.getId() == R.id.backward){
          mp.seekTo(mp.getCurrentPosition()-5000);
       }
       
       // Play Next Music
       else if(btn.getId() == R.id.next){                             
          mp.stop();
          mp.release();
          seekbar.setProgress(0);          
          if(position < song_list.size()-1){
             position = position+1;
          }
          else{
             position = 0;             
          }
          
          Uri uri = Uri.parse(song_list.get(position).toString());
          current_song = song_list.get(position).getName().toString();
          now_playing.setText(current_song);
          mp = MediaPlayer.create(getApplicationContext(),uri);
          mp.start();
          seekbar.setMax(mp.getDuration());
          
       }
       
       // Play Previous Music
       else if(btn.getId() == R.id.prev){
          mp.stop();
          mp.release();
          seekbar.setProgress(0);          
          if(position > 0){
             position = position-1;
          }
          else{
             position = song_list.size()-1;             
          }

          Uri uri = Uri.parse(song_list.get(position).toString());
          current_song = song_list.get(position).getName().toString();
          now_playing.setText(current_song);
          mp = MediaPlayer.create(getApplicationContext(),uri);
          mp.start();
          seekbar.setMax(mp.getDuration());
       }
       
       
    }
    
    @Override
   protected void onDestroy() {
      super.onDestroy();
      mp.stop();
      mp.release();
      update_seekbar.interrupt();
      is_music_change = true;
      update_seekbar.interrupt();
    }

    
    @Override
    protected void onPause() {
       super.onPause();
       //Toast.makeText(getApplication(), "Pause", Toast.LENGTH_SHORT).show();
    }
   
   
    
            
}
