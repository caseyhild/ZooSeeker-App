package com.example.zooseeker_cse_110_team_27;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SearchListViewModel extends AndroidViewModel {


    private LiveData<List<SearchListItem>> searchListItems;
    private final SearchListItemDao searchListItemDao;


    public SearchListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        SearchDatabase db = SearchDatabase.getSingleton(context);
        searchListItemDao = db.searchListItemDao();
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
        SearchListItem newItem = new SearchListItem(exhibitName, endOfListOrder);
        searchListItemDao.insert(newItem);
    }

    public void deleteSearchExhibit(SearchListItem item) {

        searchListItemDao.delete(item);
    }

    public int getNumExhibits() {
        return searchListItemDao.getAll().size();
    }
}


