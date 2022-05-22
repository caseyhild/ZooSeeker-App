package com.example.zooseeker_cse_110_team_27;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Set;

public class SearchListViewModel extends AndroidViewModel {


    private LiveData<List<SearchListItem>> searchListItems;
    private final SearchListItemDao searchListItemDao;
    private HashSet<String> exhibitNames;


    public SearchListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        SearchDatabase db = SearchDatabase.getSingleton(context);
        searchListItemDao = db.searchListItemDao();
        exhibitNames = new HashSet<>();
    }

    public LiveData<List<SearchListItem>> getSearchListItems() {
        if (searchListItems == null) {
            loadUsers();
        }
        return searchListItems;
    }

    private void loadUsers() {
        searchListItems = searchListItemDao.getAllLive();
    }

    public void updateText(SearchListItem SearchListItem, String newExhibitName) {
        SearchListItem.exhibitName = newExhibitName;
        searchListItemDao.update(SearchListItem);
    }

    public void createExhibit(String exhibitName) {
        int endOfListOrder = searchListItemDao.getOrderForAppend();
        exhibitNames.add(exhibitName);
        SearchListItem newItem = new SearchListItem(exhibitName, endOfListOrder, false);
        searchListItemDao.insert(newItem);
    }

    public void deleteSearchExhibit(SearchListItem item) {
        exhibitNames.remove(item.exhibitName);
        searchListItemDao.delete(item);
    }

    public int getNumExhibits() {
        return searchListItemDao.getAll().size();
    }

    public HashSet<String> getExhibitNames(){ return exhibitNames; }

    public List<String> getNames() {
        List<String> list = new ArrayList<>();
        for(SearchListItem s : searchListItemDao.getAll())
        {
            list.add(s.exhibitName);
        }
        return list;
    }

    public void toggleCompleted(SearchListItem searchListItem)
    {
        searchListItem.selected = !searchListItem.selected;
        searchListItemDao.update(searchListItem);
    }
}


