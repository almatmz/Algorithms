package algorithms;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    // small CLI: run basic benchmark or run algorithms interactively
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            printUsage();
            return;
        }
        switch (args[0]) {
            case "bench":
                // bench <algorithm> <n> <trials> <outfile>
                if (args.length < 5) { printUsage(); return; }
                Benchmarks.main(Arrays.copyOfRange(args,1,args.length));
                break;
            case "run":
                // run <algorithm> <n>
                if (args.length < 3) { printUsage(); return; }
                String alg = args[1];
                int n = Integer.parseInt(args[2]);
                CSVWriter w = new CSVWriter("results.csv", true);
                Benchmarks.runSingle(alg, n, 1, w);
                System.out.println("Wrote results.csv");
                break;
            default:
                printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -jar daac.jar bench <algorithm> <n> <trials> <outfile>");
        System.out.println("  java -jar daac.jar run <algorithm> <n>          (writes results.csv)");
        System.out.println("Algorithms: mergesort, quicksort, select");
    }
}
