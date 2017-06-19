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
    private RecyclerView mRecyclerView;
    public static LinearLayoutManager.SavedState mBundleRecyclerViewState;
    private RecipeItem mRecipe;

    private static String TAG = StepListActivity.class.getSimpleName();
    private static final String LIST_INSTANCE_STATE = "list_instance_state";
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.step_list);
        assert mRecyclerView != null;

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        setupRecyclerView();
    }

    private void handleSavedInstanceState(Bundle savedInstanceState) {
        if (null != savedInstanceState && savedInstanceState.containsKey(ARG_RECIPE_ITEM)) {
            this.mBundleRecyclerViewState = savedInstanceState.getParcelable(LIST_INSTANCE_STATE);
            this.mRecipe = Parcels.unwrap(savedInstanceState.getParcelable(ARG_RECIPE_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_INSTANCE_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());
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
        if (mBundleRecyclerViewState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mBundleRecyclerViewState);
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
            }
        }

    }

    private void setupRecyclerView() {
        assert mRecipe != null;
        mStepsAdapter = new StepsRecyclerViewAdapter(this, new StepsRecyclerViewListener());

        assert mStepsAdapter != null;
        StepList steps = mRecipe.getSteps();

        assert steps != null;
        mStepsAdapter.setData(steps);

        assert mRecyclerView != null;
        mRecyclerView.setAdapter(mStepsAdapter);
    }

}
