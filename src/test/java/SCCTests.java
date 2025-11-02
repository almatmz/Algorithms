import graph.*;
import graph.scc.TarjanSCC;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Strongly Connected Components using Tarjan's algorithm
 */
public class SCCTests {
    
    @Test
    public void testSimpleDAG() {
        // Simple DAG with no cycles
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        
        GraphMetrics metrics = new GraphMetrics();
        TarjanSCC scc = new TarjanSCC(g, metrics);
        List<List<Integer>> sccs = scc.findSCCs();
        
        // Each vertex should be in its own SCC
        assertEquals(4, sccs.size());
        
        // Verify metrics
        assertTrue(metrics.getDfsVisits() > 0);
        assertTrue(metrics.getElapsedTimeMs() >= 0);
    }
    
    @Test
    public void testSingleCycle() {
        // Graph with one cycle: 0 -> 1 -> 2 -> 0
        Graph g = new Graph(3);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        
        GraphMetrics metrics = new GraphMetrics();
        TarjanSCC scc = new TarjanSCC(g, metrics);
        List<List<Integer>> sccs = scc.findSCCs();
        
        // All three vertices should be in one SCC
        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
    }
    
    @Test
    public void testMultipleSCCs() {
        // Graph with two SCCs: {0,1,2} and {3,4}
        Graph g = new Graph(5);
        // First SCC
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        // Second SCC
        g.addEdge(3, 4);
        g.addEdge(4, 3);
        // Connection between SCCs
        g.addEdge(2, 3);
        
        GraphMetrics metrics = new GraphMetrics();
        TarjanSCC scc = new TarjanSCC(g, metrics);
        List<List<Integer>> sccs = scc.findSCCs();
        
        assertEquals(2, sccs.size());
        
        // Check SCC sizes
        List<Integer> sizes = scc.getSCCSizes();
        sizes.sort(Collections.reverseOrder());
        assertEquals(3, sizes.get(0));
        assertEquals(2, sizes.get(1));
    }
    
    @Test
    public void testCondensationGraph() {
        // Graph with two SCCs connected by an edge
        Graph g = new Graph(5);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(3, 4);
        g.addEdge(4, 3);
        g.addEdge(2, 3);
        
        GraphMetrics metrics = new GraphMetrics();
        TarjanSCC scc = new TarjanSCC(g, metrics);
        scc.findSCCs();
        
        Graph condensation = scc.buildCondensationGraph();
        
        // Should have 2 vertices (2 SCCs)
        assertEquals(2, condensation.getVertices());
        
        // Should have at least 1 edge (connection between SCCs)
        assertTrue(condensation.getEdgeCount() >= 1);
    }
    
    @Test
    public void testSelfLoop() {
        // Graph with self-loop
        Graph g = new Graph(3);
        g.addEdge(0, 0); // Self-loop
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        
        GraphMetrics metrics = new GraphMetrics();
        TarjanSCC scc = new TarjanSCC(g, metrics);
        List<List<Integer>> sccs = scc.findSCCs();
        
        // Vertex 0 should be in its own SCC due to self-loop
        assertEquals(3, sccs.size());
    }
    
    @Test
    public void testComplexGraph() {
        // More complex graph with multiple SCCs
        Graph g = new Graph(8);
        // SCC 1: {0, 1, 2}
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        
        // SCC 2: {3, 4, 5}
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 3);
        
        // Individual vertices: 6, 7
        
        // Connections
        g.addEdge(2, 3);
        g.addEdge(0, 6);
        g.addEdge(6, 7);
        g.addEdge(5, 7);
        
        GraphMetrics metrics = new GraphMetrics();
        TarjanSCC scc = new TarjanSCC(g, metrics);
        List<List<Integer>> sccs = scc.findSCCs();
        
        assertEquals(4, sccs.size()); // 2 SCCs of size 3, and 2 single vertices
    }
    
    @Test
    public void testEmptyGraph() {
        Graph g = new Graph(5);
        // No edges
        
        GraphMetrics metrics = new GraphMetrics();
        TarjanSCC scc = new TarjanSCC(g, metrics);
        List<List<Integer>> sccs = scc.findSCCs();
        
        // Each vertex should be its own SCC
        assertEquals(5, sccs.size());
    }
}
