package com.tgc.appledora.habitica.moodChart.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "moods")
public class Mood {
    @PrimaryKey(autoGenerate = true)
    private int id;
    // Year of entry
    private int year;
    // Year of entry
    private int month;
    // Position in the year matrix of 13 cols x 32 rows
    private int position;
    @ColumnInfo(name = "first_color")
    private int firstColor;
    @ColumnInfo(name = "first_color_value")
    private double firstColorValue;
    @ColumnInfo(name = "second_color")
    private int secondColor;
    private String notes;

    @Ignore
    public Mood(int firstColor) {
        this.firstColor = firstColor;
    }

    public Mood(int id, int year, int month, int position, int firstColor, double firstColorValue, int secondColor, String notes) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.position = position;
        this.firstColor = firstColor;
        this.firstColorValue = firstColorValue;
        this.secondColor = secondColor;
        this.notes = notes;
    }

    @Ignore
    public Mood(int year, int month, int position, int firstColor, double firstColorValue, int secondColor, String notes) {
        this.year = year;
        this.month = month;
        this.position = position;
        this.firstColor = firstColor;
        this.firstColorValue = firstColorValue;
        this.secondColor = secondColor;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getPosition() {
        return position;
    }

    public int getFirstColor() {
        return firstColor;
    }

    public double getFirstColorValue() {
        return firstColorValue;
    }

    public int getSecondColor() {
        return secondColor;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
