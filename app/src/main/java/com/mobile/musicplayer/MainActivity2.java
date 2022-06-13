package com.mobile.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity2 extends AppCompatActivity {

    ListView l;
    String tutorials[]
            = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        l = (ListView) findViewById(R.id.songList);
        ArrayAdapter<String> arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tutorials);
        l.setAdapter(arr);
    }
}