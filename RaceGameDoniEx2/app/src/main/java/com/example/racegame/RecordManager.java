package com.example.racegame;

import static com.example.racegame.MainActivity.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class RecordManager {

    private static final int NUM_RECORDS = 10;


    public RecordManager(Context context) {
     }

    public void saveRecord(int index, int score, double lati, double longi) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("score" + index, score);
        editor.putFloat("lati" + index, (float) lati);
        editor.putFloat("longi" + index, (float) longi);
        editor.apply();
    }

    public int getScore(int index) {
        return sharedPreferences.getInt("score" + index, 0);
    }

    public float getlati(int index) {
        return sharedPreferences.getFloat("lati" + index, 0f);
    }

    public float getlongi(int index) {
        return sharedPreferences.getFloat("longi" + index, 0f);
    }

    public boolean hasRecord(int index) {
        return sharedPreferences.contains("score" + index);
    }

    public void clearAllRecords() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < NUM_RECORDS; i++) {
            editor.remove("score" + i);
            editor.remove("lati" + i);
            editor.remove("longi" + i);
        }
        editor.apply();
    }
}
