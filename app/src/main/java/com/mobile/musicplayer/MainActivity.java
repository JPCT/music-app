package com.mobile.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String root = getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString();

        String imageFolderPath = root;
        File imagesFolder = new File(imageFolderPath);

        musicService = new MusicService();
    }

    public void initViews(){

    }

    public void initEvents(){

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