package com.example.zooseeker_cse_110_team_27;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.zooseeker_cse_110_team_27.location.Coord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchListActivity extends AppCompatActivity implements SearchListAdapter.ItemCallback{
    private Gson gson;
    public RecyclerView recyclerView;
    private SearchView searchView;
    private SearchListViewModel viewModel;
    private Button clearButton;
    private Button planRouteButton;
    private Button showListButton;
    private TextView deleteButton;
    private ArrayList<Exhibit> exhibits;
    private ArrayList<Exhibit> displayedExhibits;
    private List<SearchListItem> exhibitsinList;
    private Map<String, String> exhibitIdMap;
    private SearchListAdapter adapter;
    private TextView numExhibits;
    private Map<String,String> exhibitTagMap;
    private Map<String,String> selectedMap;
    private HashMap<String, Coord> coords;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d("search_list_activity", "starting page");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        gson = new Gson();

        viewModel = new ViewModelProvider(this).get(SearchListViewModel.class);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }


        //setup
        adapter = new SearchListAdapter(this);
        adapter.setHasStableIds(true);
        adapter.setOnCheckBoxClickedHandler(viewModel::toggleCompleted);
        viewModel.getSearchListItems().observe(this, adapter::setSearchListItems);
        recyclerView = findViewById(R.id.search_list_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        exhibits = (ArrayList<Exhibit>) Exhibit.loadJSONForSearching(this, "sample_node_info.json");
        displayedExhibits = new ArrayList<>(exhibits);
        exhibitTagMap = Exhibit.getSearchMap(exhibits);
        exhibitIdMap = Exhibit.getIdMap(exhibits);
        coords = Exhibit.getCoordMap(exhibits);

        //load previous selected exhibits
        Log.d("search_list_activity", "load previous selected exhibits");

        SharedPreferences sh = getSharedPreferences("SelectedPref", MODE_PRIVATE);
        String savedSelectedMap = sh.getString("map","empty");
        if(savedSelectedMap.equals("empty")) {
            selectedMap = new HashMap<>();
            populateSelectedMap();
        }
        else {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            selectedMap = gson.fromJson(savedSelectedMap, type);
        }

        this.searchView = this.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextChange(String newText){
                        //text changes, search exhibitTagMap, update list_view
                        Log.d("tag",newText);
                        saveSelected();
                        updateExhibitList(newText);
                        populateDisplay();
                        return false;
                    }
                    @Override
                    public boolean onQueryTextSubmit(String query){
                        //perform the final search
                        return false;
                    }
                }
        );

        this.planRouteButton = this.findViewById(R.id.plan_route_btn);
        planRouteButton.setOnClickListener(this::onPlanClicked);

        this.showListButton = this.findViewById(R.id.show_selected_btn);
        showListButton.setOnClickListener(this::showListClicked);

        this.clearButton = this.findViewById(R.id.clear_btn);
        clearButton.setOnClickListener(this::onClearClicked);

        Log.d("search_list_activity", "update texts, save selections, display");

        updateTextView();
        saveSelected();
        populateDisplay();
    }

    private void showListClicked(View view) {
        Log.d("search_list_activity", "show list");

        Intent i = new Intent(SearchListActivity.this, ShowSelectedActivity.class);
        ArrayList<String> passExhibitNames = new ArrayList<>();

        for (SearchListItem e : exhibitsinList) {
            if (e.selected) {
                passExhibitNames.add(e.exhibitName);
            }
        }

        Log.d("showList", passExhibitNames.toString());

        i.putExtra("names", passExhibitNames);
        startActivity(i);

    }

    @Override
    protected void onPause() {
        saveSelectedExhibits();

        super.onPause();
    }

    private void saveSelectedExhibits() {
        SharedPreferences sharedPrefSelected = getSharedPreferences("SelectedPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefSelected.edit();
        editor.clear();

        String selectedMapString = gson.toJson(selectedMap);

        editor.putString("map",selectedMapString);

        editor.commit();
    }

    private void doMySearch(String query) {
        //TODO
        //use query to check the exhibit tag map for matches, tell list_view
        //to display matches
    }

    private void onPlanClicked(View view) {
        Log.d("search_list_activity", "plan button clicked");

        Intent intent = new Intent(SearchListActivity.this, PlanRouteActivity.class);
        ArrayList<String> passExhibitNames = new ArrayList<>();
        intent.putExtra("coords", coords);
        for (SearchListItem e : exhibitsinList) {
            if(e.selected) {
                String id = exhibitIdMap.get(e.exhibitName);
                for(Exhibit ex : exhibits) {
                    if(ex.id.equals(id)) {
                        if(ex.group_id != null) {
                            id = ex.group_id;
                        }
                    }
                }
                if(!passExhibitNames.contains(id)) {
                    passExhibitNames.add(id);
                }
            }
        }
        if(passExhibitNames.size() != 0) {
            intent.putExtra("key", passExhibitNames);
            startActivity(intent);
        }
    }

    private void onClearClicked(View view) {
        Log.d("search_list_activity", "clear button clicked");

        adapter.resetSelected();
        updateExhibitList("");
        populateDisplay();
    }

    @Override
    public void updateTextView() {
        Log.d("search_list_activity", "text view updated");

        String update = adapter.getSelected() + "";
        planRouteButton.setText("Plan: " + update);
        saveSelected();
    }

    public void getExhibitsinList(List<SearchListItem> exhibitList) {
        Log.d("search_list_activity", "exhibits gotten");

        exhibitsinList = exhibitList;
    }

    public List<SearchListItem> returnExhibitList() {
        return exhibitsinList;
    }

    public void updateExhibitList(String filter) {
        Log.d("search_list_activity", "update list of exhibits");

        displayedExhibits = new ArrayList<>();
        for(Exhibit e : exhibits) {
            Log.d("tag",e.name);
            int filterLength = filter.length();
            if(!displayedExhibits.contains(e)) {
                if (filterLength <= e.name.length() && e.name.toLowerCase().substring(0, filterLength).equals(filter.toLowerCase())) {
                    Log.d("tag", "contained");
                    displayedExhibits.add(e);
                }
                for (String tag : e.tags) {
                    if (filterLength <= tag.length() && tag.toLowerCase().substring(0, filterLength).equals(filter.toLowerCase())) {
                        Log.d("tag", "contained");
                        displayedExhibits.add(e);
                    }
                }
            }
        }

        Log.d("search_list_activity", "exhibit list saved");

        saveSelected();
    }

    public void populateDisplay() {
        Log.d("search_list_activity", "clear the display");

        clearDisplay();
        for(int i = 0; i < displayedExhibits.size(); i++)
        {
            Exhibit e = displayedExhibits.get(i);
            List<String> list = viewModel.getNames();
            if(!list.contains(e.name) && e.kind.equals("exhibit")) {
                if(selectedMap.get(e.name).equals("true"))
                    viewModel.createExhibit(e.name,true);
                else
                    viewModel.createExhibit(e.name,false);
            }
        }
        Log.d("search_list_activity", "update display texts");

        updateTextView();
    }

    public void populateSelectedMap() {
        Log.d("search_list_activity", "put exhibits into the map");

        for(Exhibit e : exhibits) {
            selectedMap.put(e.name,"false");
        }
    }

    public void saveSelected() {
        Log.d("search_list_activity", "save exhibits selected");

        for(SearchListItem s : adapter.getList()) {
            if(s.selected)
                selectedMap.put(s.exhibitName, "true");
            else
                selectedMap.put(s.exhibitName, "false");
        }
    }

    public void clearDisplay() { viewModel.clearList(); }
}


