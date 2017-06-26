package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private StepsRecyclerViewAdapter mStepsAdapter;
    private IngredientsRecyclerViewAdapter mIngredientsAdapter;
    private RecyclerView mStepsRecyclerView;
    private RecyclerView mIngredientsRecyclerView;
    public static LinearLayoutManager.SavedState mBundleStepsRecyclerViewState;
    public static LinearLayoutManager.SavedState mBundleIngredientsRecyclerViewState;
    private RecipeItem mRecipe;

    private static String TAG = StepListActivity.class.getSimpleName();
    private static final String STEP_LIST_INSTANCE_STATE = "step_list_instance_state";
    private static final String INGREDIENT_LIST_INSTANCE_STATE = "ingredient_list_instance_state";
    public static final String ARG_RECIPE_ITEM = "recipe_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        Bundle bundle = (savedInstanceState == null) ? getIntent().getExtras() : savedInstanceState;

        if (null != bundle) {
            handleSavedInstanceState(bundle);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mStepsRecyclerView = (RecyclerView) findViewById(R.id.step_list);
        assert mStepsRecyclerView != null;

        mIngredientsRecyclerView = (RecyclerView) findViewById(R.id.ingredient_list);
        assert mIngredientsRecyclerView != null;

        // isTablet will be set to true only in the
        // large-screen values xml (res/values-sw900dp/bools.xml)
        // and set to false in res/values/bools.xml
        // If set to true, then the
        // activity should be in two-pane mode.
        mTwoPane = getResources().getBoolean(R.bool.isTablet);

        setupStepsRecyclerView();
        setupIngredientsRecyclerView();

        // If in two pane mode, navigate to recipe intro
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepDetailFragment.ARG_STEP_ITEM, Parcels.wrap(mRecipe.getSteps().get(0)));
            arguments.putParcelable(StepListActivity.ARG_RECIPE_ITEM, Parcels.wrap(mRecipe));
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    private void handleSavedInstanceState(Bundle savedInstanceState) {
        if (null != savedInstanceState && savedInstanceState.containsKey(ARG_RECIPE_ITEM)) {
            mBundleStepsRecyclerViewState = savedInstanceState.getParcelable(STEP_LIST_INSTANCE_STATE);
            mBundleIngredientsRecyclerViewState = savedInstanceState.getParcelable(INGREDIENT_LIST_INSTANCE_STATE);
            this.mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(ARG_RECIPE_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_LIST_INSTANCE_STATE, mStepsRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(INGREDIENT_LIST_INSTANCE_STATE, mIngredientsRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(ARG_RECIPE_ITEM, Parcels.wrap(mRecipe));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        handleSavedInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // restore steps data
        if (mRecipe != null) {
            mStepsAdapter.setData(mRecipe.getSteps());
        }

        // restore RecyclerView state
        if (mBundleStepsRecyclerViewState != null) {
            mStepsRecyclerView.getLayoutManager().onRestoreInstanceState(mBundleStepsRecyclerViewState);
        }
        if (mBundleIngredientsRecyclerViewState != null) {
            mIngredientsRecyclerView.getLayoutManager().onRestoreInstanceState(mBundleIngredientsRecyclerViewState);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public interface OnItemClickListener {
        void onClick(View view, StepItem item);
    }

    private class StepsRecyclerViewListener implements OnItemClickListener {
        @Override
        public void onClick(View v, StepItem item) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(StepDetailFragment.ARG_STEP_ITEM, Parcels.wrap(item));
                arguments.putParcelable(StepListActivity.ARG_RECIPE_ITEM, Parcels.wrap(mRecipe));
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Context context = v.getContext();
                Intent intent = new Intent(context, StepDetailActivity.class);
                intent.putExtra(StepDetailFragment.ARG_STEP_ITEM, Parcels.wrap(item));
                intent.putExtra(StepListActivity.ARG_RECIPE_ITEM, Parcels.wrap(mRecipe));

                context.startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        }

    }

    private void setupStepsRecyclerView() {
        assert mRecipe != null;
        mStepsAdapter = new StepsRecyclerViewAdapter(this, new StepsRecyclerViewListener());

        assert mStepsAdapter != null;
        ArrayList<StepItem> steps = mRecipe.getSteps();

        assert steps != null;
        mStepsAdapter.setData(steps);

        assert mStepsRecyclerView != null;
        mStepsRecyclerView.setAdapter(mStepsAdapter);
    }

    private void setupIngredientsRecyclerView() {
        assert mRecipe != null;
        mIngredientsAdapter = new IngredientsRecyclerViewAdapter(this);

        assert mIngredientsAdapter != null;
        ArrayList<IngredientItem> ingredients = mRecipe.getIngredients();

        assert ingredients != null;
        mIngredientsAdapter.setData(ingredients);

        assert mIngredientsRecyclerView != null;
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
    }

}
