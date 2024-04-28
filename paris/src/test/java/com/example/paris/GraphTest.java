package com.example.paris;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    public void testAddEdge() {
        Graph graph = new Graph();
        InterestPoints point1 = new InterestPoints("Point1",22,11);
        InterestPoints point2 = new InterestPoints("Point2",22,12);
        Graph.addEdge(point1, point2);

        assertTrue(graph.getNeighbors(point1).contains(point2), "Point2 should be a neighbor of Point1");
        assertTrue(graph.getNeighbors(point2).contains(point1), "Point1 should be a neighbor of Point2");
    }

    @Test
    public void testGetNeighbors() {
        Graph graph = new Graph();
        InterestPoints point1 = new InterestPoints("Point1",22,11);
        InterestPoints point2 = new InterestPoints("Point2",22,12);
        Graph.addEdge(point1, point2);

        assertTrue(graph.getNeighbors(point1).contains(point2), "Neighbors should include Point2");
    }

    @Test
    public void testBFSAlgorithm() {
        Graph graph = new Graph();
        InterestPoints point1 = new InterestPoints("Point1",22,11);
        InterestPoints point2 = new InterestPoints("Point2",22,12);
        InterestPoints point3 = new InterestPoints("Point3",22,12);
        Graph.addEdge(point1, point2);
        Graph.addEdge(point2, point3);

        Path resultPath = graph.bfsAlgorithm(point1, point3);

        assertNotNull(resultPath, "Resulting path should not be null");
        assertTrue(resultPath.getPath().contains(point1), "Path should contain Point1");
        assertTrue(resultPath.getPath().contains(point3), "Path should contain Point3");
        assertEquals(point1, resultPath.getPath().get(0), "Path should start at Point1");
        assertEquals(point3, resultPath.getPath().get(resultPath.getPath().size() - 1), "Path should end at Point3");
    }

}