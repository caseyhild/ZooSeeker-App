package com.example.zooseeker_cse_110_team_27;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {SearchListItem.class}, version = 1)
public abstract class SearchDatabase extends RoomDatabase {
    public static SearchDatabase singleton = null;

    public abstract SearchListItemDao searchListItemDao();

    public synchronized static SearchDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = SearchDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static SearchDatabase makeDatabase(Context context){
        return Room.databaseBuilder(context, SearchDatabase.class, "search_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(()->{
                            List<SearchListItem> searches = SearchListItem
                                    .loadJSON(context,"demo_Searches.json");
                            getSingleton(context).searchListItemDao().insertAll(searches);
                        });
                    }
                })
                .build();
    }
    @VisibleForTesting
    public static void injectTestDatabase(SearchDatabase testDatabase){
        if(singleton != null){
            singleton.close();
        }
        singleton = testDatabase;
    }
}
