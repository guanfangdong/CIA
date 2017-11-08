/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.CreateHabitEventController;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.utilities.DatePickerUtilities;
import com.cmput301.cia.utilities.DeviceUtilities;
import com.cmput301.cia.utilities.ImageUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// TODO: place picker

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 7 2017
 *
 * This class represents the activity for creating a new habit event
 */

public class CreateHabitEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // The name of the intent data representing the owning habit's name
    public static final String ID_HABIT_NAME = "Habit", ID_HABIT_HASH = "HabitID";

    // Intent returned ID for the habit event
    public static final String RETURNED_HABIT = "HabitEvent";

    // All images can be at most this number of bytes
    public static final int MAX_IMAGE_SIZE = 65535;

    // Result code for selecting an image from gallery
    public static final int SELECT_IMAGE_CODE = 1;

    // The view displaying the image
    private ImageView imageView;

    // The view displaying the event date in text format
    private TextView dateText;

    // The button that resets the selected image when clicked
    private Button resetImageButton;

    // the location the event occurred at
    private Location location;

    // the image attached to the event
    private Bitmap image;

    // The unique ID of the habit this event is being created for
    private String habitId;

    // The date this event occurred on
    private Date eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit_event);

        location = null;
        image = null;
        eventDate = new Date();

        Intent intent = getIntent();
        String habitName = intent.getStringExtra(ID_HABIT_NAME);
        habitId = intent.getStringExtra(ID_HABIT_HASH);
        ((TextView)findViewById(R.id.cheHabitNameText)).setText(habitName);

        imageView = (ImageView)findViewById(R.id.cheImageView);
        dateText = (TextView) findViewById(R.id.cheDateTextView);
        setDateText();
        resetImageButton = (Button)findViewById(R.id.cheResetImageButton);

        Toast.makeText(this, "Select the image to pick one to attach", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Handles the save button being clicked
     * @param view
     */
    public void onSaveClicked(View view){
        finishActivity(false);
    }

    /**
     * Handles the cancel button being clicked
     * @param view
     */
    public void onCancelClicked(View view){
        finishActivity(true);
    }

    /**
     * The following 2 functions are based on Benjamin's answer from
     * https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
     *
     * Permalink to specific comment:
     * https://stackoverflow.com/a/5309965
     */
    public void onImageClicked(View view) {
        CreateHabitEventController.clickImage(this);
    }

    /**
     * Handle the results of the image selection activity
     * @param requestCode id of the finished activity
     * @param resultCode code representing whether that activity was successful or not
     * @param data the data returned from that activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO: put an image on the emulator and test choosing it
        if (requestCode == SELECT_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {

                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap chosenImage = BitmapFactory.decodeStream(inputStream);
                chosenImage = ImageUtilities.compressImageToMax(chosenImage, MAX_IMAGE_SIZE);

                if (chosenImage == null) {
                    image = null;
                    updateImage();
                } else if (chosenImage.getByteCount() <= MAX_IMAGE_SIZE) {
                    image = chosenImage;
                    updateImage();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a dialog so that the user can choose a date instead of typing
     * see: DatePickerUtilities
     * @param v: the layout that it's coming from
     */
    public void datePickerDialog(View v) {
        DatePickerUtilities datePickerFragment = new DatePickerUtilities();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /** for the date selected
     * @param datePicker : the widget object for selecting a date
     * @param year : the year chosen
     * @param month : the month chosen
     * @param day : the day chosen
     * see: DatePickerUtilities
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        eventDate = calendar.getTime();
        setDateText();
    }

    /**
     * Reset the image view to nothing
     * @param view
     */
    // TODO: test
    public void onResetImageClicked(View view){
        image = null;
        updateImage();
    }

    /**
     * When the attach location button is clicked
     * @param view
     */
    public void onAttachLocationClicked(View view){
        // TODO: place picker
        location = DeviceUtilities.getLocation();
    }

    /**
     * Update the view displaying the event's date in text form
     */
    private void setDateText(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateText.setText(format.format(eventDate));
    }

    /**
     * Update the image view after an image has been selected or removed
     */
    private void updateImage(){
        // TODO: try setImageBitmap(null) to see if it works fine
        if (image != null) {
            imageView.setImageBitmap(image);
            resetImageButton.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setImageAlpha(0);
            resetImageButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * End this activity after the user clicks on either "Save" or "Cancel"
     * Adds data to the intent object if the user selects "Save"
     * @param cancelled whether the cancel button was clicked or not
     */
    private void finishActivity(boolean cancelled){
        Intent intent = new Intent();
        if (cancelled) {
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            return;
        }

        String comment = ((EditText)findViewById(R.id.cheCommentEditText)).getText().toString();
        HabitEvent event = new HabitEvent(comment);

        event.setDate(eventDate);
        event.setLocation(location);
        if (image != null){
            event.setBase64EncodedPhoto(ImageUtilities.imageToBase64(image));
        }

        intent.putExtra(RETURNED_HABIT, event);
        intent.putExtra(ID_HABIT_HASH, habitId);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}