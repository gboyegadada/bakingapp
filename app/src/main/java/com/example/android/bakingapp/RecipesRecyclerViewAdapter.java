package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Gboyega.Dada on 6/15/2017.
 */

class RecipesRecyclerViewAdapter  extends RecyclerView.Adapter<RecipesRecyclerViewAdapter.ItemViewHolder> {

    private Context mContext;
    private RecipeList mRecipes;
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
        View recipeTileView = inflater.inflate(R.layout.recipe_card, parent, shouldAttachToParentImmediately);

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

    public void setData(RecipeList data) {
        mRecipes = data;

        notifyDataSetChanged();
    }

    /*
    public RecipeList getData() {
        return mRecipes;
    }
    */
}
