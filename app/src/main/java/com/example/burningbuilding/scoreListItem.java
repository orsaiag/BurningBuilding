package com.example.burningbuilding;

import java.util.Date;

public class scoreListItem {


    private String name;
    private int score;
    private String date;


    public scoreListItem(String name, int score, String date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public scoreListItem(String item) {
        String[] fields= item.split(",");
        this.name = fields[0];
        this.score = Integer.parseInt(fields[1]);
        this.date = fields[2];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return name + "," + score + "," + date.toString();
    }
}
