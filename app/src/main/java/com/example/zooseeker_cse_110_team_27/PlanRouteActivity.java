package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PlanRouteActivity extends AppCompatActivity{
    private TextView tv;
    private Button nextBtn;
    private Button compactBtn;
    private ArrayList<String> goals;
    private ArrayList<ArrayList<String>> shortPaths;

    private PlanRoute pr;


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

        compactBtn = findViewById(R.id.compact_btn);
        compactBtn.setOnClickListener(this::onCompactClicked);

        shortPaths = new ArrayList<>();

        //create all the goals along the path
        pr = new PlanRoute(this);
        pr.createGoals(goals, start, shortPaths);

        //make it go back to the beginning when done
        ArrayList<String> temp = new ArrayList<>();
        temp.add(shortPaths.get(shortPaths.size() - 1).get(1));
        temp.add("entrance_exit_gate");
        shortPaths.add(temp);

        setShortestPath(shortPaths);
    }

    private void setShortestPath(ArrayList<ArrayList<String>> shortPaths) {
        //set the textview back to empty
        tv.setText("");

        //rewrite the textview to the shortest path
        tv.setText(pr.setShortestPath(shortPaths));

    }

    private void onCompactClicked(View view) {
        //send data to the compactlistactivity to use the shortPaths
        Intent i = new Intent(this, CompactListActivity.class);

        i.putExtra("shortPaths", shortPaths);

        startActivity(i);
    }

    private void onNextClicked(View view) {
        //when shortPaths doesnt have any more elements, finish the activity
        if (shortPaths.size() == 0) {
            finish();
            return;
        }

        //if shortPaths has only 1 element then set the text of nextBtn to finish
        if (shortPaths.size() == 1) {
            nextBtn.setText("Finish");
        }

        //otherwise we just change the textview of the activity to shortPaths
        setShortestPath(shortPaths);
    }

}