package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Gboyega.Dada on 6/18/2017.
 */

public class Util {
    private static String TAG = Util.class.getSimpleName();

    public static boolean putTempFileContent(Context context, String url, String content) {
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            FileOutputStream file = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            file.write(content.getBytes());
            file.close();

            return true; // success
        } catch (Exception e) {
            Log.d(TAG, "Unable to cache ["+url+"]. "+e.getMessage());
            return false; // failed
        }
    }

    public static String getTempFileContent(Context context, String url) {
        StringBuilder content = new StringBuilder();
        String line;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            FileInputStream file = context.openFileInput(fileName);
            InputStreamReader fileReader = new InputStreamReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            // Error
            Log.d(TAG, "Could not READ TEMP FILE for ["+url+"]. "+e.getMessage());
        }

        return content.toString();
    }
}
