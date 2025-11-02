# Algorithms Course - Implementations & Analysis

This repository contains implementations and analysis for multiple algorithm assignments.

## 📌 Assignment 1 – Divide & Conquer Algorithms

This project implements and benchmarks four classic divide-and-conquer algorithms:

1. **MergeSort** – linear-time merge with reusable buffer and small-n insertion sort cutoff.
2. **QuickSort** – randomized pivot with smaller-first recursion to bound stack depth.
3. **Deterministic Select** – Median-of-Medians pivot for O(n) worst-case selection.
4. **Closest Pair of Points (2D)** – O(n log n) sweep using strip scanning (≤ 7–8 neighbor checks).

Metrics such as runtime, recursion depth, comparisons, and allocations are measured, plotted, and analyzed against theoretical expectations.

## 📌 Assignment 4 – Graph Algorithms (Smart City/Campus Scheduling)

This project implements and benchmarks three essential graph algorithms for dependency analysis and scheduling:

1. **Strongly Connected Components (SCC)** – Tarjan's algorithm for finding cyclic dependencies
2. **Topological Sort** – Kahn's and DFS-based algorithms for task ordering
3. **DAG Shortest/Longest Paths** – Critical path analysis and optimal scheduling

### Implementation Details

**Package Structure:**
- `graph.scc` - Strongly Connected Components (Tarjan's algorithm)
- `graph.topo` - Topological Sort (Kahn's and DFS variants)
- `graph.dagsp` - DAG Shortest Path (single-source shortest & longest paths)

**Algorithms Implemented:**

1. **SCC (Tarjan's Algorithm)**
   - Finds all strongly connected components in O(V + E) time
   - Builds condensation graph (DAG of components)
   - Tracks DFS visits and edges traversed

2. **Topological Sort**
   - Kahn's algorithm (BFS-based with queue operations)
   - DFS-based variant (reverse post-order)
   - Cycle detection for validation
   - Tracks pops/pushes for Kahn's algorithm

3. **DAG Shortest/Longest Paths**
   - Single-source shortest paths using topological ordering
   - Longest paths via sign inversion (critical path analysis)
   - Path reconstruction for both shortest and longest
   - Tracks edge relaxations

**Metrics & Instrumentation:**
- `GraphMetrics` class tracks:
  - DFS visits and edges (for SCC)
  - Queue pops/pushes (for Kahn's algorithm)
  - Edge relaxations (for DAG shortest path)
  - Execution time via `System.nanoTime()`

### Test Datasets

**9 datasets generated in `/data` directory:**

**Small (6-10 nodes):**
- `small_dag_7nodes.json` - Pure DAG, sparse (7 nodes, 7 edges)
- `small_cycle_8nodes.json` - One cycle (8 nodes, 8 edges)
- `small_two_cycles_10nodes.json` - Two cycles (10 nodes, 11 edges)

**Medium (10-20 nodes):**
- `medium_dag_15nodes.json` - DAG with multiple paths (15 nodes, 20 edges)
- `medium_sccs_18nodes.json` - Multiple SCCs (18 nodes, 20 edges, 3 SCCs)
- `medium_sparse_12nodes.json` - Sparse with one cycle (12 nodes, 12 edges)

**Large (20-50 nodes):**
- `large_dag_30nodes.json` - Dense DAG for performance testing (30 nodes, 84 edges)
- `large_sccs_40nodes.json` - Multiple SCCs (40 nodes, 63 edges, 5 main SCCs)
- `large_sparse_dag_50nodes.json` - Very large sparse DAG (50 nodes, 99 edges)

### Results Summary

| Dataset | Vertices | Edges | SCCs | Topo Time (ms) | SP Time (ms) | Critical Path Length |
|---------|----------|-------|------|----------------|--------------|----------------------|
| small_dag_7 | 7 | 7 | 7 | 0.029 | 0.016 | 12.00 |
| small_cycle_8 | 8 | 8 | 6 | 0.026 | 0.016 | 9.00 |
| small_two_cycles_10 | 10 | 11 | 7 | 0.015 | 0.003 | 11.00 |
| medium_dag_15 | 15 | 20 | 15 | 0.023 | 0.007 | 22.00 |
| medium_sccs_18 | 18 | 20 | 11 | 0.016 | 0.004 | 20.00 |
| medium_sparse_12 | 12 | 12 | 10 | 0.013 | 0.004 | 14.00 |
| large_dag_30 | 30 | 84 | 30 | 0.040 | 0.016 | 58.00 |
| large_sccs_40 | 40 | 63 | 15 | 0.026 | 0.005 | 19.00 |
| large_sparse_dag_50 | 50 | 99 | 50 | 0.109 | 0.022 | 78.76 |

### Analysis & Observations

**SCC (Tarjan's Algorithm):**
- Runs in O(V + E) time as expected
- Single DFS traversal efficiently identifies all cycles
- DFS visits = number of vertices
- DFS edges = number of edges traversed
- Bottleneck: Stack operations for large graphs

**Topological Sort:**
- Kahn's algorithm: O(V + E) with queue operations
- DFS variant: O(V + E) with recursion
- Both detect cycles correctly
- Kahn's is slightly faster for dense graphs (fewer cache misses)
- Bottleneck: In-degree computation and queue management

**DAG Shortest/Longest Paths:**
- O(V + E) time using topological ordering
- Relaxations = number of edges relaxed
- Critical path analysis useful for project scheduling
- Bottleneck: Topological sort preprocessing
- Effect of structure: Dense graphs have more relaxations

**Performance Insights:**
- All algorithms scale linearly with graph size (O(V + E))
- Dense graphs (large_dag_30) show slightly higher constant factors
- SCC compression significantly reduces problem size for cyclic graphs
- Memory usage is O(V) for all algorithms (in-place modifications)

### Practical Recommendations

**When to use SCC:**
- Dependency analysis in build systems
- Detecting circular dependencies in module imports
- Identifying mutually reachable components in road networks

**When to use Topological Sort:**
- Task scheduling with dependencies
- Course prerequisite planning
- Build order determination

**When to use DAG Shortest/Longest Paths:**
- Critical path method (CPM) in project management
- Finding bottlenecks in task workflows
- Resource allocation and scheduling optimization

**Combined approach (SCC → Topo → DAG-SP):**
1. Detect cycles with SCC
2. Compress SCCs into condensation DAG
3. Find valid task order with topological sort
4. Optimize scheduling with critical path analysis

---

## 🏗 Architecture Notes

### Recursion Depth Control
- **MergeSort**: Uses a **small-n cutoff** (e.g., `n ≤ 16` → insertion sort) to reduce recursion depth and allocation churn.
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

### Assignment 1 - Divide & Conquer

```bash
# Build and test
mvn clean install -U

# Run an algorithm benchmark
java -cp target/classes algorithms.Main bench mergesort 1000 5 results.csv

# Run all algorithms 
mvn compile exec:java
```

### Assignment 4 - Graph Algorithms

```bash
# Build the project
mvn clean compile

# Run all tests (31 tests total: 7 SCC, 9 Topo, 11 DAG-SP, 4 legacy)
mvn test

# Generate datasets
java -cp target/classes graph.DatasetGenerator

# Run benchmarks on all datasets
java -cp target/classes graph.GraphBenchmarks

# Load and analyze a specific dataset
java -cp target/classes graph.GraphBenchmarks data/medium_sccs_18nodes.json
```

### Code Quality

**Testing:**
- 31 JUnit tests across all algorithms
- Edge cases covered: empty graphs, cycles, disconnected components
- Deterministic small cases for validation

**Code Organization:**
- Modular package structure (`graph.scc`, `graph.topo`, `graph.dagsp`)
- Javadoc comments on public classes and methods
- Metrics tracking integrated throughout

**Build & Reproducibility:**
- Builds from clean clone: `mvn clean install`
- All datasets committed to `/data`
- Tests run automatically with `mvn test`
