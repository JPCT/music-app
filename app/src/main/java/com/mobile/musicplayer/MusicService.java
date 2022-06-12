package com.mobile.musicplayer;

import android.util.Log;

import java.net.URL;
import java.util.Objects;
import java.util.Queue;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLQueryItem;
import cafsoft.foundation.URLSession;

public class MusicService {

    private URLComponents components; 

    public MusicService() {
        components = new URLComponents();
        components.setScheme("https");
        components.setHost("itunes.apple.com");
        components.setPath("/search");
    }

    public void searchSongsByTerm(String searchTerm, int limit, OnDataResponse delegate){
        components.setQueryItems(new URLQueryItem[]{
                new URLQueryItem("media", "music"),
                new URLQueryItem("entity", "song"),
                new URLQueryItem("limit", String.valueOf(limit)),
                new URLQueryItem("term", searchTerm)
        });

        URL url = components.getURL();
        Log.d("MusicService", url.toString());

        URLSession.getShared().dataTask(url, ((data, response, error) -> {
            HTTPURLResponse resp = (HTTPURLResponse) response;
            int statusCode = -1;
            String text = null;

            if (Objects.isNull(error) && resp.getStatusCode() == 200){
                text = data.toText();
                statusCode = resp.getStatusCode();
            }

            if (Objects.nonNull(delegate)){
                delegate.onChange(Objects.nonNull(error), statusCode, text);
            }
        })).resume();
    }

    public interface OnDataResponse {
        public abstract void onChange(boolean isNetworkError, int statusCode, String response);
    }
}
