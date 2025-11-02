package graph;

import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Benchmark and demonstration program for Assignment 4.
 * Tests SCC, Topological Sort, and DAG Shortest Path algorithms on generated datasets.
 */
public class GraphBenchmarks {
    
    public static void main(String[] args) {
        System.out.println("=== Assignment 4: Graph Algorithms Benchmarks ===\n");
        
        String[] datasets = {
            "data/small_dag_7nodes.json",
            "data/small_cycle_8nodes.json",
            "data/small_two_cycles_10nodes.json",
            "data/medium_dag_15nodes.json",
            "data/medium_sccs_18nodes.json",
            "data/medium_sparse_12nodes.json",
            "data/large_dag_30nodes.json",
            "data/large_sccs_40nodes.json",
            "data/large_sparse_dag_50nodes.json"
        };
        
        for (String datasetPath : datasets) {
            try {
                System.out.println("==================================================");
                System.out.println("Dataset: " + datasetPath);
                processDataset(datasetPath);
                System.out.println();
            } catch (Exception e) {
                System.err.println("Error processing " + datasetPath + ": " + e.getMessage());
            }
        }
    }
    
    private static void processDataset(String filepath) throws IOException {
        Graph graph = GraphLoader.loadFromJson(filepath);
        
        System.out.println("Vertices: " + graph.getVertices());
        System.out.println("Edges: " + graph.getEdgeCount());
        
        // 1. Find Strongly Connected Components
        System.out.println("\n--- Strongly Connected Components (Tarjan) ---");
        GraphMetrics sccMetrics = new GraphMetrics();
        TarjanSCC scc = new TarjanSCC(graph, sccMetrics);
        List<List<Integer>> sccs = scc.findSCCs();
        
        System.out.println("Number of SCCs: " + sccs.size());
        System.out.println("SCC Sizes: " + scc.getSCCSizes());
        System.out.println("DFS Visits: " + sccMetrics.getDfsVisits());
        System.out.println("DFS Edges: " + sccMetrics.getDfsEdges());
        System.out.println("Time: " + String.format("%.3f ms", sccMetrics.getElapsedTimeMs()));
        
        // Build condensation graph
        Graph condensation = scc.buildCondensationGraph();
        System.out.println("Condensation DAG: " + condensation.getVertices() + " vertices, " 
                          + condensation.getEdgeCount() + " edges");
        
        // 2. Topological Sort (on condensation DAG)
        System.out.println("\n--- Topological Sort (Kahn's Algorithm) ---");
        GraphMetrics topoMetrics = new GraphMetrics();
        TopologicalSort topo = new TopologicalSort(condensation, topoMetrics);
        List<Integer> topoOrder = topo.kahnSort();
        
        if (topoOrder != null) {
            System.out.println("Topological Order: " + topoOrder);
            System.out.println("Pops: " + topoMetrics.getPops());
            System.out.println("Pushes: " + topoMetrics.getPushes());
            System.out.println("Time: " + String.format("%.3f ms", topoMetrics.getElapsedTimeMs()));
            
            // 3. DAG Shortest/Longest Paths (only for DAGs)
            if (condensation.getVertices() > 1) {
                System.out.println("\n--- DAG Shortest/Longest Paths ---");
                runDAGShortestPath(condensation);
            }
        } else {
            System.out.println("Graph is cyclic (no topological order exists)");
        }
        
        // Derive original task order from SCC topological order
        if (topoOrder != null && sccs.size() > 1) {
            System.out.println("\n--- Original Task Order (after SCC compression) ---");
            List<Integer> taskOrder = new ArrayList<>();
            for (int sccIdx : topoOrder) {
                taskOrder.addAll(sccs.get(sccIdx));
            }
            System.out.println("Task execution order: " + taskOrder);
        }
    }
    
    private static void runDAGShortestPath(Graph dag) {
        // Find a good source vertex (one with in-degree 0)
        int source = findSourceVertex(dag);
        if (source == -1) {
            source = 0; // Fallback to vertex 0
        }
        
        GraphMetrics spMetrics = new GraphMetrics();
        DAGShortestPath dagsp = new DAGShortestPath(dag, spMetrics);
        
        // Shortest paths
        double[] shortest = dagsp.shortestPaths(source);
        System.out.println("Shortest paths from source " + source + ":");
        for (int i = 0; i < Math.min(10, shortest.length); i++) {
            if (shortest[i] != Double.POSITIVE_INFINITY) {
                System.out.println("  to " + i + ": " + String.format("%.2f", shortest[i]));
            }
        }
        System.out.println("Relaxations: " + spMetrics.getRelaxations());
        System.out.println("Time: " + String.format("%.3f ms", spMetrics.getElapsedTimeMs()));
        
        // Longest paths (critical path)
        GraphMetrics lpMetrics = new GraphMetrics();
        DAGShortestPath dagsp2 = new DAGShortestPath(dag, lpMetrics);
        DAGShortestPath.PathResult criticalPath = dagsp2.findCriticalPath();
        
        System.out.println("\nCritical Path (longest):");
        if (criticalPath.path != null) {
            System.out.println("  Path: " + criticalPath.path);
            System.out.println("  Length: " + String.format("%.2f", criticalPath.length));
        } else {
            System.out.println("  No critical path found");
        }
    }
    
    private static int findSourceVertex(Graph g) {
        int n = g.getVertices();
        boolean[] hasIncoming = new boolean[n];
        
        for (int v = 0; v < n; v++) {
            for (Graph.Edge e : g.getAdjacent(v)) {
                hasIncoming[e.to] = true;
            }
        }
        
        for (int v = 0; v < n; v++) {
            if (!hasIncoming[v]) {
                return v;
            }
        }
        
        return -1;
    }
}
