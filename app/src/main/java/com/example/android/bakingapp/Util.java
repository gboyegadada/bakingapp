package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

/**
 * Created by Gboyega.Dada on 6/18/2017.
 */

public class Util {

    public static File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error
        }
        return file;
    }
}
