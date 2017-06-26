package com.example.android.bakingapp;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Gboyega.Dada on 6/26/2017.
 */

public class IngredientListParcelConverter implements ParcelConverter<ArrayList<IngredientItem>> {
    @Override
    public void toParcel(ArrayList<IngredientItem> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (IngredientItem item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public ArrayList<IngredientItem> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        ArrayList<IngredientItem> items = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            items.add((IngredientItem) Parcels.unwrap(parcel.readParcelable(RecipeItem.class.getClassLoader())));
        }
        return items;
    }
}
