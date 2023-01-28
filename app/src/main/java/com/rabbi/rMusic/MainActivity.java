package com.rabbi.rMusic;
 
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
//import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.File;
import java.util.ArrayList;
import android.view.MenuInflater;
import android.net.Uri;

public class MainActivity extends Activity { 
    private ListView list_view;
    private String[] songs;
    private SearchView searchView;    
    private ArrayList<File> myList;
    File file;
   ArrayAdapter<String> adapter;
   
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //songs = new String[] {"Tu itni khubsurot he..."};
        
        list_view =findViewById(R.id.list_view);
        searchView = findViewById(R.id.searchView);
        
        
        // Prompt For Access Storage
       Dexter.withContext(this)
          .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
          .withListener(new PermissionListener(){

             @Override
             public void onPermissionGranted(PermissionGrantedResponse p1) {
                
                
                myList = new ArrayList<File>();

                File dir = Environment.getStorageDirectory();
                
                file = new File( dir + "/0403-0201/Music" );
                if(file.listFiles()==null){
                   File dir2 = Environment.getExternalStorageDirectory();
                   file = new File( dir2 + "/Music");
                }
                
                
                File[] list = file.listFiles();
                
                //Toast.makeText(getApplication(), "Dir: "+file, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplication(), "List :"+list, Toast.LENGTH_SHORT).show();
                
                try{
                  for( int i=0; i< list.length; i++)
                  {
                     myList.add(list[i]);
                  }
                }
                catch(Exception e){
                   e.printStackTrace();
                   Toast.makeText(getApplication(), "Music not found in /emulated/0/Music or /your_memorycard/Music || Make sure you have Music directory in (phone storage/external memory)", Toast.LENGTH_LONG).show();
                }
                String[] items = new String[myList.size()];
                for (int i = 0; i < myList.size(); i++) {
                   items[i] = myList.get(i).getName().replace(".mp3","").substring(0,myList.get(i).getName().length()-10)+"...";
                }
                
                //Toast.makeText(getApplication(), "Items :"+items[0], Toast.LENGTH_SHORT).show();
                
                /*ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                String [] items = new String[mySongs.size()];
                for(int i=0;i<mySongs.size();i++){
                   items[i] = mySongs.get(i).getName().replace(".mp3", "");
                }*/

                //Toast.makeText(getApplication(), "Ready ...!", Toast.LENGTH_SHORT).show();
                adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.music_list,R.id.MusicTextView,items);
                list_view.setAdapter(adapter);
                
                
                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                      @Override
                      public void onItemClick(AdapterView<?> p1, View p2, int position, long p4) {
                         Intent intent = new Intent(getApplicationContext(),PlayMusic.class);               
                         intent.putExtra("current_song",list_view.getItemAtPosition(position).toString());
                         intent.putExtra("song_list",myList);
                         intent.putExtra("position",position);
                         startActivity(intent);
                         overridePendingTransition(0,0);
                      }

                   });
                   
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

                      @Override
                      public boolean onQueryTextSubmit(String p1) {
                         return false;
                      }

                      @Override
                      public boolean onQueryTextChange(String newText) {
                         adapter.getFilter().filter(newText);
                         return false;
                      }             

                   });
                
                   
                
             }

             @Override
             public void onPermissionDenied(PermissionDeniedResponse p1) {
             }

             @Override
             public void onPermissionRationaleShouldBeShown(PermissionRequest pRequest, PermissionToken pToken) {
                pToken.continuePermissionRequest();
             }
             
             
          })
          .check();
        
        
        
        
    }
    
    /*public ArrayList<File> fetchAllSongs(File file){
       
       ArrayList arrayList = new ArrayList();
       File [] songs = file.listFiles();
       if(songs != null){
          for(File myFile : songs){
             if(!myFile.isHidden() && myFile.isDirectory()){
                arrayList.addAll(fetchAllSongs(myFile));
             }
             else{
                if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".") ){
                   arrayList.add(myFile);
                }
             }
          }
       }
       return arrayList;
    }*/
    
    
   public ArrayList<File> fetchSongs(File file){
      ArrayList arrayList = new ArrayList();
      File [] songs = file.listFiles();
      if(songs !=null){
         for(File myFile: songs){
            if(!myFile.isHidden() && myFile.isDirectory()){
               arrayList.addAll(fetchSongs(myFile));
            }
            else{
               if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                  arrayList.add(myFile);
               }
            }
         }
      }
      return arrayList;
   }
	
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {

      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.top_menu,menu);
      return super.onCreateOptionsMenu(menu);
   }

  // Add Listener
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {

      if(item.getItemId() == R.id.follow_dev){
         Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://cutt.ly/rabbi"));
         startActivity(intent);
         //Toast.makeText(getApplicationContext(),"About Clicked",Toast.LENGTH_SHORT).show();
      }
      else if(item.getItemId() == R.id.about_app){
         startActivity(new Intent(getApplicationContext(),AboutApp.class));
         overridePendingTransition(0,0);
      }
      else if(item.getItemId() == R.id.share_app){
         Intent share_intent = new Intent(Intent.ACTION_SEND);
         share_intent.setType("text/plain");
         String subject = "rMusic";
         String body = "Hello there this is an amzing music player android app.Download now from https://frappstore.railway.app";
         share_intent.putExtra(share_intent.EXTRA_SUBJECT,subject);
         share_intent.putExtra(share_intent.EXTRA_TITLE,"Share This App With Your Friends");
         share_intent.putExtra(share_intent.EXTRA_TEXT,body);
         startActivity(share_intent.createChooser(share_intent,"Share With"));
         
      }
      return super.onOptionsItemSelected(item);
   }
   
} 

