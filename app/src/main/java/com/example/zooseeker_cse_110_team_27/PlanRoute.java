package com.example.zooseeker_cse_110_team_27;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanRoute {
    static void createGoals(Graph<String, IdentifiedWeightedEdge> g, ArrayList<String> goals, String start, String goal, List<List<String>> shortPaths) {
        GraphPath<String, IdentifiedWeightedEdge> shortestPath = null;

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
    }
}
