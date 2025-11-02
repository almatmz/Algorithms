package graph.scc;

import graph.Graph;
import graph.GraphMetrics;

import java.util.*;

/**
 * Tarjan's algorithm for finding Strongly Connected Components (SCCs).
 * Runs in O(V + E) time using a single DFS traversal.
 */
public class TarjanSCC {
    private final Graph graph;
    private final GraphMetrics metrics;
    
    private int[] ids;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private int id;
    private List<List<Integer>> sccs;
    
    /**
     * Create SCC finder for the given graph
     */
    public TarjanSCC(Graph graph, GraphMetrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }
    
    /**
     * Find all strongly connected components
     * @return List of SCCs, where each SCC is a list of vertex IDs
     */
    public List<List<Integer>> findSCCs() {
        int n = graph.getVertices();
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        id = 0;
        sccs = new ArrayList<>();
        
        Arrays.fill(ids, -1);
        
        metrics.startTiming();
        
        for (int v = 0; v < n; v++) {
            if (ids[v] == -1) {
                dfs(v);
            }
        }
        
        metrics.endTiming();
        
        return sccs;
    }
    
    private void dfs(int at) {
        metrics.incDfsVisits();
        
        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = id++;
        
        for (Graph.Edge edge : graph.getAdjacent(at)) {
            int to = edge.to;
            metrics.incDfsEdges();
            
            if (ids[to] == -1) {
                dfs(to);
            }
            if (onStack[to]) {
                low[at] = Math.min(low[at], low[to]);
            }
        }
        
        // If at is a root node, pop the stack and create SCC
        if (ids[at] == low[at]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                scc.add(node);
                if (node == at) break;
            }
            sccs.add(scc);
        }
    }
    
    /**
     * Get the SCC sizes
     */
    public List<Integer> getSCCSizes() {
        List<Integer> sizes = new ArrayList<>();
        for (List<Integer> scc : sccs) {
            sizes.add(scc.size());
        }
        return sizes;
    }
    
    /**
     * Build condensation graph (DAG of SCCs)
     * Each SCC becomes a single vertex in the condensation graph
     */
    public Graph buildCondensationGraph() {
        int numSCCs = sccs.size();
        Graph condensation = new Graph(numSCCs);
        
        // Map each vertex to its SCC index
        int[] vertexToSCC = new int[graph.getVertices()];
        for (int i = 0; i < numSCCs; i++) {
            for (int v : sccs.get(i)) {
                vertexToSCC[v] = i;
            }
        }
        
        // Add edges between different SCCs
        Set<String> addedEdges = new HashSet<>();
        for (int v = 0; v < graph.getVertices(); v++) {
            int fromSCC = vertexToSCC[v];
            for (Graph.Edge edge : graph.getAdjacent(v)) {
                int toSCC = vertexToSCC[edge.to];
                if (fromSCC != toSCC) {
                    String edgeKey = fromSCC + "->" + toSCC;
                    if (!addedEdges.contains(edgeKey)) {
                        condensation.addEdge(fromSCC, toSCC, edge.weight);
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }
        
        return condensation;
    }
    
    /**
     * Get the SCCs
     */
    public List<List<Integer>> getSCCs() {
        return sccs;
    }
}
