import graph.*;
import graph.topo.TopologicalSort;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Topological Sort algorithms
 */
public class TopologicalSortTests {
    
    @Test
    public void testSimpleDAG_Kahn() {
        // Simple linear DAG: 0 -> 1 -> 2 -> 3
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        
        GraphMetrics metrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(g, metrics);
        List<Integer> order = topo.kahnSort();
        
        assertNotNull(order);
        assertEquals(4, order.size());
        
        // Verify order is valid
        assertEquals(0, order.get(0));
        assertEquals(3, order.get(3));
        
        assertTrue(metrics.getPops() > 0);
        assertTrue(metrics.getPushes() > 0);
    }
    
    @Test
    public void testSimpleDAG_DFS() {
        // Simple linear DAG: 0 -> 1 -> 2 -> 3
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        
        GraphMetrics metrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(g, metrics);
        List<Integer> order = topo.dfsSort();
        
        assertNotNull(order);
        assertEquals(4, order.size());
        
        // Verify topological order (each edge goes from earlier to later)
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            position.put(order.get(i), i);
        }
        
        assertTrue(position.get(0) < position.get(1));
        assertTrue(position.get(1) < position.get(2));
        assertTrue(position.get(2) < position.get(3));
    }
    
    @Test
    public void testDAGWithMultiplePaths() {
        // Diamond DAG: 0 -> {1, 2} -> 3
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);
        
        GraphMetrics metrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(g, metrics);
        List<Integer> order = topo.kahnSort();
        
        assertNotNull(order);
        assertEquals(4, order.size());
        
        // Verify order
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            position.put(order.get(i), i);
        }
        
        assertTrue(position.get(0) < position.get(1));
        assertTrue(position.get(0) < position.get(2));
        assertTrue(position.get(1) < position.get(3));
        assertTrue(position.get(2) < position.get(3));
    }
    
    @Test
    public void testCycleDetection_Kahn() {
        // Graph with cycle: 0 -> 1 -> 2 -> 0
        Graph g = new Graph(3);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        
        GraphMetrics metrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(g, metrics);
        List<Integer> order = topo.kahnSort();
        
        // Should return null for cyclic graph
        assertNull(order);
    }
    
    @Test
    public void testCycleDetection_DFS() {
        // Graph with cycle: 0 -> 1 -> 2 -> 0
        Graph g = new Graph(3);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        
        GraphMetrics metrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(g, metrics);
        List<Integer> order = topo.dfsSort();
        
        // Should return null for cyclic graph
        assertNull(order);
    }
    
    @Test
    public void testIsDAG() {
        // Test DAG
        Graph dag = new Graph(4);
        dag.addEdge(0, 1);
        dag.addEdge(1, 2);
        dag.addEdge(2, 3);
        
        GraphMetrics metrics1 = new GraphMetrics();
        TopologicalSort topo1 = new TopologicalSort(dag, metrics1);
        assertTrue(topo1.isDAG());
        
        // Test cyclic graph
        Graph cyclic = new Graph(3);
        cyclic.addEdge(0, 1);
        cyclic.addEdge(1, 2);
        cyclic.addEdge(2, 0);
        
        GraphMetrics metrics2 = new GraphMetrics();
        TopologicalSort topo2 = new TopologicalSort(cyclic, metrics2);
        assertFalse(topo2.isDAG());
    }
    
    @Test
    public void testDisconnectedDAG() {
        // DAG with disconnected components
        Graph g = new Graph(6);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        
        GraphMetrics metrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(g, metrics);
        List<Integer> order = topo.kahnSort();
        
        assertNotNull(order);
        assertEquals(6, order.size());
        
        // Verify partial orders
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            position.put(order.get(i), i);
        }
        
        assertTrue(position.get(0) < position.get(1));
        assertTrue(position.get(1) < position.get(2));
        assertTrue(position.get(3) < position.get(4));
        assertTrue(position.get(4) < position.get(5));
    }
    
    @Test
    public void testEmptyGraph() {
        Graph g = new Graph(3);
        // No edges
        
        GraphMetrics metrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(g, metrics);
        List<Integer> order = topo.kahnSort();
        
        assertNotNull(order);
        assertEquals(3, order.size());
    }
    
    @Test
    public void testComplexDAG() {
        // More complex DAG
        Graph g = new Graph(7);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);
        g.addEdge(2, 4);
        g.addEdge(3, 5);
        g.addEdge(4, 5);
        g.addEdge(5, 6);
        
        GraphMetrics metrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(g, metrics);
        List<Integer> order = topo.kahnSort();
        
        assertNotNull(order);
        assertEquals(7, order.size());
        
        // First element should be 0 (only source)
        assertEquals(0, order.get(0));
        // Last element should be 6 (only sink)
        assertEquals(6, order.get(6));
    }
}
