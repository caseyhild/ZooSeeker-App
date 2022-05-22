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

        pr = new PlanRoute(this);
        pr.createGoals(goals, start, shortPaths);

        setShortestPath(shortPaths);
    }

    private void setShortestPath(ArrayList<ArrayList<String>> shortPaths) {
        tv.setText("");

        tv.setText(pr.setShortestPath(shortPaths));

    }

    private void onCompactClicked(View view) {
        Intent i = new Intent(this, CompactListActivity.class);

        i.putExtra("shortPaths", shortPaths);

        startActivity(i);
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