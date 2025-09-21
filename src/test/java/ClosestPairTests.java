import algorithms.ClosestPair;
import algorithms.Metrics;
import algorithms.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class ClosestPairTests {
    private final Random rng = new Random(55);

    private double bruteForce(Point[] pts) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i].x - pts[j].x;
                double dy = pts[i].y - pts[j].y;
                double d = Math.sqrt(dx * dx + dy * dy);
                if (d < best) best = d;
            }
        }
        return best;
    }

    @Test
    public void testClosestPairSmall() {
        for (int n : new int[]{2, 3, 10, 50}) {
            Point[] pts = new Point[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new Point(rng.nextDouble() * 100, rng.nextDouble() * 100);
            }
            Metrics m = new Metrics();
            double ans = ClosestPair.closestPair(pts, m);
            double bf = bruteForce(pts);

            assertEquals(bf, ans, 1e-9,
                    () -> "Mismatch for n=" + n + ": brute=" + bf + ", closestPair=" + ans);
        }
    }
}
