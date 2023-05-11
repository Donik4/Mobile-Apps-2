package com.example.racegame;

public class Records {
    public int Score;
    public double loti;
    public double longi;

    public Records(int score, double loti, double longi) {
        Score = score;
        this.loti = loti;
        this.longi = longi;
    }

    public Records() {
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public double getLoti() {
        return loti;
    }

    public void setLoti(double loti) {
        this.loti = loti;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }
}
