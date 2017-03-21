package com.example.user.mvvmregistration;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.mvvmregistration.api.IApi;
import com.example.user.mvvmregistration.repository.DataManager;
import com.example.user.mvvmregistration.ui.RegistrationActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RegistrationScreenTest {

    @Rule
    public ActivityTestRule<RegistrationActivity> registrationActivityActivityTestRule =
            new ActivityTestRule<RegistrationActivity>(RegistrationActivity.class);

    @Test
    public void registrateButtonisEnabled(){
        Espresso.onView(ViewMatchers.withId(R.id.button)).check(ViewAssertions.matches(not(ViewMatchers.isEnabled())));
        //
        Espresso.onView(ViewMatchers.withId(R.id.edEmail)).perform(ViewActions.typeText("jsmithgmail.com"));
        Espresso.onView(ViewMatchers.withId(R.id.button)).check(ViewAssertions.matches(not(ViewMatchers.isEnabled())));

        Espresso.onView(ViewMatchers.withId(R.id.edEmail)).perform(ViewActions.clearText());

        Espresso.onView(ViewMatchers.withId(R.id.edEmail)).perform(ViewActions.typeText("jsmith@gmail.com"));
        Espresso.onView(ViewMatchers.withId(R.id.edPass)).perform(ViewActions.typeText("123"));
        Espresso.onView(ViewMatchers.withId(R.id.edConfirmPass)).perform(ViewActions.typeText(""));
        Espresso.onView(ViewMatchers.withId(R.id.button)).check(ViewAssertions.matches(not(ViewMatchers.isEnabled())));

        Espresso.onView(ViewMatchers.withId(R.id.edConfirmPass)).perform(ViewActions.typeText("123"));
        Espresso.onView(ViewMatchers.withId(R.id.button)).check(ViewAssertions.matches(ViewMatchers.isEnabled()));

        Espresso.onView(ViewMatchers.withId(R.id.edPass)).perform(ViewActions.clearText());
        Espresso.onView(ViewMatchers.withId(R.id.button)).check(ViewAssertions.matches(not(ViewMatchers.isEnabled())));
    }



}
