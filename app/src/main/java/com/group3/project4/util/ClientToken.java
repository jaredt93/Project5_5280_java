package com.group3.project4.util;

import com.google.gson.annotations.SerializedName;

public class ClientToken {

    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }
}