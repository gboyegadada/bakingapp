package com.example.android.bakingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

@Parcel
public class StepItem {

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("shortDescription")
    @Expose
    String shortDescription;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("videoURL")
    @Expose
    String videoURL;

    @SerializedName("thumbnailURL")
    @Expose
    String thumbnailURL;


    public StepItem() {}

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
