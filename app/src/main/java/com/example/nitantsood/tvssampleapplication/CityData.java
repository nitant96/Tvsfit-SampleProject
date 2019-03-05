package com.example.nitantsood.tvssampleapplication;

import java.io.Serializable;

public class CityData implements Serializable {
    String cityName;
    Double lattitude,longitude;

    public CityData(String cityName, Double lattitude, Double longitude) {
        this.cityName = cityName;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
