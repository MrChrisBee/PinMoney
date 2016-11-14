package de.cokuss.chhe.pinmoney;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import de.cokuss.chhe.pinmoney.activity.MainActivity_;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);
    String name = "Testkunde";

    @Test
    public void a0_isCustomerInList() {
        //delete old customer
        onView(withId(R.id.spinner4Child)).perform(click());
        onData(hasToString(startsWith(name))).perform(click());
        onView(allOf(withId(-1), isClickable())).perform(click());
        onView(withText("Empfänger")).perform(click());
        onView(withText("Empfänger löschen")).perform(click());
        String findme = "Soll das Account " + name + " wirklich gelöscht werden ?";
        onView(withText(findme)).check(matches(isDisplayed()));
        onView(withText("Ja")).perform(click());
    }

    @Test
    public void a1_LegeTestkundenAn() {

        onView(allOf(withId(-1), isClickable())).perform(click());
        onView(withText("Empfänger")).perform(click());
        onView(withText("Neuer Empfänger")).perform(click());

        onView(withId(R.id.input_name)).perform(typeText(name));
        onView(withId(R.id.input_name)).check(matches(withText(name)));
        closeSoftKeyboard();
        onView(withId(R.id.input_birthday)).perform(click());
        onView(withId(R.id.input_birthday)).perform(typeText("01.01.2000"));
        onView(withId(R.id.input_birthday)).check(matches(withText("01.01.2000")));
        closeSoftKeyboard();
        onView(withId(R.id.radioButton_weekly)).perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.betrag)).perform(typeText("1"));
        onView(withId(R.id.betrag)).check(matches(withText("1")));
        closeSoftKeyboard();
        onView(withId(R.id.startBetrag)).perform(typeText("10"));
        onView(withId(R.id.startBetrag)).check(matches(withText("10")));
        closeSoftKeyboard();
        onView(withId(R.id.start_date)).perform(replaceText("10.01.2016"));
        onView(withId(R.id.start_date)).check(matches(withText("10.01.2016")));
        closeSoftKeyboard();
        onView(withId(R.id.button_save_new_child)).perform(click());
    }

//  todo buchen und Konstostand checken
//  feste anzahl an tagen zurück rechnen und checken ob das errechnete taschengeld stimmt
//  testen das nicht mehrere automatische buchungen erzeugt werden
//  kann man mit den intends das Datum neu setzen ?


//    @Test
//    public void no999_beenden() {
//        onView(allOf(withId(-1),isClickable())).perform(click());
//        onView(withText("Beenden")).perform(click());
//    }

}
