
package com.example.paris;

import java.util.List;

public class Path {
    private List<InterestPoints> path; // Represents the sequence of Point objects that make up the route
    private int stops; // The total number of stops along the route
    private double distance; // The total distance covered by the route
    // Constructor to initialize a Path object with a given path and number of stops
    public Path(List<InterestPoints> path) {
        this.path = path;
        this.stops = stops;
        this.distance = 0; // Default value for distance
    }

    // Constructor to initialize a Path object with a given path and distance
    public Path(List<InterestPoints> path, double distance) {
        this.path = path;
        this.distance = distance;
        this.stops = 0; // Default value for stops
    }



    // Getter method to retrieve the path of the route
    public List<InterestPoints> getPath() {
        return path;
    }

    // Getter method to retrieve the number of stops in the route
    public int getStops() {
        return stops;
    }

    // Setter method to set the number of stops in the route
    public void setStops(int stops) {
        this.stops = stops;
    }

    // Getter method to retrieve the total distance of the route
    public double getDistance() {
        return distance;
    }
}
