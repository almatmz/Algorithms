package graph;

import java.util.*;

/**
 * Directed graph representation using adjacency list.
 * Supports weighted edges for DAG shortest path algorithms.
 */
public class Graph {
    private final int vertices;
    private final List<List<Edge>> adj;
    
    /**
     * Edge representation with weight
     */
    public static class Edge {
        public final int to;
        public final double weight;
        
        public Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }
        
        public Edge(int to) {
            this(to, 1.0);
        }
        
        @Override
        public String toString() {
            return "Edge{to=" + to + ", weight=" + weight + '}';
        }
    }
    
    /**
     * Create a graph with the given number of vertices
     */
    public Graph(int vertices) {
        this.vertices = vertices;
        this.adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
    }
    
    /**
     * Add a directed edge from u to v
     */
    public void addEdge(int from, int to) {
        addEdge(from, to, 1.0);
    }
    
    /**
     * Add a directed edge from u to v with weight
     */
    public void addEdge(int from, int to, double weight) {
        if (from < 0 || from >= vertices || to < 0 || to >= vertices) {
            throw new IllegalArgumentException("Vertex out of bounds");
        }
        adj.get(from).add(new Edge(to, weight));
    }
    
    /**
     * Get the number of vertices
     */
    public int getVertices() {
        return vertices;
    }
    
    /**
     * Get the adjacency list for a vertex
     */
    public List<Edge> getAdjacent(int vertex) {
        if (vertex < 0 || vertex >= vertices) {
            throw new IllegalArgumentException("Vertex out of bounds");
        }
        return adj.get(vertex);
    }
    
    /**
     * Get all edges in the graph
     */
    public List<Edge> getAllEdges(int from) {
        return adj.get(from);
    }
    
    /**
     * Create a reversed graph (transpose)
     */
    public Graph reverse() {
        Graph reversed = new Graph(vertices);
        for (int v = 0; v < vertices; v++) {
            for (Edge e : adj.get(v)) {
                reversed.addEdge(e.to, v, e.weight);
            }
        }
        return reversed;
    }
    
    /**
     * Get total number of edges
     */
    public int getEdgeCount() {
        int count = 0;
        for (int v = 0; v < vertices; v++) {
            count += adj.get(v).size();
        }
        return count;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph{vertices=").append(vertices).append(", edges=").append(getEdgeCount()).append("}\n");
        for (int v = 0; v < vertices; v++) {
            sb.append(v).append(": ");
            for (Edge e : adj.get(v)) {
                sb.append(e.to);
                if (e.weight != 1.0) {
                    sb.append("(").append(e.weight).append(")");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
