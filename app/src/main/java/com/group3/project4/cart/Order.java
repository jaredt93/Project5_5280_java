package com.group3.project4.cart;

import com.group3.project4.shop.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    ArrayList<Item> cartItems = new ArrayList<>();
    Double orderTotal;

    public Order() {
    }

    public Order(ArrayList<Item> cartItems) {
        this.cartItems = cartItems;
        orderTotal = getOrderTotal();
    }

    public ArrayList<Item> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<Item> cartItems) {
        this.cartItems = cartItems;
    }

    public Double getOrderTotal() {
        Double orderTotal = 0.00;

        for (Item cartItem: cartItems) {
            orderTotal = orderTotal + cartItem.getTotalCost();
        }
        return Math.round(orderTotal * 100.0) / 100.0;
    }

    public void setOrderTotal() {
        orderTotal = getOrderTotal();
    }
}
