package com.example.android.bakingapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

@Parcel
public class RecipeItem {
    int id;
    String name;
    String ingredients;
    String steps;
    int servings;
    String image;

    public RecipeItem() {}

    public RecipeItem(JSONObject json) {
        try {
            this.name = json.getString("name");
            this.ingredients = json.getJSONArray("ingredients").toString();
            this.steps = json.getJSONArray("steps").toString();
        } catch (JSONException e) {

        }

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public IngredientList getIngredients() {
        return new IngredientList(ingredients);
    }

    public StepList getSteps() {
        return new StepList(steps);
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

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
