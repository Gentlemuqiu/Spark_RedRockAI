package com.example.api.orc.dataBean

import com.google.gson.annotations.SerializedName

data class CharCenter(
    @SerializedName("x") val x: Int,
    @SerializedName("y") val y: Int
)

data class CharPolygon(
    @SerializedName("x1") val x1: Int,
    @SerializedName("y1") val y1: Int,
    @SerializedName("x2") val x2: Int,
    @SerializedName("y2") val y2: Int,
    @SerializedName("x3") val x3: Int,
    @SerializedName("y3") val y3: Int,
    @SerializedName("x4") val x4: Int,
    @SerializedName("y4") val y4: Int
)

data class Line(
    @SerializedName("angle") val angle: Int,
    @SerializedName("char_centers") val charCenters: List<List<Int>>,
    @SerializedName("char_polygons") val charPolygons: List<List<Int>>,
    @SerializedName("char_score") val charScore: List<Double>,
    @SerializedName("position") val position: List<Int>,
    @SerializedName("property") val property: Int,
    @SerializedName("score") val score: Double,
    @SerializedName("text") val text: String
)

data class DataClass(
    @SerializedName("image_angle") val imageAngle: Int,
    @SerializedName("lines") val lines: List<Line>,
    @SerializedName("property_map") val propertyMap: List<String>,
    @SerializedName("rotated_image_height") val rotatedImageHeight: Int,
    @SerializedName("rotated_image_width") val rotatedImageWidth: Int,
    @SerializedName("whole_text") val wholeText: String
)
