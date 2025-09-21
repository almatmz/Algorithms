package algorithms;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter {
    private final String path;
    private final boolean headerWritten;

    public CSVWriter(String path, boolean overwrite) throws IOException {
        this.path = path;
        if (overwrite) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
                pw.println("algorithm,n,trials,time_ns,depth,comparisons,allocations");
            }
            headerWritten = true;
        } else {
            headerWritten = false;
        }
    }

    public void append(String algorithm, int n, int trials, long timeNs, int depth, long comp, long alloc) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, true))) {
            pw.printf("%s,%d,%d,%d,%d,%d,%d%n",
                    algorithm, n, trials, timeNs, depth, comp, alloc);
        }
    }
}
