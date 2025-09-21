package algorithms;

import java.util.Random;
import java.util.Arrays;

public class SortUtils {
    private static final Random RNG = new Random();

    public static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

    public static void shuffle(int[] a, Metrics m) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = RNG.nextInt(i + 1);
            swap(a, i, j);
        }
        m.incAllocations();
    }

    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i-1] > a[i]) return false;
        return true;
    }

    public static int[] copyOf(int[] a) {
        return Arrays.copyOf(a, a.length);
    }
}
