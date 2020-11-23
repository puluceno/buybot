package com.pulu.buyer.stores;

public abstract class Store {
    private final String name;
    private final String baseUrl;

    public Store(String name, String baseUrl) {
        this.name = name;
        this.baseUrl = baseUrl;
    }

}
