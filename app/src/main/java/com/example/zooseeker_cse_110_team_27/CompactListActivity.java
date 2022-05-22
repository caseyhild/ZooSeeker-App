package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CompactListActivity extends AppCompatActivity{
    ArrayList<ArrayList<String>> shortGoals;
    TextView compactTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compact_list);

        compactTv = findViewById(R.id.compactlist_tv);
        shortGoals = (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("shortPaths");

        PlanRoute pr = new PlanRoute(this);
        while(!shortGoals.isEmpty()) {
            compactTv.append(pr.setCompactList(shortGoals));
        }

    }

}