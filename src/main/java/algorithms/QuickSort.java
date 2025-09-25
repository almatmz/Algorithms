package algorithms;

import java.util.Random;

public class QuickSort {
    private static final Random RNG = new Random();

    public static void sort(int[] a, Metrics m) {
        if (a == null || a.length < 2) return;
        SortUtils.shuffle(a, m);
        m.incAllocations();
        quicksortIterative(a, 0, a.length - 1, m);
    }

    private static void quicksortIterative(int[] a, int lo, int hi, Metrics m) {
        while (lo < hi) {
            m.enterRecursion();
            int pivotIndex = lo + RNG.nextInt(hi - lo + 1);
            int pivotValue = a[pivotIndex];

            swap(a, pivotIndex, hi);
            int p = partition(a, lo, hi - 1, pivotValue, m);
            swap(a, p, hi);
            int leftSize = p - lo;
            int rightSize = hi - p;
            if (leftSize < rightSize) {
                quicksortIterative(a, lo, p - 1, m);
                lo = p + 1;
            } else {
                quicksortIterative(a, p + 1, hi, m);
                hi = p - 1;
            }
            m.exitRecursion();
        }
    }

    private static int partition(int[] a, int lo, int hi, int pivotValue, Metrics m) {
        int i = lo, j = hi;
        while (i <= j) {
            while (i <= j) {
                m.incComparisons();
                if (a[i] <= pivotValue) i++;
                else break;
            }
            while (i <= j) {
                m.incComparisons();
                if (a[j] > pivotValue) j--;
                else break;
            }
            if (i < j) swap(a, i++, j--);
        }
        return i;
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
}
