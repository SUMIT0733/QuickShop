package com.example.quickshop.model;

public class SearchModel {

    private String product_name;
    private String category;

    public SearchModel(String product_name, String category) {
        this.product_name = product_name;
        this.category = category;
    }

    public SearchModel() { }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
