package com.example.admin.retrofitmeetsrxjava.data.modelhomeworklocation;

import java.io.Serializable;

/**
 * Created by Admin on 10/2/2017.
 */

public class Place implements Serializable{

    Double latitude;
    Double longitude;

    public Place(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
