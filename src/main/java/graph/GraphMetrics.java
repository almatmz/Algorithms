package graph;

/**
 * Metrics interface for graph algorithms.
 * Tracks operation counters and timing for SCC, Topological Sort, and DAG Shortest Path algorithms.
 */
public class GraphMetrics {
    private long dfsVisits = 0;
    private long dfsEdges = 0;
    private long pops = 0;
    private long pushes = 0;
    private long relaxations = 0;
    private long startTime = 0;
    private long endTime = 0;

    /**
     * Start timing
     */
    public void startTiming() {
        startTime = System.nanoTime();
    }

    /**
     * End timing
     */
    public void endTiming() {
        endTime = System.nanoTime();
    }

    /**
     * Get elapsed time in milliseconds
     */
    public double getElapsedTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }

    /**
     * Get elapsed time in nanoseconds
     */
    public long getElapsedTimeNs() {
        return endTime - startTime;
    }

    // DFS metrics (for SCC)
    public void incDfsVisits() {
        dfsVisits++;
    }

    public void incDfsEdges() {
        dfsEdges++;
    }

    public long getDfsVisits() {
        return dfsVisits;
    }

    public long getDfsEdges() {
        return dfsEdges;
    }

    // Queue metrics (for Kahn's algorithm)
    public void incPops() {
        pops++;
    }

    public void incPushes() {
        pushes++;
    }

    public long getPops() {
        return pops;
    }

    public long getPushes() {
        return pushes;
    }

    // Relaxation metrics (for DAG Shortest Path)
    public void incRelaxations() {
        relaxations++;
    }

    public long getRelaxations() {
        return relaxations;
    }

    /**
     * Reset all metrics
     */
    public void reset() {
        dfsVisits = 0;
        dfsEdges = 0;
        pops = 0;
        pushes = 0;
        relaxations = 0;
        startTime = 0;
        endTime = 0;
    }

    @Override
    public String toString() {
        return "GraphMetrics{" +
                "dfsVisits=" + dfsVisits +
                ", dfsEdges=" + dfsEdges +
                ", pops=" + pops +
                ", pushes=" + pushes +
                ", relaxations=" + relaxations +
                ", elapsedMs=" + getElapsedTimeMs() +
                '}';
    }
}
