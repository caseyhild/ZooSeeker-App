package com.example.zooseeker_cse_110_team_27;

import android.content.Context;
import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlanRoute {
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> g;

    private Context context;

    public PlanRoute(Context context) {
        //get the context from the activity your using
        this.context = context;

        //load the necessary data from JSON
        g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        vInfo = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(context,"sample_edge_info.json");
    }

    void createGoals(ArrayList<String> goals, String start, ArrayList<ArrayList<String>> shortPaths) {
        GraphPath<String, IdentifiedWeightedEdge> shortestPath = null;
        String goal = "";

        while (!goals.isEmpty()) {
            for (String goale : goals) {
                GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goale);

                if (shortestPath == null || path.getWeight() < shortestPath.getWeight()) {
                    shortestPath = path;
                    goal = goale;
                }
            }

            shortestPath = null;
            ArrayList<String> temp = new ArrayList<>();
            temp.add(start);
            temp.add(goal);

            shortPaths.add(temp);
            start = goal;
            goals.remove(goal);
        }


        Log.d("TAG", shortPaths.toString());
    }

    String setCompactList(ArrayList<ArrayList<String>> shortPaths) {
        String text = "";
        int totalWeight = 0;
        while (!shortPaths.isEmpty()) {
            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g,
                    shortPaths.get(0).get(0),
                    shortPaths.get(0).get(1));

            shortPaths.remove(0);

            totalWeight += path.getWeight();

            text += (vInfo.get(path.getStartVertex()).name + " to "
                    + vInfo.get(path.getEndVertex()).name + " is: " + totalWeight + " meters.\n\n");
        }

        return text;
    }

    String setShortestPath(ArrayList<ArrayList<String>> shortPaths) {
        String ap = new String();

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g,
                shortPaths.get(0).get(0),
                shortPaths.get(0).get(1));

        ap += ("The shortest path from " + vInfo.get(path.getStartVertex()).name + " to "
                + vInfo.get(path.getEndVertex()).name + " is: " + path.getWeight() + " meters.\n\n");

        int i = 1;
        String currExhibit = shortPaths.get(0).get(0);
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            ZooData.VertexInfo edgeSource = vInfo.get(g.getEdgeSource(e));
            ZooData.VertexInfo edgeTarget = vInfo.get(g.getEdgeTarget(e));

            ap += (i + ". Walk " + g.getEdgeWeight(e) + " meters along " + eInfo.get(e.getId()).street + " from ");

            if (currExhibit.equals(edgeSource.id)) {
                ap += (edgeSource.name + " to " + edgeTarget.name + ".\n\n");
                currExhibit = edgeTarget.id;
            } else {
                ap += (edgeTarget.name + " to " + edgeSource.name + ".\n\n");
                currExhibit = edgeSource.id;
            }
            i++;
        }

        shortPaths.remove(0);

        return ap;
    }

}
