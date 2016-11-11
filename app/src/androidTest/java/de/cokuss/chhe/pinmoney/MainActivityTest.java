package de.cokuss.chhe.pinmoney;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.androidannotations.annotations.Click;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import de.cokuss.chhe.pinmoney.activity.MainActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.startsWith;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

    @Test
    public void no1_legeTestkundenAn() {
        onView(allOf(withId(-1),isClickable())).perform(click());
        onView(withText("Empfänger")).perform(click());
        onView(withText("Neuer Empfänger")).perform(click());
        onView(withId(R.id.input_name)).perform(typeText("Testkunde"));
        onView(withId(R.id.input_name)).check(matches(withText("Testkunde")));
        onView(withId(R.id.input_birthday)).perform(typeText("01.01.2000"));
        onView(withId(R.id.input_startDate)).perform(typeText("10.01.2016"));
        onView(withId(R.id.input_birthday)).check(matches(withText("01.01.2000")));
        onView(withId(R.id.input_startDate)).check(matches(withText("10.01.2016")));
        onView(withId(R.id.radioButton_dayli)).perform(click());
        onView(withId(R.id.betrag)).perform(typeText("1"));
        onView(withId(R.id.betrag)).check(matches(withText("1")));
        onView(withId(R.id.startBetrag)).perform(typeText("10"));
        onView(withId(R.id.startBetrag)).check(matches(withText("10")));
        onView(withId(R.id.button_save_new_child)).perform(click());
    }

    @Test
    public void no999_beenden() {
        onView(allOf(withId(-1),isClickable())).perform(click());
        onView(withText("Beenden")).perform(click());
    }
}
