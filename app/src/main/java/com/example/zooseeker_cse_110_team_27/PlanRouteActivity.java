package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
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
        goals = (ArrayList<String>) getIntent().getSerializableExtra("key");

        tv = findViewById(R.id.directions_textView);

        nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this::onNextClicked);

        // 1. Load the graph...
        String goal = "";
        //tv.setText("");
        g = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");
        // 2. Load the information about our nodes and edges...
        vInfo = ZooData.loadVertexInfoJSON(this,"sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");

        shortPaths = new ArrayList<>();

        PlanRoute.createGoals(g, goals, start, goal, shortPaths);

        setShortestPath(shortPaths);
    }

    private void setShortestPath(List<List<String>> shortPaths) {
        tv.setText("");

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g,
                shortPaths.get(0).get(0),
                shortPaths.get(0).get(1));

        tv.append("The shortest path from " + vInfo.get(path.getStartVertex()).name + " to "
                + vInfo.get(path.getEndVertex()).name + " is: " + path.getWeight() + " meters.\n\n");

        int i = 1;
        String currExhibit = shortPaths.get(0).get(0);
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            ZooData.VertexInfo edgeSource = vInfo.get(g.getEdgeSource(e));
            ZooData.VertexInfo edgeTarget = vInfo.get(g.getEdgeTarget(e));

            tv.append(i + ". Walk " + g.getEdgeWeight(e) + " meters along " + eInfo.get(e.getId()).street + " from ");

            if (currExhibit.equals(edgeSource.id)) {
                tv.append(edgeSource.name + " to " + edgeTarget.name + ".\n\n");
                currExhibit = edgeTarget.id;
            } else {
                tv.append(edgeTarget.name + " to " + edgeSource.name + ".\n\n");
                currExhibit = edgeSource.id;
            }
            i++;
        }

        shortPaths.remove(0);
    }

    private void onNextClicked(View view) {
        if (shortPaths.size() == 0) {
            finish();
            return;
        }

        if (shortPaths.size() == 1) {
            nextBtn.setText("Finish");
        }

        setShortestPath(shortPaths);
    }
}