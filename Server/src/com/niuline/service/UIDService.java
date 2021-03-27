package com.niuline.service;

public class UIDService {
    private int index;

    public UIDService() {
        this.index = 0;
    }

    public int generateIndex() {
        return index++;
    }


}
