package com.ricken.weightcaptureapplication.database.object;

import com.ricken.weightcaptureapplication.IdElement;

public class Weight extends IdElement {
    private long time;
    private int scale;
    private double value;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
