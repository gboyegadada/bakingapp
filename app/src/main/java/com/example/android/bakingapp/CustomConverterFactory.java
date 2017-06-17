package com.example.android.bakingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Gboyega.Dada on 6/16/2017.
 */

public class CustomConverterFactory extends Converter.Factory {
    private static final String TAG = CustomConverterFactory.class.getSimpleName();

    private static final String TYPE_RECIPE_LIST = "class com.example.android.bakingapp.RecipeList";
    private static final String TYPE_JSON = "class org.json.JSONArray";

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Log.d(TAG, "Converter Type ------ " + type.toString());

        switch(type.toString()) {
            case TYPE_RECIPE_LIST:
                return RecipeListConverter.INSTANCE;
            case TYPE_JSON:
                return JsonConverter.INSTANCE;
            default:
                return JsonConverter.INSTANCE;
        }
    }

    final static class RecipeListConverter implements Converter<ResponseBody, RecipeList> {
        static final RecipeListConverter INSTANCE = new RecipeListConverter();

        @Override
        public RecipeList convert(ResponseBody responseBody) throws IOException {
            try {
                return new RecipeList(responseBody.string());
            } catch (IOException e) {
                throw new IOException("Failed to parse JSON", e);
            }
        }
    }

    final static class JsonConverter implements Converter<ResponseBody, JSONArray> {
        static final JsonConverter INSTANCE = new JsonConverter();

        @Override
        public JSONArray convert(ResponseBody responseBody) throws IOException {
            try {
                return new JSONArray(responseBody.string());
            } catch (JSONException e) {
                throw new IOException("Failed to parse JSON", e);
            }
        }
    }
}
