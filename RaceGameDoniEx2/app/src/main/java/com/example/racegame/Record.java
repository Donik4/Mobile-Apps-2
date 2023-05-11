package com.example.racegame;

public class Record {
    private int score;
    private float longi;
    private float lati;

    public Record(int score, float lati, float longi) {
        this.score = score;
        this.longi = longi;
        this.lati = lati;
    }
    public int getScore() {
        return score;
    }

    public float getlongi() {
        return longi;
    }

    public float getlati() {
        return lati;
    }
}
