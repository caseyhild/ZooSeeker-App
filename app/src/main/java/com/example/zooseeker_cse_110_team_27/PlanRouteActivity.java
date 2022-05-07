package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlanRouteActivity extends AppCompatActivity {
    private TextView tv;
    private Button nextBtn;
    private ArrayList<String> goals;
    private List<List<String>> shortPaths;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_route);

        // "source" and "sink" are graph terms for the start and end
        String start = "entrance_exit_gate";
        goals = new ArrayList<>(Arrays.asList("elephant_odyssey", "gorillas", "lions"));

        tv = findViewById(R.id.directions_textView);

        nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this::onNextCicked);

        // 1. Load the graph...
        String goal = "";
        tv.setText("");
        g = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");
        GraphPath<String, IdentifiedWeightedEdge> shortestPath = null;

        shortPaths = new ArrayList<>();

        while (!goals.isEmpty()) {
            for (String goale : goals) {
                GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goale);

                if (shortestPath == null || path.getWeight() < shortestPath.getWeight()) {
                    shortestPath = path;
                    goal = goale;
                }
            }

            shortestPath = null;

            shortPaths.add(Arrays.asList(start, goal));
            start = goal;
            goals.remove(goal);
        }

        System.out.println(shortPaths.toString());

        // 2. Load the information about our nodes and edges...
        vInfo = ZooData.loadVertexInfoJSON(this,"sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g,
                shortPaths.get(0).get(0),
                shortPaths.get(0).get(1));


        tv.append("The shortest path from " + shortPaths.get(0).get(0) + " to " + shortPaths.get(0).get(1)
                + " is: " + path.getWeight() + " meters.\n\n");

        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {

            tv.append(i + ". Walk " + g.getEdgeWeight(e) + " meters along " + eInfo.get(e.getId()).street +
                    " from " + vInfo.get(g.getEdgeSource(e).toString()).name + " to " +
                    vInfo.get(g.getEdgeTarget(e).toString()).name + ".\n\n");
            i++;
        }

        shortPaths.remove(0);
    }

    private void onNextCicked(View view) {
        tv.setText("");

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g,
                shortPaths.get(0).get(0),
                shortPaths.get(0).get(1));


        tv.append("The shortest path from " + shortPaths.get(0).get(0) + " to " + shortPaths.get(0).get(1)
                + " is: " + path.getWeight() + " meters.\n\n");

        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {

            tv.append(i + ". Walk " + g.getEdgeWeight(e) + " meters along " + eInfo.get(e.getId()).street +
                    " from " + vInfo.get(g.getEdgeSource(e).toString()).name + " to " +
                    vInfo.get(g.getEdgeTarget(e).toString()).name + ".\n\n");
            i++;
        }

        shortPaths.remove(0);

    }
}