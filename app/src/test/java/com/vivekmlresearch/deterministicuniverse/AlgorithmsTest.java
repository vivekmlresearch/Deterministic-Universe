package com.vivekmlresearch.deterministicuniverse;
import org.junit.Test;import static org.junit.Assert.*;
public class AlgorithmsTest {
 @Test public void allEnginesAreRepeatable(){Graph g=Graph.demo();for(String n:Algorithms.NAMES){Algorithms.Result a=Algorithms.run(n,g,0,12);for(int i=0;i<100;i++){Algorithms.Result b=Algorithms.run(n,g,0,12);assertEquals(n,a.path(),b.path());assertEquals(n,a.cost(),b.cost());}}}
 @Test public void dijkstraAndAStarAgree(){Graph g=Graph.demo();assertEquals(Algorithms.run("Dijkstra",g,0,12).cost(),Algorithms.run("A* Search",g,0,12).cost());}
}
