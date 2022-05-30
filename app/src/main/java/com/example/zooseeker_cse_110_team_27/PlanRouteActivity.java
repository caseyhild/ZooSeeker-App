package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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
    private TextView nextExhibit;
    private Button nextBtn;
    private Button compactBtn;
    private Button skipBtn;
    private Button backBtn;
    private ArrayList<String> goals;
    private ArrayList<ArrayList<String>> shortPaths;
    private ArrayList<ArrayList<String>> originalPaths;

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

        skipBtn = findViewById(R.id.skip_btn);
        skipBtn.setOnClickListener(this::onSkipClicked);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this::onBackClicked);

        nextExhibit = findViewById(R.id.next_exhibit_view);

        shortPaths = new ArrayList<>(); //init shortPaths var

        //create all the goals along the path
        pr = new PlanRoute(this);
        pr.createGoals(goals, start, shortPaths);

        //make it go back to the beginning when done
        ArrayList<String> temp = new ArrayList<>();
        temp.add(shortPaths.get(shortPaths.size() - 1).get(1));
        temp.add("entrance_exit_gate");
        shortPaths.add(temp);
        originalPaths = new ArrayList<>(shortPaths);

        setShortestPath(shortPaths);
        updateNextView();
    }

    private void setShortestPath(ArrayList<ArrayList<String>> shortPaths) {
        //set the textview back to empty
        tv.setText("");

        //rewrite the textview to the shortest path
        tv.setText(pr.setShortestPath(shortPaths));
    }

    private void updateNextView() {
        //update next text button depending on what the next exhibit is
        if(shortPaths.size() > 1) {
            nextExhibit.setText("Next exhibit: " + pr.getNextExhibitName(shortPaths));
        }
        else {
            nextExhibit.setText("No exhibits to skip");
        }
    }

    private void updateNextButtonText() {
        //if shortPaths has only 1 element then set the text of nextBtn to finish
        if (shortPaths.size()<2 && shortPaths.get(0).get(1).equals("entrance_exit_gate")) {
            nextBtn.setText("Finish");
        }
        else {
            nextBtn.setText("Next");
        }
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
        //update next button text when shortPaths has <= 1 element
        updateNextButtonText();

        //otherwise we just change the textview of the activity to shortPaths
        setShortestPath(shortPaths);
        updateNextView();
    }

    private void onBackClicked(View view) {
        //finish activity if at the beginning of the path
        if(shortPaths.size() > originalPaths.size()-2) {
            finish();
            return;
        }
        else {
            //find where we are relative to the original path
            for(int i = 0; i < originalPaths.size(); i++) {
                ArrayList<String> path = originalPaths.get(i);
                if (shortPaths.isEmpty()) {
                    //add the last two paths if we are at the end of the path
                    shortPaths.add(0, originalPaths.get(originalPaths.size()-2));
                    shortPaths.add(1,originalPaths.get(originalPaths.size() - 1));
                    break;
                }
                else if(shortPaths.get(0).equals(path)) {
                    //add the two paths before from original path if we are in the middle of the path
                    shortPaths.add(0, originalPaths.get(i - 2));
                    shortPaths.add(1,originalPaths.get(i - 1));
                    break;
                }
            }
            //update texts and path
            updateNextButtonText();
            setShortestPath(shortPaths);
            updateNextView();
        }
    }

    private void onSkipClicked(View view) {
        ArrayList<String> tmp = new ArrayList<>();
        //ensure there is an exhibit to skip; else do nothing
        if(shortPaths.size() > 1) {
            //create new path that skips next exhibit
            tmp.add(shortPaths.get(0).get(0));
            tmp.add(shortPaths.get(1).get(1));
            //remove exhibit from original path
            for(int i = 0; i < originalPaths.size(); i++) {
                ArrayList<String> path = originalPaths.get(i);
                if(shortPaths.get(0).equals(path)) {
                    originalPaths.remove(i);
                    originalPaths.remove(i);
                    originalPaths.add(i,tmp);
                    break;
                }
            }
            shortPaths.remove(shortPaths.get(0));
            shortPaths.remove(shortPaths.get(0));
            shortPaths.add(0, tmp);

            updateNextButtonText();

            setShortestPath(shortPaths); //update shortest path to exhibit after skipped
            updateNextView();
        }
    }

}