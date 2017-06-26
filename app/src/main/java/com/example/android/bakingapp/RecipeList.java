package com.example.android.bakingapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

public class RecipeList extends ArrayList<RecipeItem> {
    private JSONArray mJson;


    public RecipeList(String jsonString) {
        try {
            mJson = new JSONArray(jsonString);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isEmpty() {
        return mJson.length() == 0;
    }

    @Override
    public int size() {
        return mJson.length();
    }

    @Override
    public RecipeItem get(int position) {
        try {
            return new RecipeItem(mJson.getJSONObject(position));
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public RecipeItem set(int index, RecipeItem item) {
        // do nothing (shhh!!)
        return item;
    }

    @Override
    public void clear() {
        this.mJson = null;
    }

}
