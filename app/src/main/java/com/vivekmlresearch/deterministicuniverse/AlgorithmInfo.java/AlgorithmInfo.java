package com.vivekmlresearch.deterministicuniverse;
import java.util.*;
public final class AlgorithmInfo {
 public record Info(String name,String purpose,String time,String space,String optimal,String functional,String pros,String cons,String bestFor) {}
 public static final List<Info> ALL=List.of(
 new Info("Breadth-First Search","Layer-by-layer traversal","O(V + E)","O(V)","Yes for equal costs","Expands every dot at the current hop distance before going deeper.","Complete; shortest hop path","Memory-heavy; ignores weights","Unweighted mazes, social degrees"),
 new Info("Depth-First Search","Deep traversal and reachability","O(V + E)","O(V)","No","Follows one branch until it ends, then backtracks.","Low frontier overhead","May take detours; not shortest","Cycle detection, exploration"),
 new Info("Dijkstra","Non-negative shortest path","O((V + E) log V)","O(V + E)","Yes","Expands the unsettled dot with the smallest known cost.","Exact; dependable","No negative edges; broad search","Routing and network latency"),
 new Info("A* Search","Goal-directed shortest path","O(E) typical; exponential worst","O(V)","Yes with sound heuristic","Combines travelled cost g(n) and estimated remaining cost h(n).","Usually explores less than Dijkstra","Depends on heuristic; memory-heavy","Maps and spatial navigation"),
 new Info("Bidirectional Dijkstra","Two-ended shortest path","O((V + E) log V)","O(V)","Yes","Searches from both endpoints until optimal frontiers meet.","Can greatly reduce search","Complex stopping rule","Point-to-point routing"),
 new Info("Bellman-Ford","Shortest paths with negative edges","O(VE)","O(V)","Yes without negative cycles","Relaxes every edge repeatedly and exposes negative cycles.","Negative weights; simple proof","Much slower than Dijkstra","Arbitrage, distance-vector routing"),
 new Info("Floyd-Warshall","All-pairs shortest paths","O(V³)","O(V²)","Yes without negative cycles","Tests every dot as an intermediate for every pair.","All pairs; negative edges","Expensive for large graphs","Small dense networks"),
 new Info("Kruskal MST","Minimum spanning forest","O(E log E)","O(V + E)","Minimum tree cost","Adds cheapest non-cycling edges across components.","Excellent on sparse graphs","Requires sorting; not a route","Cabling and clustering"),
 new Info("Prim MST","Minimum spanning tree","O(E log V)","O(V + E)","Minimum tree cost","Grows one tree using the cheapest boundary edge.","Strong on dense graphs","Needs connectivity","Grid and topology design"),
 new Info("Topological Sort","DAG dependency ordering","O(V + E)","O(V)","Order may not be unique","Releases zero-indegree tasks; leftovers reveal a cycle.","Linear; detects cycles","Only directed acyclic graphs","Builds and workflows"));
 public static Info get(String n){for(Info i:ALL)if(i.name.equals(n))return i;throw new IllegalArgumentException(n);}private AlgorithmInfo(){}
}
