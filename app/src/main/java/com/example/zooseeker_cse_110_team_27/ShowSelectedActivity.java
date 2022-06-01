package com.example.zooseeker_cse_110_team_27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowSelectedActivity extends AppCompatActivity {

    private ArrayList<String> selected;
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("show_sel_activity", "starting page");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selected);

        selected = (ArrayList<String>) getIntent().getSerializableExtra("names");
        tv = findViewById(R.id.selected_tv);
        tv.setMovementMethod(new ScrollingMovementMethod());

        btn = findViewById(R.id.finish_btn);
        btn.setOnClickListener(this::onBackClicked);


        Log.d("showList", selected.toString());

        for (String sel : selected) {
            tv.append(sel + "\n\n");
        }

    }

    private void onBackClicked(View view) {
        finish();
    }
}