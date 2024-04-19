package com.lytmoon.identify;

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import java.util.Base64;
import java.util.List;

class CharCenter {
    @SerializedName("x")
    public int x;

    @SerializedName("y")
    public int y;

    // Getters and setters
}

class CharPolygon {
    @SerializedName("x1")
    public int x1;

    @SerializedName("y1")
    public int y1;

    @SerializedName("x2")
    public int x2;

    @SerializedName("y2")
    public int y2;

    @SerializedName("x3")
    public int x3;

    @SerializedName("y3")
    public int y3;

    @SerializedName("x4")
    public int x4;

    @SerializedName("y4")
    public int y4;

    // Getters and setters
}

class Line {
    @SerializedName("angle")
    public int angle;

    @SerializedName("char_centers")
    public List<List<Integer>> charCenters;

    @SerializedName("char_polygons")
    public List<List<Integer>> charPolygons;

    @SerializedName("char_score")
    public List<Double> charScore;

    @SerializedName("position")
    public List<Integer> position;

    @SerializedName("property")
    public int property;

    @SerializedName("score")
    public double score;

    @SerializedName("text")
    public String text;

    // Getters and setters
}

class DataClass {
    @SerializedName("image_angle")
    public int imageAngle;

    @SerializedName("lines")
    public List<Line> lines;

    @SerializedName("property_map")
    public List<String> propertyMap;

    @SerializedName("rotated_image_height")
    public int rotatedImageHeight;

    @SerializedName("rotated_image_width")
    public int rotatedImageWidth;

    @SerializedName("whole_text")
    public String wholeText;


}

