package algorithms;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair {
    public static class Point {
        public final double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
    }

    public static double closestPair(Point[] pts, Metrics m) {
        if (pts == null || pts.length < 2) return Double.POSITIVE_INFINITY;
        Point[] byX = Arrays.copyOf(pts, pts.length);
        Arrays.sort(byX, Comparator.comparingDouble(p -> p.x));
        m.incAllocations();
        Point[] aux = new Point[pts.length];
        m.incAllocations();
        return Math.sqrt(closestRec(byX, aux, 0, pts.length, m));
    }

    private static double closestRec(Point[] byX, Point[] aux, int l, int r, Metrics m) {
        int n = r - l;
        if (n <= 3) {
            double best = Double.POSITIVE_INFINITY;
            for (int i = l; i < r; i++) for (int j = i+1; j < r; j++) {
                m.incComparisons();
                double d = dist2(byX[i], byX[j]);
                if (d < best) best = d;
            }

            Arrays.sort(byX, l, r, Comparator.comparingDouble(p -> p.y));
            return best;
        }
        int mid = (l + r) >>> 1;
        double midx = byX[mid].x;
        m.enterRecursion();
        double dl = closestRec(byX, aux, l, mid, m);
        double dr = closestRec(byX, aux, mid, r, m);
        m.exitRecursion();
        double d = Math.min(dl, dr);

        int i = l, j = mid, k = l;
        while (i < mid && j < r) {
            if (byX[i].y <= byX[j].y) aux[k++] = byX[i++];
            else aux[k++] = byX[j++];
        }
        while (i < mid) aux[k++] = byX[i++];
        while (j < r) aux[k++] = byX[j++];
        System.arraycopy(aux, l, byX, l, n);


        int stripCount = 0;
        for (int idx = l; idx < r; idx++) {
            if ((byX[idx].x - midx) * (byX[idx].x - midx) < d) {
                aux[stripCount++] = byX[idx];
            }
        }

        for (i = 0; i < stripCount; i++) {
            for (j = i + 1; j < stripCount && j < i + 8; j++) {
                m.incComparisons();
                double dd = dist2(aux[i], aux[j]);
                if (dd < d) d = dd;
            }
        }
        return d;
    }

    private static double dist2(Point a, Point b) {
        double dx = a.x - b.x, dy = a.y - b.y;
        return dx*dx + dy*dy;
    }
}
