package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Gboyega.Dada on 6/18/2017.
 */

public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private static String TAG = WidgetListProvider.class.getSimpleName();
    public final static String DATA_URI = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private Context mContext;
    private IngredientList mIngredients;
    private int mAppWidgetId;
    // private final BakingAppWidget.OnItemClickListener mClickListener;

    TextView mErrorMessageDisplay;
    LinearLayoutManager mLayoutManager;

    public WidgetListProvider(Context context, Intent intent) {
        this.mContext = context;
        this.mAppWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);


        // mErrorMessageDisplay = (TextView) context.findViewById(R.id.tv_error_message_display);
        mLayoutManager = new LinearLayoutManager(context);

        populateListItems();
    }

    private void populateListItems() {

        String recipe_json = Util.getTempFileContent(mContext, DATA_URI);
        RecipeItem recipe = new RecipeItem(recipe_json);
        mIngredients = recipe.getIngredients();

        Log.d(TAG, "Recipe ID: "+ recipe.getId());
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        populateListItems();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(),
                R.layout.ingredient_list_item);
        IngredientItem item = mIngredients.get(i);

        remoteView.setTextViewText(R.id.tv_id, ""+i);
        remoteView.setTextViewText(R.id.tv_measure, item.getQuantity()+" "+item.getMeasure());
        remoteView.setTextViewText(R.id.tv_ingredient, item.getIngredient());

        // remoteView.setOnClickPendingIntent(
        //
        // );

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
