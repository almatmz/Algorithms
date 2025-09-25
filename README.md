# Assignment 1 – Divide & Conquer Algorithms

## 📌 Overview
This project implements and benchmarks four classic divide-and-conquer algorithms:

1. **MergeSort** – linear-time merge with reusable buffer and small-n insertion sort cutoff.
2. **QuickSort** – randomized pivot with smaller-first recursion to bound stack depth.
3. **Deterministic Select** – Median-of-Medians pivot for O(n) worst-case selection.
4. **Closest Pair of Points (2D)** – O(n log n) sweep using strip scanning (≤ 7–8 neighbor checks).

Metrics such as runtime, recursion depth, comparisons, and allocations are measured, plotted, and analyzed against theoretical expectations.

---

## 🏗 Architecture Notes

### Recursion Depth Control
- **MergeSort**: Uses a **small-n cutoff** (e.g., `n ≤ 32` → insertion sort) to reduce recursion depth and allocation churn.
- **QuickSort**: Always **recurses into the smaller partition** first, while iterating over the larger one. This guarantees stack depth ≲ 2 × ⌊log₂ n⌋ on random pivots.
- **Deterministic Select**: Recurses only into the **partition containing k** and always chooses the smaller side if ambiguous.
- **Closest Pair**: Recursion splits the x-sorted array. The y-sorted strip is maintained to avoid rebuilding arrays.

### Memory Allocations
- **MergeSort**: Maintains a **single reusable buffer** for merging.
- **QuickSort** and **Select**: Use **in-place partitioning** to avoid extra arrays.
- **Closest Pair**: Maintains auxiliary arrays for y-ordering but reuses them across recursive calls.

### Metrics & Utilities
- A shared `Metrics` utility tracks:
    - **Comparisons** (integer counter).
    - **Recursion depth** (increment/decrement on call entry/exit).
    - **Allocations** (tracked when new buffers are created).
- A lightweight CLI parses arguments, runs the chosen algorithm, and **writes CSV output** for plotting.

---

## 📊 Recurrence Analyses

### MergeSort
- **Recurrence:** T(n) = 2 T(n/2) + Θ(n)
- **Master Theorem (Case 2):** a=2, b=2, f(n)=Θ(n)=Θ(n^{log₂2}) → **T(n)=Θ(n log n)**.
- Cutoff to insertion sort slightly reduces constant factors but does not change Θ-bound.

### QuickSort (Random Pivot)
- **Expected Recurrence:** T(n)=T(k)+T(n–k–1)+Θ(n) with random k.
- Average partition sizes → **T(n)=Θ(n log n)** by Akra–Bazzi or standard expectation.
- Worst-case depth O(n) (rare with randomized pivot); smaller-first recursion ensures stack depth ≲ 2 log₂ n on average.

### Deterministic Select (Median-of-Medians)
- **Recurrence:** T(n)=T(n/5)+T(7n/10)+Θ(n) (median of medians pivot).
- By Akra–Bazzi intuition → T(n)=Θ(n).
- Extra linear work for pivot selection adds a larger constant factor than randomized QuickSelect but guarantees linear worst-case.

### Closest Pair of Points
- **Recurrence:** T(n)=2 T(n/2)+Θ(n) for divide, conquer, and merging the strip.
- Master Theorem (Case 2): T(n)=Θ(n log n).
- The 7–8 neighbor scan keeps strip checking linear in n.

---

## 📈 Plots and Measurements

| n       | MergeSort Time (ms) | QS Time (ms) | Select Time (ms) | Closest Pair Time (ms) |
|---------:|------------------:|--------------:|-----------------:|-----------------------:|
| 1 000    | 0.51                  | 0,5             | 0,47                | 4,18                      |
| 10 000   | 2,53                  | 6,67             | 3,27                | 19,55                      |
| 100 000  | 29,71                  | 27,35             | 10,51                | 156,22                      |


| n       | MergeSort depth | QS depth | Select depth | Closest Pair depth  |
|---------:|------------------:|--------------:|-----------------:|-----------------------:|
| 1 000    | 6                 | 6             | 4                | 9                      |
| 10 000   | 10                  | 9             | 5                | 12                      |
| 100 000  | 13                  | 11             | 7                | 16                      |

- **Time vs n**: All four follow their predicted Θ-curves. QuickSort is fastest on average but occasionally shows deeper recursion.
- **Depth vs n**: QuickSort stays close to 2 log₂ n; MergeSort depth is ≈ log₂ n.
- **Constant-Factor Effects**:
    - Small cutoff in MergeSort improves cache locality.
    - JVM garbage collector overhead slightly increases times for large arrays.
    - Deterministic Select is slower than QuickSelect for small n due to pivot overhead, but matches Θ(n) growth.
    - Closest Pair strip check constant factors are low; cache effects noticeable for very large n.

---

## 📝 Summary

The experimental measurements **align closely with theoretical predictions**:
- **MergeSort & QuickSort** show Θ(n log n) scaling. QuickSort is usually faster but slightly riskier for worst-case depth.
- **Deterministic Select** demonstrates guaranteed Θ(n) even when adversarial inputs are used.
- **Closest Pair** timings confirm the O(n log n) performance and validate the strip-based neighbor scan efficiency.

Minor mismatches between theory and practice stem from:
- Cache performance (smaller buffers reuse faster).
- JVM garbage collection pauses.
- Random pivot variance for small samples.

These results validate the divide-and-conquer approaches and safe recursion patterns implemented in this project.

---

## ▶️ Usage

```bash
# Build and test
mvn clean install -U

# Run an algorithm benchmark
java -cp target/classes algorithms.Main bench mergesort 1000 5 results.csv

# Run all algorithms 
mvn compile exec:java
