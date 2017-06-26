package com.example.android.bakingapp;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Gboyega.Dada on 6/26/2017.
 */

public class StepListParcelConverter implements ParcelConverter<ArrayList<StepItem>> {
    @Override
    public void toParcel(ArrayList<StepItem> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (StepItem item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public ArrayList<StepItem> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        ArrayList<StepItem> items = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            items.add((StepItem) Parcels.unwrap(parcel.readParcelable(StepItem.class.getClassLoader())));
        }
        return items;
    }
}
