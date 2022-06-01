package com.example.zooseeker_cse_110_team_27;

import android.content.Context;
import android.util.Log;

import com.example.zooseeker_cse_110_team_27.location.Coord;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanRoute {
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> g;

    private double DEG_LAT_IN_FT = 363843.57;
    private double DEG_LNG_IN_FT = 307515.50;

    private Context context;

    public PlanRoute(Context context) {
        //get the context from the activity your using
        this.context = context;

        //load the necessary data from JSON
        g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        vInfo = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(context,"sample_edge_info.json");
    }

    ArrayList<ArrayList<String>> createGoals(ArrayList<String> goals, String start) {
        GraphPath<String, IdentifiedWeightedEdge> shortestPath = null;
        String goal = "";

        ArrayList<String> newGoals = new ArrayList<String>(goals);
        ArrayList<ArrayList<String>> shortPaths = new ArrayList<>();

        while (!newGoals.isEmpty()) {
            for (String goale : newGoals) {
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
            newGoals.remove(goal);
        }

        Log.d("plan_route", "make goals, apply dijkstra, find short path");
        return shortPaths;
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

        Log.d("plan_route", "make the compact list");
        return text;
    }

    String setShortestPath(ArrayList<ArrayList<String>> shortPaths, ArrayList<String> goals, boolean check) {
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

        //check if the user is clicking this function when AFTER viewing an exhibit
        if (check == true) {
            goals.remove(path.getStartVertex());
        }

        shortPaths.remove(0);

        Log.d("plan_route", "find the shortest path");
        return ap;
    }

    public String getNextExhibitName(ArrayList<ArrayList<String>> shortPaths) {
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g,
                shortPaths.get(1).get(0),
                shortPaths.get(1).get(1));
        Log.d("plan_route", "get exhibit name");
        return vInfo.get(path.getStartVertex()).name;
    }
  
    //get the current coord of the user
    //get the list of the coords of all exhibits
    //get the users current goals
    //go through the goals list and find the closest goal to the user and calculate whatever the distance is
    //set the closest goal to be the new start
    //run createGoals method with the new start and the same goals again
    //return the new start location
    String offRouteLocator(Coord p1, HashMap<String, Coord> coords, ArrayList<String> goals) {
        double weight = Integer.MAX_VALUE;
        String tempStr = "";
        for (String goal : goals) {
            Coord p2 = coords.get(goal);

            double tempWeight = getTempWeight(p1, p2);

            if (tempWeight < weight) {
                tempStr = goal;
                weight = tempWeight;
            }
        }

        Log.d("plan_route", "off route");
        return tempStr;
    }

    double getTempWeight(Coord p1, Coord p2) {
        int BASE = 100;

        double d_lat = Math.abs(p1.lat - p2.lat);
        double d_lng = Math.abs(p1.lng - p2.lng);

        double d_ft_v = d_lat * DEG_LAT_IN_FT;
        double d_ft_h = d_lng * DEG_LNG_IN_FT;

        double d_ft = Math.sqrt(Math.pow(d_ft_h,2) + Math.pow(d_ft_v,2));
        double tempWeight = BASE * Math.ceil(d_ft/ BASE);

        Log.d("plan_route", "calculate distance for coordinate points");
        return tempWeight;
    }

}
