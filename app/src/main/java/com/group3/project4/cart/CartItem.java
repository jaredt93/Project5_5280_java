package com.group3.project4.cart;

import com.group3.project4.shop.Item;

public class CartItem {
    Item item;
    Integer quantity;
    Double finalPrice;

    public CartItem() {
        // empty
    }

    public CartItem(Item item, Integer quantity, Double finalPrice) {
        this.item = item;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
