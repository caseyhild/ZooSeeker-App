package com.example.zooseeker_cse_110_team_27;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchStoryTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void searchAddsExhibit() {
        ActivityScenario<MainActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searched = activity.findViewById(R.id.search_bar);
            Button addButton = activity.findViewById(R.id.add_exhibit_btn);
            TextView exhibit = activity.findViewById(R.id.searched_item);

            if(searched != null && addButton != null && exhibit != null) {
                searched.setQuery("Bear", true);
                addButton.performClick();
                assertEquals("Bear", exhibit.getText());
            }
        });
    }

    @Test
    public void listDisplayed() {
        ActivityScenario<MainActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            SearchView searched = activity.findViewById(R.id.search_bar);
            Button addButton = activity.findViewById(R.id.add_exhibit_btn);
            ListView list = activity.findViewById(R.id.list_view);

            if(searched != null && addButton != null) {
                searched.setQuery("Bear", true);
                addButton.performClick();
                assertNotNull(list);
            }
        });
    }
}