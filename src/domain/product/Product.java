package domain.product;

import domain.profile.Step;

public class Product {
    public String goodsNo;
    public String brand;
    public String name;
    public String url;
    public int price;
    public String ingredients;
    public Step step;

    public Product(String goodsNo, String brand, String name, int price, String url, String ingredients, Step step) {
        this.goodsNo = goodsNo;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.url = url;
        this.ingredients = ingredients == null ? "" : ingredients;
        this.step = step;
    }
}