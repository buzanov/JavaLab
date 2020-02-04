package ru.javalab.homework7.models;

public class Product {
    private int id;
    private int price;
    private String name;

    public Product(int id, int price, String name) {
        this(price, name);
        this.id = id;
    }

    public Product(int price, String name) {
        this.price = price;
        this.name = name;
    }

    public Product(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
