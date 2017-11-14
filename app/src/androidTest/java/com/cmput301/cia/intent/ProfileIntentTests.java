/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.intent;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.activities.users.UserProfileActivity;
import com.cmput301.cia.models.Profile;
import com.robotium.solo.Solo;

import java.lang.reflect.Field;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 12 2017
 *
 * This class tests the UI for viewing a user's profile
 * NOTE: These tests require an internet connection
 */

// TODO: viewing other people's profiles, changing the profile image, following/saving

public class ProfileIntentTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public ProfileIntentTests() {
        super(com.cmput301.cia.activities.MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
        Log.d("SETUP", "setUp()");

        solo.enterText((EditText)solo.getView(R.id.loginNameEdit), "nowitenz3");
        solo.clickOnButton("Login");
        solo.sleep(3000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

    }

    /**
     * Test to make sure the profile being viewed is the signed in user's profile
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void testViewedProfile() throws NoSuchFieldException, IllegalAccessException {

        solo.clickOnActionBarItem(R.id.menu_button_My_Profile);
        solo.clickOnMenuItem("My Profile");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("profile");
        field.setAccessible(true);
        Profile viewed = (Profile)field.get(solo.getCurrentActivity());

        Field field2 = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field2.setAccessible(true);
        Profile user = (Profile)field.get(solo.getCurrentActivity());

        assertTrue(viewed.equals(user));
    }

    /**
     * Test selecting the save button and making sure that the comment and image save as a result
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void testSave() throws NoSuchFieldException, IllegalAccessException {

        String newComment = "this is a test comment @das";

        Field field2 = solo.getCurrentActivity().getClass().getDeclaredField("user");
        field2.setAccessible(true);
        Profile user = (Profile)field2.get(solo.getCurrentActivity());
        user.setComment("");
        assertFalse(user.getComment().equals(newComment));

        solo.clickOnActionBarItem(R.id.menu_button_My_Profile);
        solo.clickOnMenuItem("My Profile");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", UserProfileActivity.class);

        Field field = solo.getCurrentActivity().getClass().getDeclaredField("profile");
        field.setAccessible(true);
        Profile viewed = (Profile)field.get(solo.getCurrentActivity());

        viewed.setComment("");
        assertFalse(viewed.getComment().equals(newComment));

        solo.clearEditText((EditText)solo.getView(R.id.profileCommentDynamicText));
        solo.sleep(500);
        solo.enterText((EditText)solo.getView(R.id.profileCommentDynamicText), newComment);
        solo.clickOnButton("Save");
        solo.sleep(1000);
        solo.assertCurrentActivity("wrong activity", HomePageActivity.class);

        assertTrue(user.getComment().equals(newComment));
        // TODO: test image saving
    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}