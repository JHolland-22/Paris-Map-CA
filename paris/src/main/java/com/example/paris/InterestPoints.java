
package com.example.paris;

import com.example.paris.InterestLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InterestPoints {
    private String pointName; // The name of the point
    private double xcoord; // The xcoord-coordinate of the point
    private double ycoord; // The ycoord-coordinate of the point
    private Map<InterestPoints, Double> neighborPoints; // A map of neighboring points and their distances
    private List<Double> distances; // A list of distances to the neighborPoints
    private List<InterestLines> lines; // A list of lines that the point is on
    private double distanceFromFirstPoint = Double.MAX_VALUE; // Distance from the start point for Dijkstra's algorithm
    private InterestPoints previousPoint; // Previous point in the shortest path from the start point

    // CONSTRUCTOR
    public InterestPoints(String pointName, double x, double y) {
        this.pointName = pointName; // Initialize the point with a name
        this.xcoord = x; // Initialize the xcoord-coordinate of the point
        this.ycoord = y; // Initialize the ycoord-coordinate of the point
        this.neighborPoints = new HashMap<>(); // Initialize the map of neighborPoints as an empty HashMap
        this.distances = new ArrayList<>(); // Initialize the list of distances as an empty ArrayList
        this.lines = new ArrayList<>(); // Initialize the list of lines as an empty ArrayList
    }

    // GETTERS
    public String getPointName() {
        return pointName; // Get the name of the point
    }

    public double getXcoord() {
        return xcoord; // Get the xcoord-coordinate of the point
    }

    public double getYcoord() {
        return ycoord; // Get the ycoord-coordinate of the point
    }

    public Map<InterestPoints, Double> getNeighborPoints() {
        return neighborPoints; // Get the map of neighboring points and their distances
    }

    public List<InterestLines> getLines() {
        return lines; // Get the list of lines that the point is on
    }

    public double getDistanceFromFirstPoint() {
        return distanceFromFirstPoint; // Get the distance from the start point
    }

    public InterestPoints getPreviousPoint() {
        return previousPoint; // Get the previous point in the shortest path from the start point
    }

    // SETTERS
    public void setPointName(String pointName) {
        this.pointName = pointName; // Set the name of the point
    }

    public void setXcoord(double xcoord) {
        this.xcoord = xcoord; // Set the xcoord-coordinate of the point
    }

    public void setYcoord(double ycoord) {
        this.ycoord = ycoord; // Set the ycoord-coordinate of the point
    }

    public void setNeighborPoints(Map<InterestPoints, Double> neighborPoints) {
        this.neighborPoints = neighborPoints; // Set the map of neighboring points and their distances
    }

    public void setDistanceFromFirstPoint(double distanceFromFirstPoint) {
        this.distanceFromFirstPoint = distanceFromFirstPoint; // Set the distance from the start point
    }

    public void setPreviousPoint(InterestPoints previousPoint) {
        this.previousPoint = previousPoint; // Set the previous point in the shortest path from the start point
    }
    public void addNeighbor(InterestPoints neighbor) {
        double distance = this.calculateDistanceTo(neighbor); // Calculate the distance between this point and the neighbor
        this.neighborPoints.put(neighbor, distance); // Add the neighboring point and its distance to the map of neighborPoints
        distances.add(distance); // Add the distance to the list of distances
    }
    public double calculateDistanceTo(InterestPoints other) {
        return Math.sqrt(Math.pow((this.xcoord - other.getXcoord()), 2) + Math.pow((this.ycoord - other.getYcoord()), 2));
    }

    public void addLine(InterestLines line) {
        this.lines.add(line); // Add a line to the list of lines
    }

    @Override
    public String toString() {
        String neighborNames = neighborPoints.keySet().stream()
                .map(InterestPoints::getPointName)
                .collect(Collectors.joining(", "));

        return "Station: " + "Name: " + getPointName() + ", xCoordinate: " + xcoord + ", yCoordinate: " + ycoord + ", neighboring points: " + neighborNames;
    }
}
