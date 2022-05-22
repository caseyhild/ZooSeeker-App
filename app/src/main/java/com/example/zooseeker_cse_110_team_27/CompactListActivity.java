package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CompactListActivity extends AppCompatActivity{
    ArrayList<ArrayList<String>> shortPaths;
    TextView compactTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compact_list);

        compactTv = findViewById(R.id.compactlist_tv);
        shortPaths = (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("shortPaths");

        PlanRoute pr = new PlanRoute(this);

        //append to the textview with the shortest paths
        if (shortPaths.size() == 0) {
            compactTv.append("No more exhibits!");
        }
        else
            compactTv.append(pr.setCompactList(shortPaths));
    }

}