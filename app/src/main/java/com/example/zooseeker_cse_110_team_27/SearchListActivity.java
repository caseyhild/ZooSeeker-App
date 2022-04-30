package com.example.zooseeker_cse_110_team_27;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private EditText searchBarText;
    private SearchListViewModel viewModel;
    private Button addExhibitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        viewModel = new ViewModelProvider(this).get(SearchListViewModel.class);


        SearchListAdapter adapter = new SearchListAdapter();
        adapter.setOnDeleteButtonClicked(viewModel::deleteSearchExhibit);
        adapter.setHasStableIds(true);
        viewModel.getSearchListItems().observe(this, adapter::setSearchListItems);

        recyclerView = findViewById(R.id.search_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        this.searchBarText = this.findViewById(R.id.search_bar);
        this.addExhibitButton = this.findViewById(R.id.add_exhibit_btn);
        addExhibitButton.setOnClickListener(this::onAddSearchClicked);
    }

    private void onAddSearchClicked(View view) {
        String text = searchBarText.getText().toString();
        searchBarText.setText("");
        viewModel.createExhibit(text);
    }


}


