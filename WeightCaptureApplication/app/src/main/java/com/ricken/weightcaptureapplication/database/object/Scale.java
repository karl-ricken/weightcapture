package com.ricken.weightcaptureapplication.database.object;

import com.ricken.weightcaptureapplication.IdElement;

public class Scale extends IdElement {
    private String name;
    public Scale(){}
    public Scale(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String toString(){
        return getName();
    }
}
