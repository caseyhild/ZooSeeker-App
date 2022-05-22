package com.example.zooseeker_cse_110_team_27;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchListActivity extends AppCompatActivity implements SearchListAdapter.ItemCallback{
    public RecyclerView recyclerView;
    private SearchView searchView;
    private SearchListViewModel viewModel;
    private Button addExhibitButton;
    private Button planRouteButton;
    private TextView deleteButton;
    private ArrayList<Exhibit> exhibits;
    private List<SearchListItem> exhibitsinList;
    private Map<String, String> exhibitIdMap;
    private SearchListAdapter adapter;
    private TextView numExhibits;
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

        adapter = new SearchListAdapter(this);
        //adapter.setOnDeleteButtonClicked(viewModel::deleteSearchExhibit);
        adapter.setHasStableIds(true);
        adapter.setOnCheckBoxClickedHandler(viewModel::toggleCompleted);
        viewModel.getSearchListItems().observe(this, adapter::setSearchListItems);
        recyclerView = findViewById(R.id.search_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        exhibits = (ArrayList<Exhibit>) Exhibit.loadJSONForSearching(this, "sample_node_info.json");
        exhibitTagMap = Exhibit.getSearchMap(exhibits);
        exhibitIdMap = Exhibit.getIdMap(exhibits);

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

        this.numExhibits = this.findViewById(R.id.num_exhibits_view);

        updateTextView();
        populateDisplay();
    }

    private void doMySearch(String query) {
        //TODO
        //use query to check the exhibit tag map for matches, tell list_view
        //to display matches
    }

    private void onPlanClicked(View view) {
        Intent intent = new Intent(SearchListActivity.this, PlanRouteActivity.class);
        ArrayList<String> passExhibitNames = new ArrayList<>();
        for (SearchListItem e : exhibitsinList) {
            String id = exhibitIdMap.get(e.exhibitName);
            passExhibitNames.add(id);
        }
        intent.putExtra("key", passExhibitNames);
        startActivity(intent);
    }



    private void onAddSearchClicked(View view) {
        String text = searchView.getQuery().toString();

        if(text.length() == 0 || viewModel.getExhibitNames().contains(exhibitTagMap.get(text))){
            return;
        }

        if (exhibitTagMap.containsKey(text)) {
            searchView.setQuery("", false);
            viewModel.createExhibit(exhibitTagMap.get(text));
            updateTextView();
        }
    }

    @Override
    public void updateTextView() {
        String update = adapter.getSelected() + "";
        numExhibits.setText(update);
    }

    public void getExhibitsinList(List<SearchListItem> exhibitList) {
        exhibitsinList = exhibitList;
    }

    public void populateDisplay() {
        for(int i = 0; i < exhibits.size(); i++)
        {
            Exhibit e = exhibits.get(i);
            List<String> list = viewModel.getNames();
            if(!list.contains(e.name) && e.kind.equals("exhibit")) {
                viewModel.createExhibit(e.name);
            }
        }
        updateTextView();
    }
}


