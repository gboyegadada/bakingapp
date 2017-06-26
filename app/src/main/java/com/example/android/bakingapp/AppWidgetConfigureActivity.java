package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppWidgetConfigureActivity extends AppCompatActivity implements Callback<ArrayList<RecipeItem>> {
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private static String TAG = AppWidgetConfigureActivity.class.getSimpleName();
    public final static String DATA_URI = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static final String LIST_INSTANCE_STATE = "list_instance_state";
    private static final String RECIPES_JSON_STRING = "recipes_json_string";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mMode;

    ArrayList<RecipeItem> mRecipeList;
    RecyclerView mRecyclerView;
    RecipesRecyclerViewAdapter mRecipesAdapter;
    TextView mErrorMessageDisplay;
    LinearLayoutManager mLayoutManager;
    public static LinearLayoutManager.SavedState mBundleRecyclerViewState;
    ProgressBar mLoadingIndicator;

    public static final String PREFS_RECIPE_ID_KEY = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cause host to cancel out of widget placement if they press
        // the back button.
        setResult(RESULT_CANCELED);

        // Set the view resource to use.
        setContentView(R.layout.activity_app_widget_configure);

        // Find the widget id from the intent.
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recipes);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mLayoutManager = new LinearLayoutManager(this);

        mRecipesAdapter = new RecipesRecyclerViewAdapter(this, new RecipesWidgetViewListener());

        mRecyclerView.setAdapter(mRecipesAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadRecipes();

    }

    static void saveSelectedRecipePref(Context context, int appWidgetId, RecipeItem recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                context.getString(R.string.widget_shared_preference_key),
                Context.MODE_PRIVATE).edit();
        prefs.putInt(PREFS_RECIPE_ID_KEY + appWidgetId, recipe.getId());
        prefs.commit();

        Util.putTempFileContent(context, DATA_URI, (new Gson()).toJson(recipe));

        Log.d(TAG, "Recipe ID saved: "+recipe.getId());
    }

    public void loadRecipes() {
        mLoadingIndicator.setVisibility(View.VISIBLE);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DATA_URI)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeAPI recipeAPI = retrofit.create(RecipeAPI.class);

        Call<ArrayList<RecipeItem>> call = recipeAPI.getRecipes();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ArrayList<RecipeItem>> call, Response<ArrayList<RecipeItem>> response) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if(response.isSuccessful()) {
            mRecipeList = response.body();
            mRecipesAdapter.setData(mRecipeList);
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        } else {
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            Log.e(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<ArrayList<RecipeItem>> call, Throwable t) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        Log.e(TAG, t.getMessage());
        t.printStackTrace();
    }

    private class RecipesWidgetViewListener implements MainActivity.OnItemClickListener {
        @Override
        public void onClick(View view, RecipeItem item) {
            Context context = AppWidgetConfigureActivity.this;
            saveSelectedRecipePref(context, mAppWidgetId, item);

            // Push widget update to the surface with new prefs
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            BakingAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back original widget id
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

}
