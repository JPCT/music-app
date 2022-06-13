package com.mobile.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.mobile.musicplayer.model.Root;

import java.io.File;
import java.net.URL;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLSession;

public class MainActivity extends AppCompatActivity {

    private MusicService musicService;
    private MediaPlayer mediaPlayer;
    private Root root;
    ListView l;
    String tutorials[]
            = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String root = getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString();

        String imageFolderPath = root;
        File imagesFolder = new File(imageFolderPath);

        musicService = new MusicService();
        //startActivity(new Intent(MainActivity.this, MainActivity2.class));


    }

    public void initViews(){

    }

    public void initEvents(){

    }

    public void launchSecondView(String term){
        setContentView(R.layout.activity_main2);
        l = (ListView) findViewById(R.id.songList);
        ArrayAdapter<String> arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tutorials);
        l.setAdapter(arr);
    }

    public void search(String term){
        musicService.searchSongsByTerm(term, 50, ((isNetworkError, statusCode, response) -> {
            if (!isNetworkError){

            }else if (statusCode != 200){

            }else{
                Gson gson = new Gson();
                root = gson.fromJson(response, Root.class);
            }
        }));
    }

    public void openLoadAudioFile(URL remoteURL, String destFilename){
        URLSession.getShared().downloadTask(remoteURL, (localURL, response, error) -> {
            HTTPURLResponse resp = (HTTPURLResponse) response;

            if (error == null){
                if (resp.getStatusCode() == 200){
                    File file = new File(localURL.getFile());

                    if (file.renameTo(new File(destFilename))){
                        mediaPlayer = MediaPlayer.create(this, Uri.parse(destFilename));
                        mediaPlayer.start();
                    }
                }
            }
        });
    }
}