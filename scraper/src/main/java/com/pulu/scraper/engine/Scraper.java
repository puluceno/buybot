package com.pulu.scraper.engine;

import com.pulu.scraper.model.Product;

import java.util.List;

public interface Scraper {

    List<Product> scrape(String uri, boolean inStock);

    List<Product> scrape(List<String> uris, boolean inStock);
}
