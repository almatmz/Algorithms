import algorithms.MergeSort;
import algorithms.Metrics;
import algorithms.QuickSort;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.Arrays;

public class SortTests {
    private final Random rng = new Random(42);

    private int[] randomArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rng.nextInt(n * 3 + 1);
        return a;
    }

    @Test
    public void testMergeSort() {
        Metrics m = new Metrics();
        for (int n : new int[]{0, 1, 2, 10, 50, 1000}) {
            int[] a = randomArray(n);
            int[] b = Arrays.copyOf(a, a.length);
            MergeSort.sort(a, m);
            Arrays.sort(b);
            assertArrayEquals(b, a);
        }
    }
    @Test
    public void testQuickSort() {
        Metrics m = new Metrics();
        for (int n : new int[]{0,1,2,10,50,1000}) {
            int[] a = randomArray(n);
            int[] b = Arrays.copyOf(a, a.length);
            QuickSort.sort(a, m);
            Arrays.sort(b);
            assertArrayEquals(b, a);
        }
    }
}