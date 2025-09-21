import algorithms.DeterministicSelect;
import algorithms.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Random;

public class SelectTests {
    private final Random rng = new Random(123);

    @Test
    public void testSelectCorrectness() {
        for (int n : new int[]{1,2,5,20,101}) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rng.nextInt(1000);
            int[] b = Arrays.copyOf(a, a.length);
            Arrays.sort(b);
            Metrics m = new Metrics();
            int k = n/2;
            int s = DeterministicSelect.select(Arrays.copyOf(a, a.length), k, m);
            assertEquals(b[k], s);
        }
    }
}
