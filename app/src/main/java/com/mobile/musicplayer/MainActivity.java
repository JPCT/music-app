package com.mobile.musicplayer;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.mobile.musicplayer.model.Root;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLSession;

public class MainActivity extends AppCompatActivity {

    private MusicService musicService;
    private MediaPlayer mediaPlayer;
    private Root root;

    private Button btnSearch;
    private ListView list;
    private TextInputEditText textInputLayout;

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

        musicService = new MusicService();

        initViews();
        initEvents();
    }

    public void initViews(){
        textInputLayout = findViewById(R.id.InputNameMusic);
        btnSearch = findViewById(R.id.button);
    }

    public void initEvents(){
        btnSearch.setOnClickListener(view -> search());
    }

    private void search() {
        String term = textInputLayout.getText().toString();
        launchSecondView(term);
    }

    public void launchSecondView(String term){
        setContentView(R.layout.activity_main2);
        list = (ListView) findViewById(R.id.songList);

        search(term);
        while (root == null){

        }

        List<String> tracks = root.getResults().stream()
                .map(result -> result.getTrackName())
                .collect(Collectors.toList());
        ArrayAdapter<String> arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tracks);
        list.setAdapter(arr);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Reproduciendo " + tracks.get(position), Toast.LENGTH_LONG)
                        .show();


                openLoadAudioFile(tracks.get(position), root.getResults().get(position).getPreviewUrl());
            }
        });
    }

    public void search(String term){
        musicService.searchSongsByTerm(term, 50, ((isNetworkError, statusCode, response) -> {
            if (isNetworkError){

            }else if (statusCode != 200){

            }else{
                Gson gson = new Gson();
                root = gson.fromJson(response, Root.class);
            }
        }));
    }

    public void openLoadAudioFile(String trackName, String url){
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(trackName);
        request.setDescription("Downloading");//request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,trackName + ".m4a");
        downloadmanager.enqueue(request);
        String fileURl= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + trackName + ".m4a";

        mediaPlayer = MediaPlayer.create(this, Uri.parse(fileURl));
        mediaPlayer.start();

    }
}