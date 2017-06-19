package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.parceler.Parcels;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = updateWidgetListView(context, appWidgetId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged (Context context, AppWidgetManager appWidgetManager,
                                           int appWidgetId, Bundle newOPtions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOPtions);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.baking_app_widget);
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        serviceIntent.setData(Uri.parse(
                serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent intent = new Intent(context, MainActivity.class);

        // Creating a pending intent and wrapping our intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.bt_see_more, pendingIntent);

        remoteViews.setRemoteAdapter(R.id.lv_ingredients, serviceIntent);
        remoteViews.setEmptyView(R.id.lv_ingredients, R.id.tv_empty_view);

        return remoteViews;
    }


    public interface OnItemClickListener {
        void onClick(Context context, IngredientItem item);
    }

    private class RecipesRecyclerViewListener implements OnItemClickListener {
        @Override
        public void onClick(Context context, IngredientItem item) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(StepListActivity.ARG_RECIPE_ITEM, Parcels.wrap(item));
            context.startActivity(intent);
        }
    }

}

