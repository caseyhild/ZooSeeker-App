package com.example.zooseeker_cse_110_team_27;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class SearchListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private SearchView searchView;
    private SearchListViewModel viewModel;
    private Button addExhibitButton;
    private Button planRouteButton;
    private List<Exhibit> exhibits;
    private Map<String,String> exhibitTagMap;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        viewModel = new ViewModelProvider(this).get(SearchListViewModel.class);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

        SearchListAdapter adapter = new SearchListAdapter();
        adapter.setOnDeleteButtonClicked(viewModel::deleteSearchExhibit);
        adapter.setHasStableIds(true);
        viewModel.getSearchListItems().observe(this, adapter::setSearchListItems);

        recyclerView = findViewById(R.id.search_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        exhibits = Exhibit.loadJSONForSearching(this, "sample_node_info.json");
        exhibitTagMap = Exhibit.getSearchMap(exhibits);
        this.searchView = this.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextChange(String newText){
                        //TODO
                        //text changes, search exhibitTagMap, update list_view
                        return false;
                    }
                    @Override
                    public boolean onQueryTextSubmit(String query){
                        //perform the final search
                        return false;
                    }
                }
        );
        this.addExhibitButton = this.findViewById(R.id.add_exhibit_btn);
        addExhibitButton.setOnClickListener(this::onAddSearchClicked);

        this.planRouteButton = this.findViewById(R.id.plan_route_btn);
        planRouteButton.setOnClickListener(this::onPlanClicked);

    }

    private void doMySearch(String query) {
        //TODO
        //use query to check the exhibit tag map for matches, tell list_view
        //to display matches
    }

    private void onPlanClicked(View view) {
        Intent intent = new Intent(SearchListActivity.this, PlanRouteActivity.class);
        startActivity(intent);
    }



    private void onAddSearchClicked(View view) {
        String text = searchView.getQuery().toString();
        searchView.setQuery("", false);
        viewModel.createExhibit(text);
    }


}


