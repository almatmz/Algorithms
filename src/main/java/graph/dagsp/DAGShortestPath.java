package graph.dagsp;

import graph.Graph;
import graph.GraphMetrics;
import graph.topo.TopologicalSort;

import java.util.*;

/**
 * Shortest and longest path algorithms for DAGs.
 * Uses topological ordering for O(V + E) time complexity.
 */
public class DAGShortestPath {
    private final Graph graph;
    private final GraphMetrics metrics;
    
    /**
     * Create DAG shortest path solver for the given graph
     */
    public DAGShortestPath(Graph graph, GraphMetrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }
    
    /**
     * Compute single-source shortest paths in a DAG
     * @param source The source vertex
     * @return Array of shortest distances from source
     */
    public double[] shortestPaths(int source) {
        int n = graph.getVertices();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[source] = 0;
        
        // Get topological order
        TopologicalSort topoSort = new TopologicalSort(graph, new GraphMetrics());
        List<Integer> order = topoSort.dfsSort();
        
        if (order == null) {
            throw new IllegalArgumentException("Graph contains a cycle");
        }
        
        metrics.startTiming();
        
        // Relax edges in topological order
        for (int v : order) {
            if (dist[v] != Double.POSITIVE_INFINITY) {
                for (Graph.Edge edge : graph.getAdjacent(v)) {
                    int to = edge.to;
                    if (dist[v] + edge.weight < dist[to]) {
                        dist[to] = dist[v] + edge.weight;
                        metrics.incRelaxations();
                    }
                }
            }
        }
        
        metrics.endTiming();
        
        return dist;
    }
    
    /**
     * Compute longest paths in a DAG (critical path)
     * @param source The source vertex
     * @return Array of longest distances from source
     */
    public double[] longestPaths(int source) {
        int n = graph.getVertices();
        double[] dist = new double[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        dist[source] = 0;
        
        // Get topological order
        TopologicalSort topoSort = new TopologicalSort(graph, new GraphMetrics());
        List<Integer> order = topoSort.dfsSort();
        
        if (order == null) {
            throw new IllegalArgumentException("Graph contains a cycle");
        }
        
        metrics.startTiming();
        
        // Relax edges for longest path (using max instead of min)
        for (int v : order) {
            if (dist[v] != Double.NEGATIVE_INFINITY) {
                for (Graph.Edge edge : graph.getAdjacent(v)) {
                    int to = edge.to;
                    if (dist[v] + edge.weight > dist[to]) {
                        dist[to] = dist[v] + edge.weight;
                        metrics.incRelaxations();
                    }
                }
            }
        }
        
        metrics.endTiming();
        
        return dist;
    }
    
    /**
     * Reconstruct shortest path from source to target
     * @param source Source vertex
     * @param target Target vertex
     * @return List of vertices in the path, or null if no path exists
     */
    public List<Integer> reconstructShortestPath(int source, int target) {
        double[] dist = shortestPaths(source);
        
        if (dist[target] == Double.POSITIVE_INFINITY) {
            return null; // No path exists
        }
        
        List<Integer> path = new ArrayList<>();
        reconstructPath(target, source, dist, path, false);
        Collections.reverse(path);
        return path;
    }
    
    /**
     * Reconstruct longest path from source to target
     * @param source Source vertex
     * @param target Target vertex
     * @return List of vertices in the path, or null if no path exists
     */
    public List<Integer> reconstructLongestPath(int source, int target) {
        double[] dist = longestPaths(source);
        
        if (dist[target] == Double.NEGATIVE_INFINITY) {
            return null; // No path exists
        }
        
        List<Integer> path = new ArrayList<>();
        reconstructPath(target, source, dist, path, true);
        Collections.reverse(path);
        return path;
    }
    
    private void reconstructPath(int current, int source, double[] dist, List<Integer> path, boolean longest) {
        path.add(current);
        
        if (current == source) {
            return;
        }
        
        // Find predecessor
        for (int v = 0; v < graph.getVertices(); v++) {
            for (Graph.Edge edge : graph.getAdjacent(v)) {
                if (edge.to == current) {
                    boolean isOnPath;
                    if (longest) {
                        isOnPath = Math.abs(dist[current] - (dist[v] + edge.weight)) < 1e-9;
                    } else {
                        isOnPath = Math.abs(dist[current] - (dist[v] + edge.weight)) < 1e-9;
                    }
                    
                    if (isOnPath && dist[v] != (longest ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY)) {
                        reconstructPath(v, source, dist, path, longest);
                        return;
                    }
                }
            }
        }
    }
    
    /**
     * Find the critical path (longest path) in the entire DAG
     * @return PathResult containing the path and its length
     */
    public PathResult findCriticalPath() {
        int n = graph.getVertices();
        double maxLength = Double.NEGATIVE_INFINITY;
        int bestSource = -1;
        int bestTarget = -1;
        
        // Try all possible sources (vertices with in-degree 0 are best candidates)
        for (int source = 0; source < n; source++) {
            double[] dist = longestPaths(source);
            for (int target = 0; target < n; target++) {
                if (dist[target] != Double.NEGATIVE_INFINITY && dist[target] > maxLength) {
                    maxLength = dist[target];
                    bestSource = source;
                    bestTarget = target;
                }
            }
        }
        
        if (bestSource == -1) {
            return new PathResult(null, 0);
        }
        
        List<Integer> path = reconstructLongestPath(bestSource, bestTarget);
        return new PathResult(path, maxLength);
    }
    
    /**
     * Result of a path computation
     */
    public static class PathResult {
        public final List<Integer> path;
        public final double length;
        
        public PathResult(List<Integer> path, double length) {
            this.path = path;
            this.length = length;
        }
        
        @Override
        public String toString() {
            return "PathResult{path=" + path + ", length=" + length + '}';
        }
    }
}
