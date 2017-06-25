package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Gboyega.Dada on 6/17/2017.
 */

class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ItemViewHolder> {

    private Context mContext;
    private IngredientList mIngredients;

    static final String RECIPE_NAME_KEY = "name";
    private static final int VIEW_TYPE_INTRO = 0;
    private static final int VIEW_TYPE_STEP = 0;


    public IngredientsRecyclerViewAdapter(Context parent) {

        mContext = parent;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mIngredientView;
        public final TextView mMeasureView;
        public IngredientItem mItem;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            mIngredientView = (TextView) view.findViewById(R.id.tv_ingredient);
            mMeasureView = (TextView) view.findViewById(R.id.tv_measure);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIngredientView.getText() + "'";
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View recipeTileView = inflater.inflate(R.layout.ingredient_list_content, parent, shouldAttachToParentImmediately);

        return new ItemViewHolder(recipeTileView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mItem = mIngredients.get(position);

        String measure = " ("+holder.mItem.getQuantity()+" "+holder.mItem.getMeasure() + ")";

        String ingredient =
                Html.fromHtml(mContext.getString(R.string.ingredients_list_bullet)) +
                " " +
                holder.mItem.getIngredient() + measure;
        holder.mIngredientView.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        if (null == mIngredients) return 0;
        return mIngredients.size();
    }

    public void setData(IngredientList data) {
        mIngredients = data;

        notifyDataSetChanged();
    }

    /*
    public RecipeList getData() {
        return mIngredients;
    }
    */
}
