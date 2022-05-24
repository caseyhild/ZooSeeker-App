package com.example.zooseeker_cse_110_team_27;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SearchListItemDao {

    @Insert
    long insert(SearchListItem searchListItem);

    @Query("DELETE FROM search_list_items")
    void nukeTable();

    @Query("SELECT * FROM `search_list_items` WHERE `id` = :id")
    SearchListItem get(long id);

    @Query("SELECT * FROM `Search_list_items` ORDER BY `order`")
    List<SearchListItem> getAll();

    @Query("SELECT COUNT(*) FROM `search_list_items`")
    LiveData<Integer> getDataCount();

    @Update
    int update(SearchListItem searchListItem);

    @Delete
    int delete(SearchListItem searchListItem);

    @Insert
    List<Long> insertAll(List<SearchListItem> searchListItem);

    @Query("SELECT * FROM Search_list_items ORDER BY `order`")
    LiveData<List<SearchListItem>> getAllLive();

    @Query("SELECT `order` + 1 FROM Search_list_items ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();

}