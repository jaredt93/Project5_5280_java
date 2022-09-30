package com.group3.project4.shop;

import com.google.gson.annotations.SerializedName;

public class ItemResponse {
    @SerializedName("results")
    private Item[] itemsArray;

    public Item[] getItemsArray() {
        return itemsArray;
    }

    public void setItemsArray(Item[] itemsArray) {
        this.itemsArray = itemsArray;
    }
}
