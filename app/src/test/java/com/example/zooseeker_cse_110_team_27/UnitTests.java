package com.example.zooseeker_cse_110_team_27;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowMediaPlayer;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UnitTests {
    private SearchListItemDao dao;
    private SearchDatabase db;

    @Rule
    public ActivityScenarioRule<SearchListActivity> scenarioRule = new ActivityScenarioRule<>(SearchListActivity.class);

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context,SearchDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.searchListItemDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void addExhibitToDao() {
        SearchListItem exhibit1 = new SearchListItem("Gorillas",0,true );
        SearchListItem exhibit2 = new SearchListItem("Flamingos",1,false );

        long id1 = dao.insert(exhibit1);
        long id2 = dao.insert(exhibit2);

        assertNotEquals(id1,id2);
        assertEquals(exhibit1.exhibitName,dao.get(id1).exhibitName);
        assertEquals(exhibit2.exhibitName,dao.get(id2).exhibitName);
        assertEquals(exhibit1.selected,true);
        assertEquals(exhibit2.selected,false);

    }


}
