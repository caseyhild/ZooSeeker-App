package com.example.zooseeker_cse_110_team_27;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowMediaPlayer;

import static org.junit.Assert.*;

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
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UnitTests {
    @Rule
    public ActivityScenarioRule<SearchListActivity> scenarioRule = new ActivityScenarioRule<>(SearchListActivity.class);

    @Test
    public void listDisplayed() {
        ActivityScenario<SearchListActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            ListView list = activity.findViewById(R.id.list_view);
            assertNotNull(list);
        });
    }
}
