package graph;

import java.io.IOException;
import java.util.*;

/**
 * Generator for test datasets as specified in Assignment 4.
 * Creates 9 datasets: 3 small (6-10 nodes), 3 medium (10-20 nodes), 3 large (20-50 nodes)
 */
public class DatasetGenerator {
    
    public static void main(String[] args) {
        try {
            generateAllDatasets();
            System.out.println("All 9 datasets generated successfully in /data directory");
        } catch (IOException e) {
            System.err.println("Error generating datasets: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void generateAllDatasets() throws IOException {
        // Small datasets (6-10 nodes)
        generateSmallDatasets();
        
        // Medium datasets (10-20 nodes)
        generateMediumDatasets();
        
        // Large datasets (20-50 nodes)
        generateLargeDatasets();
    }
    
    private static void generateSmallDatasets() throws IOException {
        // Small 1: Simple DAG (7 nodes, sparse)
        Graph small1 = new Graph(7);
        small1.addEdge(0, 1, 2.0);
        small1.addEdge(0, 2, 3.0);
        small1.addEdge(1, 3, 1.0);
        small1.addEdge(2, 3, 4.0);
        small1.addEdge(3, 4, 2.0);
        small1.addEdge(4, 5, 1.0);
        small1.addEdge(4, 6, 3.0);
        GraphLoader.saveToJson(small1, "data/small_dag_7nodes.json");
        
        // Small 2: Graph with one cycle (8 nodes)
        Graph small2 = new Graph(8);
        small2.addEdge(0, 1, 1.0);
        small2.addEdge(1, 2, 2.0);
        small2.addEdge(2, 3, 1.0);
        small2.addEdge(3, 1, 1.0); // Creates cycle: 1 -> 2 -> 3 -> 1
        small2.addEdge(3, 4, 3.0);
        small2.addEdge(4, 5, 2.0);
        small2.addEdge(5, 6, 1.0);
        small2.addEdge(6, 7, 2.0);
        GraphLoader.saveToJson(small2, "data/small_cycle_8nodes.json");
        
        // Small 3: Graph with two small cycles (10 nodes)
        Graph small3 = new Graph(10);
        small3.addEdge(0, 1, 1.0);
        small3.addEdge(1, 2, 2.0);
        small3.addEdge(2, 1, 1.0); // Cycle 1: 1 <-> 2
        small3.addEdge(2, 3, 3.0);
        small3.addEdge(3, 4, 1.0);
        small3.addEdge(4, 5, 2.0);
        small3.addEdge(5, 6, 1.0);
        small3.addEdge(6, 7, 2.0);
        small3.addEdge(7, 5, 1.0); // Cycle 2: 5 -> 6 -> 7 -> 5
        small3.addEdge(7, 8, 3.0);
        small3.addEdge(8, 9, 1.0);
        GraphLoader.saveToJson(small3, "data/small_two_cycles_10nodes.json");
    }
    
    private static void generateMediumDatasets() throws IOException {
        // Medium 1: DAG with multiple paths (15 nodes, medium density)
        Graph med1 = new Graph(15);
        med1.addEdge(0, 1, 3.0);
        med1.addEdge(0, 2, 2.0);
        med1.addEdge(1, 3, 1.0);
        med1.addEdge(1, 4, 4.0);
        med1.addEdge(2, 4, 2.0);
        med1.addEdge(2, 5, 3.0);
        med1.addEdge(3, 6, 1.0);
        med1.addEdge(4, 6, 2.0);
        med1.addEdge(4, 7, 3.0);
        med1.addEdge(5, 7, 1.0);
        med1.addEdge(6, 8, 2.0);
        med1.addEdge(7, 8, 1.0);
        med1.addEdge(7, 9, 4.0);
        med1.addEdge(8, 10, 2.0);
        med1.addEdge(9, 10, 1.0);
        med1.addEdge(10, 11, 3.0);
        med1.addEdge(11, 12, 1.0);
        med1.addEdge(11, 13, 2.0);
        med1.addEdge(12, 14, 1.0);
        med1.addEdge(13, 14, 2.0);
        GraphLoader.saveToJson(med1, "data/medium_dag_15nodes.json");
        
        // Medium 2: Mixed with several SCCs (18 nodes, dense)
        Graph med2 = new Graph(18);
        // SCC 1: nodes 0-2
        med2.addEdge(0, 1, 1.0);
        med2.addEdge(1, 2, 2.0);
        med2.addEdge(2, 0, 1.0);
        // SCC 2: nodes 3-5
        med2.addEdge(3, 4, 3.0);
        med2.addEdge(4, 5, 1.0);
        med2.addEdge(5, 3, 2.0);
        // SCC 3: nodes 6-9
        med2.addEdge(6, 7, 2.0);
        med2.addEdge(7, 8, 1.0);
        med2.addEdge(8, 9, 3.0);
        med2.addEdge(9, 6, 1.0);
        // Connect SCCs
        med2.addEdge(2, 3, 4.0);
        med2.addEdge(5, 6, 2.0);
        med2.addEdge(9, 10, 3.0);
        med2.addEdge(10, 11, 1.0);
        med2.addEdge(11, 12, 2.0);
        med2.addEdge(12, 13, 1.0);
        med2.addEdge(13, 14, 3.0);
        med2.addEdge(14, 15, 1.0);
        med2.addEdge(15, 16, 2.0);
        med2.addEdge(16, 17, 1.0);
        GraphLoader.saveToJson(med2, "data/medium_sccs_18nodes.json");
        
        // Medium 3: Sparse graph with few cycles (12 nodes)
        Graph med3 = new Graph(12);
        med3.addEdge(0, 1, 2.0);
        med3.addEdge(1, 2, 1.0);
        med3.addEdge(2, 3, 3.0);
        med3.addEdge(3, 4, 1.0);
        med3.addEdge(4, 2, 2.0); // Cycle: 2 -> 3 -> 4 -> 2
        med3.addEdge(4, 5, 1.0);
        med3.addEdge(5, 6, 2.0);
        med3.addEdge(6, 7, 1.0);
        med3.addEdge(7, 8, 3.0);
        med3.addEdge(8, 9, 1.0);
        med3.addEdge(9, 10, 2.0);
        med3.addEdge(10, 11, 1.0);
        GraphLoader.saveToJson(med3, "data/medium_sparse_12nodes.json");
    }
    
    private static void generateLargeDatasets() throws IOException {
        // Large 1: DAG for performance testing (30 nodes, dense)
        Graph large1 = new Graph(30);
        for (int i = 0; i < 29; i++) {
            for (int j = i + 1; j <= Math.min(i + 3, 29); j++) {
                large1.addEdge(i, j, 1.0 + (j - i));
            }
        }
        GraphLoader.saveToJson(large1, "data/large_dag_30nodes.json");
        
        // Large 2: Large graph with multiple SCCs (40 nodes)
        Graph large2 = new Graph(40);
        Random rand = new Random(42); // Fixed seed for reproducibility
        
        // Create 5 SCCs of varying sizes
        int[] sccSizes = {5, 6, 7, 8, 4};
        int nodeIdx = 0;
        
        for (int sccIdx = 0; sccIdx < sccSizes.length; sccIdx++) {
            int size = sccSizes[sccIdx];
            int start = nodeIdx;
            
            // Create a cycle for the SCC
            for (int i = 0; i < size; i++) {
                int from = start + i;
                int to = start + ((i + 1) % size);
                large2.addEdge(from, to, 1.0 + rand.nextDouble() * 3.0);
                
                // Add some internal edges
                if (i < size - 2) {
                    large2.addEdge(from, start + i + 2, 1.0 + rand.nextDouble() * 2.0);
                }
            }
            
            nodeIdx += size;
        }
        
        // Connect SCCs
        for (int i = 0; i < sccSizes.length - 1; i++) {
            int from = sum(sccSizes, i) + rand.nextInt(sccSizes[i]);
            int to = sum(sccSizes, i + 1) + rand.nextInt(sccSizes[i + 1]);
            large2.addEdge(from, to, 2.0 + rand.nextDouble() * 3.0);
        }
        
        // Add remaining nodes as individual vertices
        for (int i = nodeIdx; i < 39; i++) {
            large2.addEdge(i, i + 1, 1.0 + rand.nextDouble() * 2.0);
        }
        
        GraphLoader.saveToJson(large2, "data/large_sccs_40nodes.json");
        
        // Large 3: Very large sparse DAG (50 nodes)
        Graph large3 = new Graph(50);
        Random rand2 = new Random(123);
        
        for (int i = 0; i < 49; i++) {
            // Each node connects to 1-3 nodes ahead
            int numEdges = 1 + rand2.nextInt(3);
            for (int e = 0; e < numEdges; e++) {
                int offset = 1 + rand2.nextInt(Math.min(5, 49 - i));
                int to = i + offset;
                if (to < 50) {
                    large3.addEdge(i, to, 1.0 + rand2.nextDouble() * 4.0);
                }
            }
        }
        
        GraphLoader.saveToJson(large3, "data/large_sparse_dag_50nodes.json");
    }
    
    private static int sum(int[] arr, int upTo) {
        int sum = 0;
        for (int i = 0; i < upTo; i++) {
            sum += arr[i];
        }
        return sum;
    }
}
