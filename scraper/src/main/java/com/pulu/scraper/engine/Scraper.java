package com.pulu.scraper.engine;

import java.util.List;

public interface Scraper {

    List<String> scrape(String uri, boolean inStock);

    List<String> scrape(List<String> uris, boolean inStock);
}
