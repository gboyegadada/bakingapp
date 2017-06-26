package com.example.android.bakingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.ArrayList;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

@Parcel
public class RecipeItem {


    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("name")
    @Expose
    String name;

    @ParcelPropertyConverter(IngredientListParcelConverter.class)
    @SerializedName("ingredients")
    @Expose
    ArrayList<IngredientItem> ingredients;

    @ParcelPropertyConverter(StepListParcelConverter.class)
    @SerializedName("steps")
    @Expose
    ArrayList<StepItem> steps;

    @SerializedName("servings")
    @Expose
    int servings;

    @SerializedName("image")
    @Expose
    String image;

    String json;

    public RecipeItem() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<IngredientItem> getIngredients() {
        return ingredients;
    }

    public ArrayList<StepItem> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(ArrayList<IngredientItem> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<StepItem> steps) {
        this.steps = steps;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
