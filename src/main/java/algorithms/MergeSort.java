package algorithms;
public class MergeSort {
    private static final int INSERTION_CUTOFF = 16;

    public static void sort(int[] a, Metrics m) {
        if (a == null || a.length < 2) return;
        int[] buf = new int[a.length];
        m.incAllocations(); // buffer counted as allocation
        mergesort(a, 0, a.length, buf, m);
    }

    private static void mergesort(int[] a, int l, int r, int[] buf, Metrics m) {
        if (r - l <= INSERTION_CUTOFF) {
            insertionSort(a, l, r, m);
            return;
        }
        m.enterRecursion();
        int mid = (l + r) >>> 1;
        mergesort(a, l, mid, buf, m);
        mergesort(a, mid, r, buf, m);

        // merge a[l..mid-1] and a[mid..r-1] into buf[l..r-1]
        int i = l, j = mid, k = l;
        while (i < mid && j < r) {
            m.incComparisons();
            if (a[i] <= a[j]) buf[k++] = a[i++];
            else buf[k++] = a[j++];
        }
        while (i < mid) buf[k++] = a[i++];
        while (j < r) buf[k++] = a[j++];
        System.arraycopy(buf, l, a, l, r - l);
        m.exitRecursion();
    }

    private static void insertionSort(int[] a, int l, int r, Metrics m) {
        for (int i = l + 1; i < r; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= l) {
                m.incComparisons();
                if (a[j] > key) {
                    a[j+1] = a[j];
                    j--;
                } else break;
            }
            a[j+1] = key;
        }
    }
}

