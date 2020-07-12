package com.example.uasmkppb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Restaurants {
    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
