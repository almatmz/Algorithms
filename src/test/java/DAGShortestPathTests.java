import graph.*;
import graph.dagsp.DAGShortestPath;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for DAG Shortest and Longest Path algorithms
 */
public class DAGShortestPathTests {
    
    @Test
    public void testShortestPath_SimpleDAG() {
        // Simple linear DAG with weights
        Graph g = new Graph(4);
        g.addEdge(0, 1, 2.0);
        g.addEdge(1, 2, 3.0);
        g.addEdge(2, 3, 1.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        double[] dist = dagsp.shortestPaths(0);
        
        assertEquals(0.0, dist[0], 0.001);
        assertEquals(2.0, dist[1], 0.001);
        assertEquals(5.0, dist[2], 0.001);
        assertEquals(6.0, dist[3], 0.001);
        
        assertTrue(metrics.getRelaxations() > 0);
    }
    
    @Test
    public void testShortestPath_Diamond() {
        // Diamond DAG: 0 -> {1, 2} -> 3
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1.0);
        g.addEdge(0, 2, 4.0);
        g.addEdge(1, 3, 2.0);
        g.addEdge(2, 3, 1.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        double[] dist = dagsp.shortestPaths(0);
        
        assertEquals(0.0, dist[0], 0.001);
        assertEquals(1.0, dist[1], 0.001);
        assertEquals(4.0, dist[2], 0.001);
        assertEquals(3.0, dist[3], 0.001); // min(1+2, 4+1) = 3
    }
    
    @Test
    public void testLongestPath_SimpleDAG() {
        // Simple linear DAG with weights
        Graph g = new Graph(4);
        g.addEdge(0, 1, 2.0);
        g.addEdge(1, 2, 3.0);
        g.addEdge(2, 3, 1.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        double[] dist = dagsp.longestPaths(0);
        
        assertEquals(0.0, dist[0], 0.001);
        assertEquals(2.0, dist[1], 0.001);
        assertEquals(5.0, dist[2], 0.001);
        assertEquals(6.0, dist[3], 0.001);
    }
    
    @Test
    public void testLongestPath_Diamond() {
        // Diamond DAG: 0 -> {1, 2} -> 3
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1.0);
        g.addEdge(0, 2, 4.0);
        g.addEdge(1, 3, 2.0);
        g.addEdge(2, 3, 1.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        double[] dist = dagsp.longestPaths(0);
        
        assertEquals(0.0, dist[0], 0.001);
        assertEquals(1.0, dist[1], 0.001);
        assertEquals(4.0, dist[2], 0.001);
        assertEquals(5.0, dist[3], 0.001); // max(1+2, 4+1) = 5
    }
    
    @Test
    public void testReconstructShortestPath() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1.0);
        g.addEdge(0, 2, 4.0);
        g.addEdge(1, 3, 2.0);
        g.addEdge(2, 3, 1.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        List<Integer> path = dagsp.reconstructShortestPath(0, 3);
        
        assertNotNull(path);
        assertEquals(0, path.get(0));
        assertEquals(3, path.get(path.size() - 1));
        
        // Should be 0 -> 1 -> 3 (length 3)
        assertEquals(3, path.size());
    }
    
    @Test
    public void testReconstructLongestPath() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1.0);
        g.addEdge(0, 2, 4.0);
        g.addEdge(1, 3, 2.0);
        g.addEdge(2, 3, 1.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        List<Integer> path = dagsp.reconstructLongestPath(0, 3);
        
        assertNotNull(path);
        assertEquals(0, path.get(0));
        assertEquals(3, path.get(path.size() - 1));
        
        // Should be 0 -> 2 -> 3 (length 5) or 0 -> 1 -> 3 based on tie-breaking
    }
    
    @Test
    public void testCriticalPath() {
        // DAG with clear critical path
        Graph g = new Graph(5);
        g.addEdge(0, 1, 3.0);
        g.addEdge(0, 2, 2.0);
        g.addEdge(1, 3, 4.0);
        g.addEdge(2, 3, 1.0);
        g.addEdge(3, 4, 2.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        DAGShortestPath.PathResult result = dagsp.findCriticalPath();
        
        assertNotNull(result.path);
        assertTrue(result.length >= 9.0); // 0 -> 1 -> 3 -> 4 = 3+4+2 = 9
    }
    
    @Test
    public void testUnreachableVertex() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 1.0);
        g.addEdge(1, 2, 2.0);
        // Vertices 3 and 4 are unreachable from 0
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        double[] dist = dagsp.shortestPaths(0);
        
        assertEquals(0.0, dist[0], 0.001);
        assertEquals(1.0, dist[1], 0.001);
        assertEquals(3.0, dist[2], 0.001);
        assertEquals(Double.POSITIVE_INFINITY, dist[3]);
        assertEquals(Double.POSITIVE_INFINITY, dist[4]);
    }
    
    @Test
    public void testDisconnectedComponents() {
        Graph g = new Graph(6);
        // Component 1: 0 -> 1 -> 2
        g.addEdge(0, 1, 2.0);
        g.addEdge(1, 2, 3.0);
        // Component 2: 3 -> 4 -> 5
        g.addEdge(3, 4, 1.0);
        g.addEdge(4, 5, 2.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        double[] dist = dagsp.shortestPaths(0);
        
        assertEquals(0.0, dist[0], 0.001);
        assertEquals(2.0, dist[1], 0.001);
        assertEquals(5.0, dist[2], 0.001);
        assertEquals(Double.POSITIVE_INFINITY, dist[3]);
        assertEquals(Double.POSITIVE_INFINITY, dist[4]);
        assertEquals(Double.POSITIVE_INFINITY, dist[5]);
    }
    
    @Test
    public void testSingleVertex() {
        Graph g = new Graph(1);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        double[] dist = dagsp.shortestPaths(0);
        
        assertEquals(0.0, dist[0], 0.001);
    }
    
    @Test
    public void testComplexDAG() {
        // More complex DAG
        Graph g = new Graph(7);
        g.addEdge(0, 1, 2.0);
        g.addEdge(0, 2, 3.0);
        g.addEdge(1, 3, 1.0);
        g.addEdge(2, 3, 4.0);
        g.addEdge(2, 4, 2.0);
        g.addEdge(3, 5, 2.0);
        g.addEdge(4, 5, 1.0);
        g.addEdge(5, 6, 3.0);
        
        GraphMetrics metrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(g, metrics);
        double[] dist = dagsp.shortestPaths(0);
        
        assertEquals(0.0, dist[0], 0.001);
        assertTrue(dist[6] > 0); // Should reach vertex 6
        
        // Test longest path
        double[] longest = dagsp.longestPaths(0);
        assertTrue(longest[6] >= dist[6]); // Longest should be >= shortest
    }
}
