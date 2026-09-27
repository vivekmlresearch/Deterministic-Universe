package com.vivekmlresearch.deterministicuniverse;

import java.util.*;

public final class Graph {
    public record Node(int id, float x, float y) {}
    public record Edge(int from, int to, long weight) {}
    public final List<Node> nodes;
    public final List<Edge> edges;
    public final boolean directed;

    public Graph(List<Node> nodes, List<Edge> edges, boolean directed) {
        this.nodes = List.copyOf(nodes); this.edges = List.copyOf(edges); this.directed = directed;
    }
    public int size() { return nodes.size(); }
    public List<Edge> outgoing(int id) {
        ArrayList<Edge> out = new ArrayList<>();
        for (Edge e : edges) {
            if (e.from == id) out.add(e);
            if (!directed && e.to == id) out.add(new Edge(id, e.from, e.weight));
        }
        out.sort(Comparator.comparingInt(Edge::to).thenComparingLong(Edge::weight));
        return out;
    }
    public static Graph demo() {
        Random r = new Random(20260927L); ArrayList<Node> n = new ArrayList<>();
        for (int i=0;i<24;i++) n.add(new Node(i, .08f+r.nextFloat()*.84f, .1f+r.nextFloat()*.78f));
        ArrayList<Edge> e = new ArrayList<>();
        for (int i=0;i<24;i++) {
            add(n,e,i,(i+1)%24); add(n,e,i,(i+5)%24);
            if (i%3==0) add(n,e,i,(i+9)%24);
        }
        return new Graph(n,e,false);
    }
    private static void add(List<Node> n,List<Edge> e,int a,int b) {
        Node x=n.get(a),y=n.get(b); long w=Math.max(1,Math.round(Math.hypot(x.x-y.x,x.y-y.y)*100));
        Edge z=new Edge(a,b,w); for(Edge q:e) if((q.from==a&&q.to==b)||(q.from==b&&q.to==a)) return; e.add(z);
    }
}
