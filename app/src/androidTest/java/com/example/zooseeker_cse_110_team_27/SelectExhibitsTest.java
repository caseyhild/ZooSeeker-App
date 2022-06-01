package com.example.zooseeker_cse_110_team_27;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SelectExhibitsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void selectExhibitsTest() {
        /* TESTS
        * when no exhibits are selected, list is empty string
        * when exhibit(s) are selected, list is not empty
        * when selections are cleared, list is empty string
        * when search matches a result, result is displayed in drop down search list
        * when search does not match anything, nothing is displayed
        * */
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.search_bar),
                        withParent(allOf(withId(R.id.search_bar),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.show_selected_btn), withText("Show List"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.selected_tv),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("")));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.finish_btn), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_list_items),
                                        1),
                                1),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.show_selected_btn), withText("Show List"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.selected_tv),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText(not(""))));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.finish_btn), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_list_items),
                                        1),
                                1),
                        isDisplayed()));
        materialCheckBox2.perform(click());

        ViewInteraction materialCheckBox3 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_list_items),
                                        5),
                                1),
                        isDisplayed()));
        materialCheckBox3.perform(click());

        ViewInteraction materialCheckBox4 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_list_items),
                                        6),
                                1),
                        isDisplayed()));
        materialCheckBox4.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.show_selected_btn), withText("Show List"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton5.perform(click());


        ViewInteraction textView3 = onView(
                allOf(withId(R.id.selected_tv),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText(not(""))));

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.finish_btn), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.clear_btn), withText("Clear"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.show_selected_btn), withText("Show List"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.selected_tv),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(withText("")));

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.finish_btn), withText("Go Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(androidx.appcompat.R.id.search_button), withContentDescription("Search"),
                        childAtPosition(
                                allOf(withId(R.id.search_bar),
                                        childAtPosition(
                                                withId(R.id.search_bar),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("mon"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text), withText("mon"),
                        withParent(allOf(withId(androidx.appcompat.R.id.search_plate),
                                withParent(withId(androidx.appcompat.R.id.search_edit_frame)))),
                        isDisplayed()));
        editText.check(matches(withText("mon")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.search_list_items),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text), withText("mon"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete2.perform(replaceText("mont"));

        ViewInteraction searchAutoComplete3 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text), withText("mont"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete3.perform(closeSoftKeyboard());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(androidx.appcompat.R.id.search_close_btn), withContentDescription("Clear query"),
                        childAtPosition(
                                allOf(withId(androidx.appcompat.R.id.search_plate),
                                        childAtPosition(
                                                withId(androidx.appcompat.R.id.search_edit_frame),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatImageView2.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
