package com.group3.project4.shop;

public class Item {
    String name, photo;
    Double price;
    Integer discount;

    public Item() {
        // empty
    }

    public Item(String name, String photo, Double price, Integer discount) {
        this.name = name;
        this.photo = photo;
        this.price = price;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
