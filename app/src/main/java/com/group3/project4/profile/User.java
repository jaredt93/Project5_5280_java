package com.group3.project4.profile;

import com.group3.project4.cart.Order;
import com.group3.project4.shop.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class User implements Serializable {
    public static String FEMALE = "FEMALE";
    public static String MALE = "MALE";

    String email, first_name, last_name, city, gender, id, address;
    String token;
    int age, weight;

    Order order = new Order();
    ArrayList<Order> orderHistory = new ArrayList<>();
    String customerId;

    public User() {
        // empty constructor
    }

    public User(String id, String email, String first_name, String last_name, String city, String gender,
            String token, int age, int weight, String address, Order order, ArrayList<Order> orderHistory, String customerId) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
        this.gender = gender;
        this.token = token;
        this.age = age;
        this.weight = weight;
        this.address = address;
        this.order = order;
        this.orderHistory = orderHistory;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) && first_name.equals(user.first_name) && last_name.equals(user.last_name) && city.equals(user.city) && gender.equals(user.gender) && id.equals(user.id) && token.equals(user.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, first_name, last_name, city, gender, id, token);
    }
}
