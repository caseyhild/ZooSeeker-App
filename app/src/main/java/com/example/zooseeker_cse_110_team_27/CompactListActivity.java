package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CompactListActivity extends AppCompatActivity{
    ArrayList<ArrayList<String>> shortPaths;
    String currLoc;
    TextView compactTv;
    Button returnBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compact_list);

        compactTv = findViewById(R.id.compactlist_tv);
        shortPaths = (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("shortPaths");
        currLoc = (String) getIntent().getSerializableExtra("currLoc");

        PlanRoute pr = new PlanRoute(this);

        returnBtn = findViewById(R.id.return_btn);
        returnBtn.setOnClickListener(this::onReturnClicked);

        //append to the textview with the shortest paths
        if (shortPaths.size() == 0) {
            compactTv.append("No more exhibits!");
        }
        else
            compactTv.append(pr.setCompactList(shortPaths, currLoc));
    }

    private void onReturnClicked(View view) {
        finish();
    }
}