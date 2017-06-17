package com.example.android.bakingapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

@Parcel
public class IngredientItem {
    int quantity;
    String measure;
    String ingredient;


    public IngredientItem() {}

    public IngredientItem(JSONObject json) {
        try {
            this.quantity = json.getInt("id");
            this.measure = json.getString("measure");
            this.ingredient = json.getString("ingredient");
        } catch (JSONException e) {

        }

    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
