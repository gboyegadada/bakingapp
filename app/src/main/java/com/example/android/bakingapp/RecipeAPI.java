package com.example.android.bakingapp;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

public interface RecipeAPI {
    @GET("baking.json")
    Call<RecipeList> getRecipes();
}
