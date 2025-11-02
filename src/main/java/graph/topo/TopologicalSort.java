package graph.topo;

import graph.Graph;
import graph.GraphMetrics;

import java.util.*;

/**
 * Topological sorting using Kahn's algorithm (BFS-based).
 * Works only on DAGs (Directed Acyclic Graphs).
 */
public class TopologicalSort {
    private final Graph graph;
    private final GraphMetrics metrics;
    
    /**
     * Create topological sort for the given graph
     */
    public TopologicalSort(Graph graph, GraphMetrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }
    
    /**
     * Compute topological order using Kahn's algorithm
     * @return List of vertices in topological order, or null if graph has cycles
     */
    public List<Integer> kahnSort() {
        int n = graph.getVertices();
        int[] inDegree = new int[n];
        
        // Compute in-degrees
        for (int v = 0; v < n; v++) {
            for (Graph.Edge edge : graph.getAdjacent(v)) {
                inDegree[edge.to]++;
            }
        }
        
        // Initialize queue with vertices of in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.offer(v);
                metrics.incPushes();
            }
        }
        
        List<Integer> order = new ArrayList<>();
        
        metrics.startTiming();
        
        while (!queue.isEmpty()) {
            int v = queue.poll();
            metrics.incPops();
            order.add(v);
            
            for (Graph.Edge edge : graph.getAdjacent(v)) {
                int to = edge.to;
                inDegree[to]--;
                if (inDegree[to] == 0) {
                    queue.offer(to);
                    metrics.incPushes();
                }
            }
        }
        
        metrics.endTiming();
        
        // Check if all vertices are included (no cycles)
        if (order.size() != n) {
            return null; // Graph has a cycle
        }
        
        return order;
    }
    
    /**
     * Compute topological order using DFS
     * @return List of vertices in topological order, or null if graph has cycles
     */
    public List<Integer> dfsSort() {
        int n = graph.getVertices();
        boolean[] visited = new boolean[n];
        boolean[] recStack = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        
        metrics.startTiming();
        
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                if (!dfsVisit(v, visited, recStack, stack)) {
                    metrics.endTiming();
                    return null; // Cycle detected
                }
            }
        }
        
        metrics.endTiming();
        
        // Convert stack to list (reverse post-order)
        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) {
            order.add(stack.pop());
        }
        
        return order;
    }
    
    private boolean dfsVisit(int v, boolean[] visited, boolean[] recStack, Deque<Integer> stack) {
        visited[v] = true;
        recStack[v] = true;
        
        for (Graph.Edge edge : graph.getAdjacent(v)) {
            int to = edge.to;
            
            if (recStack[to]) {
                return false; // Cycle detected
            }
            
            if (!visited[to]) {
                if (!dfsVisit(to, visited, recStack, stack)) {
                    return false;
                }
            }
        }
        
        recStack[v] = false;
        stack.push(v);
        return true;
    }
    
    /**
     * Check if graph is a DAG (has no cycles)
     */
    public boolean isDAG() {
        return kahnSort() != null;
    }
}
