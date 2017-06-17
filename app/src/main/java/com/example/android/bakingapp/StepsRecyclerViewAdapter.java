package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Gboyega.Dada on 6/17/2017.
 */

class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ItemViewHolder> {

    private Context mContext;
    private StepList mSteps;
    private final StepListActivity.OnItemClickListener mClickListener;

    static final String RECIPE_NAME_KEY = "name";


    public StepsRecyclerViewAdapter(Context parent, StepListActivity.OnItemClickListener listener) {

        mContext = parent;
        mClickListener = listener;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public StepItem mItem;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClick(view, mSteps.get(getAdapterPosition()));
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View recipeTileView = inflater.inflate(R.layout.step_list_content, parent, shouldAttachToParentImmediately);

        return new ItemViewHolder(recipeTileView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mItem = mSteps.get(position);
        holder.mIdView.setText(""+holder.mItem.getId());
        holder.mContentView.setText(holder.mItem.getDescription());
    }

    @Override
    public int getItemCount() {
        if (null == mSteps) return 0;
        return mSteps.size();
    }

    public void setData(StepList data) {
        mSteps = data;

        notifyDataSetChanged();
    }

    /*
    public RecipeList getData() {
        return mSteps;
    }
    */
}
