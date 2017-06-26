package com.example.android.bakingapp;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Gboyega.Dada on 6/26/2017.
 */

public class RecipeListParcelConverter implements ParcelConverter<ArrayList<RecipeItem>> {
    @Override
    public void toParcel(ArrayList<RecipeItem> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (RecipeItem item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public ArrayList<RecipeItem> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        ArrayList<RecipeItem> items = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            items.add((RecipeItem) Parcels.unwrap(parcel.readParcelable(RecipeItem.class.getClassLoader())));
        }
        return items;
    }
}
