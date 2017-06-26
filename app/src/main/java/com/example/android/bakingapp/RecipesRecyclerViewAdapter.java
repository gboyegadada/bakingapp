package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

class RecipesRecyclerViewAdapter  extends RecyclerView.Adapter<RecipesRecyclerViewAdapter.ItemViewHolder> {

    private Context mContext;
    private ArrayList<RecipeItem> mRecipes;
    private int mItemResourceId = R.layout.recipe_card;
    private final MainActivity.OnItemClickListener mClickListener;

    static final String RECIPE_NAME_KEY = "name";


    public RecipesRecyclerViewAdapter(Context parent, MainActivity.OnItemClickListener listener) {

        mContext = parent;
        mClickListener = listener;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;

        public  ItemViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClick(view, mRecipes.get(getAdapterPosition()));
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View recipeTileView = inflater.inflate(mItemResourceId, parent, shouldAttachToParentImmediately);

        return new ItemViewHolder(recipeTileView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int pos) {
        RecipeItem recipe = mRecipes.get(pos);

        holder.name.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (null == mRecipes) return 0;
        return mRecipes.size();
    }

    // This will allow us to set a custom resource for the list items
    // when reusing our adapter in the AppWidget layout.
    public void setListItemResourceId(int resourceId) {
        mItemResourceId = resourceId;
    }

    public void setData(ArrayList<RecipeItem> data) {
        mRecipes = data;

        notifyDataSetChanged();
    }

    /*
    public ArrayList<RecipeItem> getData() {
        return mRecipes;
    }
    */
}
