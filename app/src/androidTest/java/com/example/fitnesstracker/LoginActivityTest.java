package com.example.fitnesstracker;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.fitnesstracker.Activities.LoginActivity;
import com.example.fitnesstracker.Activities.RegisterActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private ActivityScenario<LoginActivity> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void testLoginRefButton()
    {
        onView(withId(R.id.registerRef)).perform(ViewActions.click());
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), RegisterActivity.class);
        scenario.onActivity(activity -> activity.startActivity(intent));
    }
}