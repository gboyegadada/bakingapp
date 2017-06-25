package com.example.android.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<RecipeList> {

    private static String TAG = MainActivity.class.getSimpleName();
    public final static String DATA_URI = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static final int GRID_SPAN_COUNT_PHONE = 2;
    private static final int GRID_SPAN_COUNT_TABLET = 3;
    private static final String LIST_INSTANCE_STATE = "list_instance_state";
    private static final String RECIPES_JSON_STRING = "recipes_json_string";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mMode;

    String mResponseString;
    RecipeList mRecipeList;
    RecyclerView mRecyclerView;
    RecipesRecyclerViewAdapter mRecipesAdapter;
    TextView mErrorMessageDisplay;
    GridLayoutManager mLayoutManager;
    public static LinearLayoutManager.SavedState mBundleRecyclerViewState;
    ProgressBar mLoadingIndicator;


    // The Idling Resource which will be null in production.
    @Nullable
    private RecipesIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != savedInstanceState) {
            mBundleRecyclerViewState = savedInstanceState.getParcelable(LIST_INSTANCE_STATE);
            mResponseString = savedInstanceState.getString(RECIPES_JSON_STRING);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recipes);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT_PHONE);

        mRecipesAdapter = new RecipesRecyclerViewAdapter(this, new RecipesRecyclerViewListener());

        mRecyclerView.setAdapter(mRecipesAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Get the IdlingResource instance
        getIdlingResource();

        loadRecipes();
    }

    public void loadRecipes() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mIdlingResource.setIdleState(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DATA_URI)
                .addConverterFactory(new CustomConverterFactory())
                .build();

        RecipeAPI recipeAPI = retrofit.create(RecipeAPI.class);

        Call<RecipeList> call = recipeAPI.getRecipes();
        call.enqueue(this);
    }


    /**
     * Only called from test, creates and returns a new {@link RecipesIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipesIdlingResource();
        }
        return mIdlingResource;
    }

    public interface OnItemClickListener {
        void onClick(View view, RecipeItem item);
    }

    private class RecipesRecyclerViewListener implements OnItemClickListener {
        @Override
        public void onClick(View view, RecipeItem item) {
            Intent intent = new Intent(MainActivity.this, StepListActivity.class);
            intent.putExtra(StepListActivity.ARG_RECIPE_ITEM, Parcels.wrap(item));
            startActivity(intent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }

    @Override
    public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mIdlingResource.setIdleState(true);

        if(response.isSuccessful()) {
            mRecipeList = response.body();
            mRecipesAdapter.setData(mRecipeList);
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        } else {
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            Log.d(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<RecipeList> call, Throwable t) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mIdlingResource.setIdleState(true);

        Log.e(TAG, t.getMessage());
        t.printStackTrace();
    }
}
