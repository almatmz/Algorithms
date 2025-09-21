package algorithms;

import java.util.Random;
import java.io.IOException;

public class Benchmarks {
    private static final Random RNG = new Random(12345);

    public static int[] genRandomArray(int n, int range) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = RNG.nextInt(range);
        return a;
    }

    public static void runSingle(String algorithm, int n, int trials, CSVWriter writer) throws IOException {
        Metrics m = new Metrics();
        long totalTime = 0;
        for (int t = 0; t < trials; t++) {
            int[] a = genRandomArray(n, Math.max(1000, n * 2));
            int[] copy = SortUtils.copyOf(a);
            m.reset();
            long t0 = System.nanoTime();
            switch (algorithm.toLowerCase()) {
                case "mergesort":
                    MergeSort.sort(copy, m);
                    break;
                case "quicksort":
                    QuickSort.sort(copy, m);
                    break;
                case "select":
                    // choose median
                    int k = copy.length/2;
                    DeterministicSelect.select(copy, k, m);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
            }
            long t1 = System.nanoTime();
            totalTime += (t1 - t0);
        }
        writer.append(algorithm, n, trials, totalTime, m.getMaxDepth(), m.getComparisons(), m.getAllocations());
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Usage: Benchmarks <algorithm> <n> <trials> <outfile>");
            System.exit(1);
        }
        String alg = args[0];
        int n = Integer.parseInt(args[1]);
        int trials = Integer.parseInt(args[2]);
        String out = args[3];
        CSVWriter w = new CSVWriter(out, true);
        runSingle(alg, n, trials, w);
        System.out.println("Done writing to " + out);
    }
}
