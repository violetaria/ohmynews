package com.getlosthere.ohmynews.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by violetaria on 7/26/16.
 */
public class Article implements Serializable {
    String webUrl;
    String headline;
    String thumbnail;
    private final String BASE_URL = "http://www.nytimes.com/";

    public String getThumbnail() {
        return BASE_URL + thumbnail;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }
    public Article (JSONObject jsonObject){
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.thumbnail = "";
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            for(int i = 0; i < multimedia.length(); i++) {
                JSONObject image = multimedia.getJSONObject(i);

                if(image.getString("subtype").equals("thumbnail")) {
                    this.thumbnail = image.getString("url");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            try {
                results.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
