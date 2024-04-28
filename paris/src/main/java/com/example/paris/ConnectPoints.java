package com.example.paris;

public class ConnectPoints {
    public GraphNode<?> destNode; // represents the node that the edge is directed towards
    public int cost;

    public ConnectPoints(GraphNode<?> destNode) {
        this.destNode =destNode;
    }
}

