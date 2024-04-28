package com.example.paris;

import java.util.ArrayList;
import java.util.List;

public class InterestLines {
    // The name of the line
    private String lineName;
    // The lineColor of the line
    private String lineColor;
    // A list of points on the line
    private List<InterestPoints> points;

    public InterestLines(String lineName) {
        // Initialize the line with a name and lineColor
        this.lineName = lineName;

        // Initialize the list of points as an empty ArrayList
        this.points = new ArrayList<>();
    }

    public String getLineName() {
        // Get the name of the line
        return lineName;
    }

    public String getLineColor() {
        // Get the lineColor of the line
        return lineColor;
    }

    public List<InterestPoints> getPoints() {
        // Get the list of points on the line
        return points;
    }

    public void addPoint(InterestPoints point) {
        // Add a point to the line by adding it to the list of points
        this.points.add(point);
        point.addLine(this);// add this line to the point
    }

}