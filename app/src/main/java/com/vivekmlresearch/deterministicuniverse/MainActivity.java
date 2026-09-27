package com.vivekmlresearch.deterministicuniverse;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.*;import android.graphics.drawable.*;import android.view.*;import android.widget.*;import java.util.*;

public final class MainActivity extends Activity {
    private Graph graph; private UniverseView universe; private TextView result; private Spinner selector; private int start=0,goal=12;
    private final int cyan=Color.rgb(82,229,255), navy=Color.rgb(7,16,31), panel=Color.rgb(15,31,55);
    @Override public void onCreate(Bundle b){super.onCreate(b);graph=Graph.demo();buildUi();}
    private TextView text(String s,int sp){TextView v=new TextView(this);v.setText(s);v.setTextColor(Color.WHITE);v.setTextSize(sp);v.setPadding(dp(16),dp(8),dp(16),dp(8));return v;}
    private void buildUi(){
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(navy);
        TextView title=text("DETERMINISTIC UNIVERSE",22);title.setTextColor(cyan);title.setTypeface(Typeface.DEFAULT_BOLD);root.addView(title,new LinearLayout.LayoutParams(-1,-2));
        TextView subtitle=text("Algorithm Lab  •  Same input. Same result.",13);subtitle.setTextColor(Color.LTGRAY);root.addView(subtitle);
        universe=new UniverseView(this);root.addView(universe,new LinearLayout.LayoutParams(-1,0,1));
        selector=new Spinner(this);ArrayAdapter<String>a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,Algorithms.NAMES);selector.setAdapter(a);root.addView(selector,new LinearLayout.LayoutParams(-1,dp(52)));
        LinearLayout actions=new LinearLayout(this);actions.setPadding(dp(10),0,dp(10),0);Button run=button("RUN LAB");Button all=button("BENCHMARK ×10");actions.addView(run,new LinearLayout.LayoutParams(0,dp(52),1));actions.addView(all,new LinearLayout.LayoutParams(0,dp(52),1));root.addView(actions);
        result=text("Tap RUN LAB to explore the selected algorithm.\nStart: 0  •  Goal: 12  •  Fixed seed: 20260927",14);result.setBackgroundColor(panel);result.setMinHeight(dp(92));root.addView(result);
        run.setOnClickListener(v->runOne());all.setOnClickListener(v->benchmark());setContentView(root);
    }
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setTextColor(navy);b.setTypeface(Typeface.DEFAULT_BOLD);GradientDrawable g=new GradientDrawable();g.setColor(cyan);g.setCornerRadius(dp(12));g.setStroke(dp(4),navy);b.setBackground(g);return b;}
    private void runOne(){String name=(String)selector.getSelectedItem();Algorithms.Result r=Algorithms.run(name,graph,start,goal);universe.path=r.path();universe.invalidate();result.setText(name+"\nCost: "+r.cost()+"  •  Explored: "+r.visited()+"  •  Engine: "+String.format(Locale.US,"%.3f ms",r.nanos()/1e6)+"\n"+r.note());}
    private void benchmark(){result.setText("Running controlled benchmark…");new Thread(()->{StringBuilder s=new StringBuilder("10 deterministic engines • median of 10 runs\n");for(String n:Algorithms.NAMES){long[] t=new long[10];Algorithms.Result last=null;for(int warm=0;warm<3;warm++)Algorithms.run(n,graph,start,goal);for(int i=0;i<10;i++){last=Algorithms.run(n,graph,start,goal);t[i]=last.nanos();}Arrays.sort(t);s.append(String.format(Locale.US,"%-22s %7.3f ms  cost %d\n",shortName(n),t[5]/1e6,last.cost()));}runOnUiThread(()->result.setText(s.toString()));}).start();}
    private String shortName(String n){return n.length()>20?n.substring(0,20):n;}
    private int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}
    final class UniverseView extends View {
        Paint p=new Paint(3);List<Integer>path=List.of();UniverseView(Context c){super(c);p.setTypeface(Typeface.DEFAULT_BOLD);setContentDescription("Interactive graph universe with 24 dots");}
        protected void onDraw(Canvas c){super.onDraw(c);c.drawColor(navy);float w=getWidth(),h=getHeight();p.setStrokeWidth(dp(1));p.setColor(Color.rgb(43,73,107));for(Graph.Edge e:graph.edges){Graph.Node a=graph.nodes.get(e.from()),b=graph.nodes.get(e.to());c.drawLine(a.x()*w,a.y()*h,b.x()*w,b.y()*h,p);}
            p.setStrokeWidth(dp(5));p.setColor(Color.YELLOW);for(int i=1;i<path.size();i++){Graph.Node a=graph.nodes.get(path.get(i-1)),b=graph.nodes.get(path.get(i));c.drawLine(a.x()*w,a.y()*h,b.x()*w,b.y()*h,p);}for(Graph.Node n:graph.nodes){p.setColor(n.id()==start?Color.GREEN:n.id()==goal?Color.YELLOW:path.contains(n.id())?cyan:Color.rgb(155,104,255));c.drawCircle(n.x()*w,n.y()*h,dp(7),p);p.setTextSize(dp(9));p.setColor(Color.WHITE);c.drawText(String.valueOf(n.id()),n.x()*w+dp(8),n.y()*h-dp(6),p);}}
    }
}
