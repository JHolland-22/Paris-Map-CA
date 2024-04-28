package com.example.paris;

import com.example.paris.AdjacencyMatrix;
import javafx.scene.shape.Circle;

public class GraphNode<T> {
    public T data;
    public double x;
    public double y;
    public Circle visual;
    public AdjacencyMatrix mat;
    public int nodeId;

    public GraphNode(T data, double x, double y, AdjacencyMatrix mat) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.mat = mat;
        this.visual = new Circle(x, y, 5); // 5 is the radius of the circle
        if (mat.nodeCount < mat.nodes.length) {
            this.nodeId = mat.nodeCount++;
            mat.nodes[nodeId] = this;
        } else {
            throw new IllegalStateException("Too many nodes added to the graph");
        }
    }

    public void connectToNodeDirected(GraphNode<T> destNode) {
        mat.amat[nodeId][destNode.nodeId] = true;
    }

    public void connectToNodeUndirected(GraphNode<T> destNode) {
        mat.amat[nodeId][destNode.nodeId] = mat.amat[destNode.nodeId][nodeId] = true;
    }
}
