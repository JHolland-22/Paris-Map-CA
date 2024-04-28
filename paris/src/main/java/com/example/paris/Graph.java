package com.example.paris;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.*;

public class Graph implements Initializable {

    // A static graph object to represent the graph
    public static Graph graph;

    // A adjacency list to represent vertices on the graph
    private static Map<InterestPoints, List<InterestPoints>> adjacencyList = new HashMap<>();

    // Initialize the Graph object
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph = this;  // Set the static graph object to this instance
        // Optionally, you could initialize your graph here if you have static data
    }

    // Method to get the set of neighbors of a given point
    public Set<InterestPoints> getNeighbors(InterestPoints point) {
        // Ensure there is always a list for every point, even if it's empty
        return new HashSet<>(adjacencyList.getOrDefault(point, Collections.emptyList()));
    }

    // Method to add an edge between two points
    public static void addEdge(InterestPoints one, InterestPoints two) {
        adjacencyList.putIfAbsent(one, new ArrayList<>());  // Ensure there is a list for 'one'
        adjacencyList.get(one).add(two);

        adjacencyList.putIfAbsent(two, new ArrayList<>());  // Ensure there is a list for 'two'
        adjacencyList.get(two).add(one);  // For undirected graph, add both directions
    }

    // Method to find the shortest path between two points using BFS
    public Path bfsAlgorithm(InterestPoints start, InterestPoints end) {
        if (start == null || end == null) {
            System.out.println("Start or end point is null, cannot perform BFS");
            return null;
        }

        Map<InterestPoints, InterestPoints> previous = new HashMap<>();
        Queue<InterestPoints> queue = new LinkedList<>();
        Set<InterestPoints> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        previous.put(start, null);
        System.out.println("Starting BFS from: " + start.getPointName() + " to " + end.getPointName());

        while (!queue.isEmpty()) {
            InterestPoints current = queue.poll();
            System.out.println("Visiting: " + current.getPointName());

            if (current.equals(end)) {
                return reconstructPath(previous, end);
            }

            for (InterestPoints neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    previous.put(neighbor, current);
                    System.out.println("Enqueue: " + neighbor.getPointName());
                }
            }
        }
        System.out.println("No path found between the given stations.");
        return null;
    }


    private Path reconstructPath(Map<InterestPoints, InterestPoints> previous, InterestPoints end) {
        LinkedList<InterestPoints> path = new LinkedList<>();
        for (InterestPoints at = end; at != null; at = previous.get(at)) {
            System.out.println("Tracing path back to: " + (at != null ? at.getPointName() : "null"));
            path.addFirst(at);
        }
        return new Path(path);
    }



    // Method to find the shortest path using Dijkstra's Algorithm (implementation here is for example purposes)
    public Path dijkstraAlgorithm(Set<InterestPoints> allStations, InterestPoints start, InterestPoints end) {
        Map<InterestPoints, Double> distances = new HashMap<>();
        Map<InterestPoints, InterestPoints> previous = new HashMap<>();
        PriorityQueue<InterestPoints> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

        for (InterestPoints station : allStations) {
            distances.put(station, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            InterestPoints current = queue.poll();
            if (current.equals(end)) {
                return reconstructPath(previous, end);
            }

            for (InterestPoints neighbor : getNeighbors(current)) {
                double alt = distances.get(current) + 1; // Assume distance between neighbors is 1
                if (alt < distances.get(neighbor)) {
                    distances.put(neighbor, alt);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return null;
    }
}
