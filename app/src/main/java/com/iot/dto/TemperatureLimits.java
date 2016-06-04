package com.iot.dto;

import java.io.Serializable;

/**
 * Created by gmartin on 28/05/2016.
 */
public class TemperatureLimits implements Serializable{
    private double min;
    private double max;

    public TemperatureLimits(){

    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
