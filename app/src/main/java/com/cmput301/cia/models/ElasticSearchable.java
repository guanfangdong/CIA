/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.models;

import io.searchbox.annotations.JestId;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 2 2017
 *
 * Represents an object that can be stored and retrieved from an ElasticSearch database
 */

public abstract class ElasticSearchable {

    @JestId
    private String id;

    /**
     * @return the object's template type id
     */
    public abstract String getTypeId();

    /**
     * @return the object's unique hash
     */
    public String getId() {
        return id;
    }

    /**
     * Set the object's unique hash
     * @param id the object's unique hash
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Serialize this object to the ElasticSearch server
     */
    // TODO: this might be the exact same for all objects
    // if it is, make this non abstract and implement it here
    public abstract void save();

    /**
     * Synchronize this object from the ElasticSearch server
     */
    public abstract void load();

}