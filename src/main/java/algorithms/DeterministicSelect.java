package algorithms;

import java.util.Arrays;

public class DeterministicSelect {
    public static int select(int[] a, int k, Metrics m) {
        if (a == null || k < 0 || k >= a.length) {
            throw new IllegalArgumentException("Invalid input array or k");
        }
        return selectInplace(a, 0, a.length - 1, k, m);
    }

    private static int selectInplace(int[] a, int left, int right, int k, Metrics m) {
        while (true) {
            if (left == right) return a[left];

            int pivotValue = medianOfMedians(a, left, right, m);
            int pivotIndex = partitionAroundPivotValue(a, left, right, pivotValue, m);
            int rank = pivotIndex - left;

            if (k == rank) {
                return a[pivotIndex];
            } else if (k < rank) {
                right = pivotIndex - 1;
            } else {
                k = k - rank - 1;
                left = pivotIndex + 1;
            }
        }
    }

    private static int partitionAroundPivotValue(int[] a, int left, int right, int pivotValue, Metrics m) {
        int pivotIdx = left;
        while (pivotIdx <= right && a[pivotIdx] != pivotValue) pivotIdx++;
        if (pivotIdx > right) {
            pivotIdx = (left + right) >>> 1;
        }

        swap(a, pivotIdx, right);
        int store = left;
        for (int i = left; i < right; i++) {
            m.incComparisons();
            if (a[i] < pivotValue) {
                swap(a, store++, i);
            }
        }
        swap(a, store, right);
        return store;
    }

    private static int medianOfMedians(int[] a, int left, int right, Metrics m) {
        int n = right - left + 1;
        if (n <= 5) {
            insertionSort(a, left, right, m);
            return a[left + (n - 1) / 2];
        }

        int numMedians = 0;
        for (int i = left; i <= right; i += 5) {
            int subR = Math.min(i + 4, right);
            insertionSort(a, i, subR, m);
            int medianIdx = i + (subR - i) / 2;
            swap(a, left + numMedians, medianIdx);
            numMedians++;
        }
        m.incAllocations();

        m.enterRecursion();
        int momValue = selectInplace(a, left, left + numMedians - 1, numMedians / 2, m);
        m.exitRecursion();
        return momValue;
    }

    private static void insertionSort(int[] a, int l, int r, Metrics m) {
        for (int i = l + 1; i <= r; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= l) {
                m.incComparisons();
                if (a[j] > key) {
                    a[j + 1] = a[j];
                    j--;
                } else break;
            }
            a[j + 1] = key;
        }
    }

    private static void swap(int[] a, int i, int j) {
        SortUtils.swap(a, i, j);
    }
}
