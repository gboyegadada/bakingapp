package com.example.android.bakingapp;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

public class StepList extends ArrayList<StepItem> {

    JSONArray mJson;


    public StepList(String jsonString) {
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
    public StepItem get(int position) {
        try {
            return new StepItem(mJson.getJSONObject(position));
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public StepItem set(int index, StepItem item) {
        // do nothing (shhh!!)
        return item;
    }

    @Override
    public void clear() {
        this.mJson = null;
    }
}
