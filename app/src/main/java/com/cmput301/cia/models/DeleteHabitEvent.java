/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

/**
 * @author Adil Malik
 * @version 3
 * Date: Nov 27 2017
 * This class represents a pending event to delete a user's habit event
 */

public class DeleteHabitEvent extends OfflineEvent {

    /**
     * Construct a new command object that will delete the specified event when executed
     * @param event the event to delete
     */
    public DeleteHabitEvent(HabitEvent event){
        super(event);
    }

    /**
     * What this event does when it is synchronized with the server
     * @return whether the event was handled successfully or not
     */
    @Override
    public boolean handle(Profile profile) {
        Habit habit = profile.getHabitById(event.getHabitId());
        if (habit != null)
            habit.removeHabitEvent(event);
        return profile.save();
    }
}
