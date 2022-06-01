package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zooseeker_cse_110_team_27.location.Coord;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanRouteActivity extends AppCompatActivity{
    private TextView tv;
    private Button nextBtn;
    private Button clearBtn;
    private Button compactBtn;
    private Button skipBtn;
    private Button backBtn;
    private ArrayList<String> goals;
    private ArrayList<String> originalGoals;
    private ArrayList<ArrayList<String>> shortPaths;
    private ArrayList<ArrayList<String>> originalPaths;
    private EditText lat_et;
    private EditText lng_et;
    private Button relocateBtn;
    private HashMap<String, Coord> coords;
    private CheckBox brief_directions;
    private boolean isBrief;
    private String currentLocation;

    private PlanRoute pr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_route);

        // "source" and "sink" are graph terms for the start and end
        String start = "entrance_exit_gate";
        goals = (ArrayList<String>) getIntent().getSerializableExtra("key");
        originalGoals = new ArrayList<>(goals);
        coords = (HashMap<String, Coord>) getIntent().getSerializableExtra("coords");

        currentLocation = "entrance_exit_gate";

        isBrief = false;
        brief_directions = findViewById(R.id.brief_directions);
        brief_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBriefDirectionsChecked();
                Log.d("brief", "Shortest path: " + shortPaths.toString());
                tv.setText(pr.setShortestPath(shortPaths, goals, false, isBrief));
                Log.d("brief", "Shortest path: " + shortPaths.toString());
                Log.d("brief", "Brief Directions = " + isBrief);
            }
        });

        lat_et = findViewById(R.id.lat_et);
        lng_et = findViewById(R.id.lng_et);

        tv = findViewById(R.id.directions_textView);
        tv.setMovementMethod(new ScrollingMovementMethod());

        relocateBtn = findViewById(R.id.relocate_btn);
        relocateBtn.setOnClickListener(this::onRelocateClicked);

        nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this::onNextClicked);


        clearBtn = findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this::onClearClicked);

        compactBtn = findViewById(R.id.compact_btn);
        compactBtn.setOnClickListener(this::onCompactClicked);

        skipBtn = findViewById(R.id.skip_btn);
        skipBtn.setOnClickListener(this::onSkipClicked);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this::onBackClicked);

        shortPaths = new ArrayList<>(); //init shortPaths var

        //create all the goals along the path
        pr = new PlanRoute(this);
        shortPaths = pr.createGoals(goals, start);

        //make it go back to the beginning when done
        ArrayList<String> temp = new ArrayList<>();
        temp.add(shortPaths.get(shortPaths.size() - 1).get(1));
        temp.add("entrance_exit_gate");
        shortPaths.add(temp);
        originalPaths = new ArrayList<>(shortPaths);

        tv.setText("");
        //this one is false since we don't want to remove the exhibit from the list just yet when we create the activity
        //when we created the activity, the user has NOT visited the next exhibit yet
        //so we don't remove it from the list yet
        //only when we click next on the next exhibit, would we remove it
        //so test case: entrance->flamingos, then click next, flamingos->monkeys, remove flamingos from the list
        //goals: [flamingos, monkeys] -> [monkeys]
        tv.setText(pr.setShortestPath(shortPaths, goals, false, isBrief));
    }

    //if the user wants to relocate, it will first relocate
    //then if the exhibit is way further than it actually is, the button name will change to Relocate?
    //when this happens, if the user press relocate? then it will change the exhibits to match the shortest path from
    //the user's location
    private void onRelocateClicked(View view) {
        String lat = lat_et.getText().toString();
        String lng = lng_et.getText().toString();

        if (lat.equals("") || lng.equals("")) {
            return;
        }

        Coord p1 = new Coord(Double.parseDouble(lat), Double.parseDouble(lng));

        if (relocateBtn.getText().toString() == "Relocate?") {
            //take current pos of the user and the current
            String newStart = pr.offRouteLocator(p1, coords, goals);

            ArrayList<String> newGoals = new ArrayList<>(goals);
            newGoals.remove(newStart);
            shortPaths = pr.createGoals(newGoals, newStart);

            ArrayList<String> temp = new ArrayList<>();
            temp.add(shortPaths.get(shortPaths.size() - 1).get(1));
            temp.add("entrance_exit_gate");
            shortPaths.add(temp);


            Log.d("debuggg",shortPaths.toString());
            tv.setText(pr.setShortestPath(shortPaths, goals, true, isBrief));
            Log.d("debuggg",shortPaths.toString());
            relocateBtn.setText("Change Location");
        } else if (pr.getTempWeight(p1, coords.get(shortPaths.get(0).get(0))) > 500) {
            relocateBtn.setText("Relocate?");
            return;
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

    private void onClearClicked(View view) {
        //clear all selected exhibits and return to that page
        Intent i = new Intent(this, SearchListActivity.class);

        SearchListActivity.clearList = true;

        startActivity(i);
    }

    private void onCompactClicked(View view) {
        //send data to the compactlistactivity to use the shortPaths
        Intent i = new Intent(this, CompactListActivity.class);

        i.putExtra("shortPaths", shortPaths);
        i.putExtra("currLoc", currentLocation);

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
        tv.setText("");
      
        //send shortgoals, goals, and check if the user clicked next exhibit in this function
        //if they did, that means they 'visited' the exhibit
        //so if it was entrance->gorillas
        //click this function, gorillas would be removed from the exhibit list
        //otherwise, if the first thing they do is relocate
        //then the list would remain the same
        //so entrance->gorillas
        //relocate
        //the user would now be at gorillas
        //they would then go to crocidiles next, and remove gorillas from the list
        currentLocation = shortPaths.get(0).get(0);
        tv.setText(pr.setShortestPath(shortPaths, goals, true, isBrief));
    }

    private void onBackClicked(View view) {
        //finish activity if at the beginning of the path
        String start = "entrance_exit_gate";
        if(!shortPaths.isEmpty()) {
            for (int i = 0; i < originalPaths.size(); i++) {
                if (shortPaths.get(0).equals(originalPaths.get(i))) {
                    start = originalPaths.get(i - 1).get(0);
                }
            }
        }
        else {
            start = originalPaths.get(originalPaths.size()-1).get(0);
        }
        if(start.equals("entrance_exit_gate")) {
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
            tv.setText(pr.setShortestPath(shortPaths, goals, false, isBrief));
        }
    }

    private void onSkipClicked(View view) {
        ArrayList<String> tmp = new ArrayList<>();
        //ensure there is an exhibit to skip; else do nothing

        String start = "entrance_exit_gate";
        if(!shortPaths.isEmpty()) {
            for (int i = 0; i < originalPaths.size(); i++) {
                if (shortPaths.get(0).equals(originalPaths.get(i))) {
                    start = originalPaths.get(i - 1).get(0);
                }
            }
            Log.d("100t","START: "+start);

            goals.remove(shortPaths.get(0).get(0));
            originalGoals.remove(shortPaths.get(0).get(0));

            updateNextButtonText();

            //recreate shortPaths and originalPaths
            shortPaths = pr.createGoals(goals,start);
            ArrayList<String> temp = new ArrayList<>();
            if(shortPaths.isEmpty()) {
                temp.add(originalPaths.get(originalPaths.size()-2).get(0));
            }
            else {
                temp.add(shortPaths.get(shortPaths.size() - 1).get(1));
            }
            originalPaths = pr.createGoals(originalGoals,"entrance_exit_gate");
            temp.add("entrance_exit_gate");
            shortPaths.add(temp);
            originalPaths.add(temp);


            tv.setText(pr.setShortestPath(shortPaths, goals, false, isBrief));
        }
    }

    private void onBriefDirectionsChecked() {
        if(brief_directions.isChecked())
            isBrief = true;
        else
            isBrief = false;

        if (shortPaths.isEmpty())
            shortPaths.add(0, originalPaths.get(originalPaths.size() - 1));
        else {
            for (int i = 1; i < originalPaths.size(); i++) {
                if (shortPaths.get(0).equals(originalPaths.get(i))) {
                    shortPaths.add(0, originalPaths.get(i - 1));
                    break;
                }
            }
        }
    }
}