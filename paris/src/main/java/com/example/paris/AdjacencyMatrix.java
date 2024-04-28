package com.example.paris;

public class AdjacencyMatrix {
    public boolean[][] amat; // adjacency matrix to store edges
    public GraphNode<?>[] nodes; // array to store nodes
    public int nodeCount; // counter to keep track of added nodes

    public AdjacencyMatrix(int size) {
        amat = new boolean[size][size];
        nodes = new GraphNode[size];
        nodeCount = 0;
    }

    /**
     * Adds a node to the matrix and returns its index.
     * @param node The node to add.
     * @return the index of the node.
     */
    public int addNode(GraphNode<?> node) {
        if (nodeCount >= nodes.length) {
            throw new IllegalStateException("Adjacency matrix is full");
        }
        nodes[nodeCount] = node;
        return nodeCount++;
    }

    /**
     * Connects two nodes directly.
     * @param from The index of the starting node.
     * @param to The index of the ending node.
     */
    public void addEdge(int from, int to) {
        if (from >= nodeCount || to >= nodeCount) {
            throw new IllegalArgumentException("Node index out of bounds");
        }
        amat[from][to] = true;
    }

    /**
     * Connects two nodes undirected, meaning both directions are connected.
     * @param from The index of the first node.
     * @param to The index of the second node.
     */
    public void addUndirectedEdge(int from, int to) {
        addEdge(from, to);
        addEdge(to, from);
    }

    /**
     * Checks if there is a direct connection between two nodes.
     * @param from The index of the starting node.
     * @param to The index of the ending node.
     * @return true if there is a direct connection, false otherwise.
     */
    public boolean isConnected(int from, int to) {
        if (from >= nodeCount || to >= nodeCount) {
            throw new IllegalArgumentException("Node index out of bounds");
        }
        return amat[from][to];
    }
}
