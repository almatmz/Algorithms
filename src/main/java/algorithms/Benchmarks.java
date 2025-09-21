package algorithms;
import java.util.*;
import java.io.IOException;
import java.util.Random;

public class Benchmarks {
    private static final Random RNG = new Random(12345);

    public static int[] genRandomArray(int n, int range) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = RNG.nextInt(range);
        return a;
    }

    public static Point[] genRandomPoints(int n, int range) {
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point(RNG.nextInt(range), RNG.nextInt(range));
        }
        return pts;
    }

    public static void runSingle(String algorithm, int n, int trials, CSVWriter writer) throws IOException {
        Metrics m = new Metrics();
        long totalTime = 0;
        for (int t = 0; t < trials; t++) {
            m.reset();
            long t0, t1;
            switch (algorithm.toLowerCase()) {
                case "mergesort": {
                    int[] a = genRandomArray(n, Math.max(1000, n * 2));
                    int[] copy = SortUtils.copyOf(a);
                    t0 = System.nanoTime();
                    MergeSort.sort(copy, m);
                    t1 = System.nanoTime();
                    break;
                }
                case "quicksort": {
                    int[] a = genRandomArray(n, Math.max(1000, n * 2));
                    int[] copy = SortUtils.copyOf(a);
                    t0 = System.nanoTime();
                    QuickSort.sort(copy, m);
                    t1 = System.nanoTime();
                    break;
                }
                case "select": {
                    int[] a = genRandomArray(n, Math.max(1000, n * 2));
                    int[] copy = SortUtils.copyOf(a);
                    int k = copy.length / 2;
                    t0 = System.nanoTime();
                    DeterministicSelect.select(copy, k, m);
                    t1 = System.nanoTime();
                    break;
                }
                case "closest": {
                    Point[] pts = genRandomPoints(n, n * 10);
                    t0 = System.nanoTime();
                    ClosestPair.closestPair(pts, m);
                    t1 = System.nanoTime();
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
            }
            totalTime += (System.nanoTime() - t0);
        }
        writer.append(algorithm, n, trials, totalTime, m.getMaxDepth(), m.getComparisons(), m.getAllocations());
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            String[] algos = {"mergesort", "quicksort", "select", "closest"};
            int[] sizes = {1000, 10000, 100000};
            int trials = 5;
            CSVWriter writer = new CSVWriter("results.csv", true);
            for (String alg : algos) {
                for (int n : sizes) {
                    System.out.printf("Running %s with n=%d…%n", alg, n);
                    runSingle(alg, n, trials, writer);
                }
            }
            System.out.println("Benchmarking complete. Results saved to results.csv");
        } else if (args.length >= 4) {
            String alg = args[0];
            int n = Integer.parseInt(args[1]);
            int trials = Integer.parseInt(args[2]);
            String out = args[3];
            CSVWriter w = new CSVWriter(out, true);
            runSingle(alg, n, trials, w);
            System.out.println("Done writing to " + out);
        } else {
            System.out.println("Usage: Benchmarks <algorithm> <n> <trials> <outfile>");
        }
    }
}
