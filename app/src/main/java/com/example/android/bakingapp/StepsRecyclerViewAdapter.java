package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gboyega.Dada on 6/17/2017.
 */

class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ItemViewHolder> {

    private Context mContext;
    private ArrayList<StepItem> mSteps;
    private final StepListActivity.OnItemClickListener mClickListener;

    static final String RECIPE_NAME_KEY = "name";
    private static final int VIEW_TYPE_INTRO = 0;
    private static final int VIEW_TYPE_STEP = 1;


    public StepsRecyclerViewAdapter(Context parent, StepListActivity.OnItemClickListener listener) {

        mContext = parent;
        mClickListener = listener;

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        // public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mThumbnailView;
        public StepItem mItem;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            // mIdView = (TextView) view.findViewById(R.id.id);

            mContentView = (TextView) view.findViewById(R.id.content);
            mThumbnailView = (ImageView) view.findViewById(R.id.step_thumbnail);

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
        int view_layout = (viewType == VIEW_TYPE_INTRO)
                            ? R.layout.step_list_intro
                            : R.layout.step_list_content;
        View recipeTileView = inflater.inflate(view_layout, parent, shouldAttachToParentImmediately);

        return new ItemViewHolder(recipeTileView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mItem = mSteps.get(position);
        // holder.mIdView.setText(""+holder.mItem.getId());


        Spanned content = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? Html.fromHtml(holder.mItem.getDescription(), Html.FROM_HTML_MODE_COMPACT)
                : Html.fromHtml(holder.mItem.getDescription());

        holder.mContentView.setText(content);

        // Handle thumbnail
        String thumbnailUrl = holder.mItem.getThumbnailURL();
        if (!TextUtils.isEmpty(thumbnailUrl)) {
            Picasso.with(mContext)
                    .load(thumbnailUrl)
                    .error(R.mipmap.ic_error)
                    .placeholder(R.mipmap.ic_error)
                    .into(holder.mThumbnailView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0)
                ? VIEW_TYPE_INTRO // This is the intro
                : VIEW_TYPE_STEP; // This is a step
    }

    @Override
    public int getItemCount() {
        if (null == mSteps) return 0;
        return mSteps.size();
    }

    public void setData(ArrayList<StepItem> data) {
        mSteps = data;

        notifyDataSetChanged();
    }

    /*
    public RecipeList getData() {
        return mSteps;
    }
    */
}
