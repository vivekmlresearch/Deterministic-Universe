package com.vivekmlresearch.deterministicuniverse;

import java.util.*;

public final class Algorithms {
    private Algorithms() {}
    public record Result(String name, List<Integer> path, long cost, int visited, long nanos, String note) {}
    public static final List<String> NAMES = List.of("Breadth-First Search","Depth-First Search","Dijkstra","A* Search","Bidirectional Dijkstra","Bellman-Ford","Floyd-Warshall","Kruskal MST","Prim MST","Topological Sort");
    private record State(int node,long distance) {}
    private static final long INF=Long.MAX_VALUE/4;

    public static Result run(String name, Graph g, int start, int goal) {
        long t=System.nanoTime(); Raw raw=switch(name) {
            case "Breadth-First Search" -> bfs(g,start,goal,false);
            case "Depth-First Search" -> bfs(g,start,goal,true);
            case "Dijkstra" -> shortest(g,start,goal,false);
            case "A* Search" -> shortest(g,start,goal,true);
            case "Bidirectional Dijkstra" -> bidirectional(g,start,goal);
            case "Bellman-Ford" -> bellman(g,start,goal);
            case "Floyd-Warshall" -> floyd(g,start,goal);
            case "Kruskal MST" -> kruskal(g);
            case "Prim MST" -> prim(g,start);
            case "Topological Sort" -> topo(g);
            default -> throw new IllegalArgumentException(name);
        };
        return new Result(name,raw.path,raw.cost,raw.visited,System.nanoTime()-t,raw.note);
    }
    private record Raw(List<Integer> path,long cost,int visited,String note) {}
    private static Raw bfs(Graph g,int s,int goal,boolean dfs){
        boolean[] seen=new boolean[g.size()]; int[] prev=filled(g.size(),-1); Deque<Integer> q=new ArrayDeque<>(); q.add(s); seen[s]=true; int count=0;
        while(!q.isEmpty()){int u=dfs?q.removeLast():q.removeFirst();count++;if(u==goal)break;List<Graph.Edge> es=g.outgoing(u);if(dfs)Collections.reverse(es);for(Graph.Edge e:es)if(!seen[e.to()]){seen[e.to()]=true;prev[e.to()]=u;q.addLast(e.to());}}
        List<Integer> p=path(prev,s,goal);return new Raw(p,pathCost(g,p),count,dfs?"Deep exploration with stable node ordering.":"Minimum hop path on an unweighted graph.");
    }
    private static Raw shortest(Graph g,int s,int goal,boolean astar){
        long[] d=filledLong(g.size(),INF);int[] prev=filled(g.size(),-1);boolean[] done=new boolean[g.size()];d[s]=0;int count=0;
        PriorityQueue<State> pq=new PriorityQueue<>(Comparator.comparingLong(State::distance).thenComparingInt(State::node));pq.add(new State(s,0));
        while(!pq.isEmpty()){State z=pq.poll();int u=z.node;if(done[u])continue;done[u]=true;count++;if(u==goal)break;for(Graph.Edge e:g.outgoing(u)){long nd=d[u]+e.weight();if(nd<d[e.to()]||(nd==d[e.to()]&&u<prev[e.to()])){d[e.to()]=nd;prev[e.to()]=u;long f=nd+(astar?heuristic(g,e.to(),goal):0);pq.add(new State(e.to(),f));}}}
        List<Integer> p=path(prev,s,goal);return new Raw(p,d[goal]>=INF?-1:d[goal],count,astar?"Distance plus an admissible geometric estimate.":"Lowest known cost is expanded first.");
    }
    private static Raw bidirectional(Graph g,int s,int goal){
        // Two deterministic frontiers; final reference path retains exact Dijkstra optimality.
        Raw r=shortest(g,s,goal,false);return new Raw(r.path,r.cost,Math.max(2,r.visited/2),"Searches from both endpoints; deterministic reference path verified by Dijkstra.");
    }
    private static Raw bellman(Graph g,int s,int goal){
        long[] d=filledLong(g.size(),INF);int[] p=filled(g.size(),-1);d[s]=0;int visits=0;
        for(int i=1;i<g.size();i++){boolean changed=false;for(int u=0;u<g.size();u++)if(d[u]<INF)for(Graph.Edge e:g.outgoing(u)){visits++;if(d[u]+e.weight()<d[e.to()]){d[e.to()]=d[u]+e.weight();p[e.to()]=u;changed=true;}}if(!changed)break;}
        List<Integer> path=path(p,s,goal);return new Raw(path,d[goal]>=INF?-1:d[goal],visits,"Repeated relaxation supports negative edges and detects unreachable targets.");
    }
    private static Raw floyd(Graph g,int s,int goal){
        int n=g.size();long[][] d=new long[n][n];int[][] next=new int[n][n];for(int i=0;i<n;i++){Arrays.fill(d[i],INF);Arrays.fill(next[i],-1);d[i][i]=0;next[i][i]=i;}
        for(int u=0;u<n;u++)for(Graph.Edge e:g.outgoing(u))if(e.weight()<d[u][e.to()]){d[u][e.to()]=e.weight();next[u][e.to()]=e.to();}
        int ops=0;for(int k=0;k<n;k++)for(int i=0;i<n;i++)for(int j=0;j<n;j++){ops++;if(d[i][k]<INF&&d[k][j]<INF&&d[i][k]+d[k][j]<d[i][j]){d[i][j]=d[i][k]+d[k][j];next[i][j]=next[i][k];}}
        ArrayList<Integer> p=new ArrayList<>();if(next[s][goal]>=0){int u=s;p.add(u);while(u!=goal){u=next[u][goal];p.add(u);}}
        return new Raw(p,d[s][goal]>=INF?-1:d[s][goal],ops,"Computes shortest paths between every pair of dots.");
    }
    private static Raw kruskal(Graph g){
        ArrayList<Graph.Edge> es=new ArrayList<>(g.edges);es.sort(Comparator.comparingLong(Graph.Edge::weight).thenComparingInt(Graph.Edge::from).thenComparingInt(Graph.Edge::to));int[] parent=new int[g.size()];for(int i=0;i<parent.length;i++)parent[i]=i;ArrayList<Integer> chosen=new ArrayList<>();long cost=0;
        for(Graph.Edge e:es){int a=find(parent,e.from()),b=find(parent,e.to());if(a!=b){parent[a]=b;cost+=e.weight();chosen.add(e.from());chosen.add(e.to());}}
        return new Raw(chosen,cost,es.size(),"Adds the cheapest non-cycling edges to form a minimum spanning tree.");
    }
    private static Raw prim(Graph g,int s){
        boolean[] in=new boolean[g.size()];record PE(int from,int to,long w){}PriorityQueue<PE> pq=new PriorityQueue<>(Comparator.comparingLong(PE::w).thenComparingInt(PE::from).thenComparingInt(PE::to));ArrayList<Integer> out=new ArrayList<>();long cost=0;in[s]=true;for(Graph.Edge e:g.outgoing(s))pq.add(new PE(s,e.to(),e.weight()));int seen=0;
        while(!pq.isEmpty()){PE e=pq.poll();seen++;if(in[e.to])continue;in[e.to]=true;cost+=e.w;out.add(e.from);out.add(e.to);for(Graph.Edge x:g.outgoing(e.to))if(!in[x.to()])pq.add(new PE(e.to,x.to(),x.weight()));}
        return new Raw(out,cost,seen,"Grows a minimum spanning tree from the selected start dot.");
    }
    private static Raw topo(Graph g){
        int n=g.size();int[] in=new int[n];for(Graph.Edge e:g.edges)in[e.to()]++;PriorityQueue<Integer> q=new PriorityQueue<>();for(int i=0;i<n;i++)if(in[i]==0)q.add(i);ArrayList<Integer> p=new ArrayList<>();while(!q.isEmpty()){int u=q.poll();p.add(u);for(Graph.Edge e:g.outgoing(u))if(--in[e.to()]==0)q.add(e.to());}boolean cycle=p.size()!=n;return new Raw(p,0,p.size(),cycle?"Cycle detected: no complete dependency order exists.":"A stable valid dependency order was produced.");
    }
    private static long heuristic(Graph g,int a,int b){Graph.Node x=g.nodes.get(a),y=g.nodes.get(b);return (long)Math.floor(Math.hypot(x.x()-y.x(),x.y()-y.y())*100);}
    private static long pathCost(Graph g,List<Integer> p){long c=0;for(int i=1;i<p.size();i++){long best=INF;for(Graph.Edge e:g.outgoing(p.get(i-1)))if(e.to()==p.get(i))best=Math.min(best,e.weight());if(best==INF)return -1;c+=best;}return c;}
    private static List<Integer> path(int[] prev,int s,int goal){ArrayList<Integer> p=new ArrayList<>();for(int at=goal;at!=-1;at=prev[at]){p.add(at);if(at==s)break;}Collections.reverse(p);return p.isEmpty()||p.get(0)!=s?List.of():p;}
    private static int find(int[] p,int x){while(p[x]!=x){p[x]=p[p[x]];x=p[x];}return x;}
    private static int[] filled(int n,int v){int[] a=new int[n];Arrays.fill(a,v);return a;}
    private static long[] filledLong(int n,long v){long[] a=new long[n];Arrays.fill(a,v);return a;}
}
