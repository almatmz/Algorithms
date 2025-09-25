package algorithms;

public class Metrics {
    private long comparisons = 0;
    private long allocations = 0;
    private int depth = 0;
    private int maxDepth = 0;

    public synchronized void incComparisons() {
        comparisons++;
    }
    public synchronized void incComparisons(long n) {
        comparisons += n;
    }

    public synchronized void incAllocations() {
        allocations++;
    }
    public synchronized void incAllocations(long n) {
        allocations += n;
    }

    public synchronized void enterRecursion() {
        depth++;
        if (depth > maxDepth) maxDepth = depth;
    }
    public synchronized void exitRecursion() {
        depth--;
    }

    public long getComparisons() {
        return comparisons;
    }
    public long getAllocations() {
        return allocations;
    }
    public int getMaxDepth() {
        return maxDepth;
    }

    public void reset() {
        comparisons = 0;
        allocations = 0;
        depth = 0;
        maxDepth = 0;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "comparisons=" + comparisons +
                ", allocations=" + allocations +
                ", maxDepth=" + maxDepth +
                '}';
    }
}
