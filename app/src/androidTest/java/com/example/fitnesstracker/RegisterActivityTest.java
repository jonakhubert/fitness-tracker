package com.example.fitnesstracker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import com.example.fitnesstracker.Activities.LoginActivity;
import com.example.fitnesstracker.Activities.RegisterActivity;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private ActivityScenario<RegisterActivity> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(RegisterActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void testForgotPasswordButton()
    {
        onView(withId(R.id.loginRef)).perform(ViewActions.click());
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LoginActivity.class);
        scenario.onActivity(activity -> activity.startActivity(intent));
    }
}