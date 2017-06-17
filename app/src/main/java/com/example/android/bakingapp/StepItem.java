package com.example.android.bakingapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

@Parcel
public class StepItem {
    int id;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;


    public StepItem() {}

    public StepItem(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.shortDescription = json.getString("shortDescription");
            this.description = json.getString("description");
            this.videoURL = json.getString("videoURL");
            this.thumbnailURL = json.getString("thumbnailURL");
        } catch (JSONException e) {

        }

    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
