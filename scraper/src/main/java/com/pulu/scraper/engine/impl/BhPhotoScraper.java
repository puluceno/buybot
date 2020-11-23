package com.pulu.scraper.engine.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BhPhotoScraper extends DefaultScraper {

    private static final String LINK_PREFIX = "https://www.bhphotovideo.com";

    @Override
    public List<String> scrape(String uri, boolean viewInStock) {
        if (!uri.contains("bhphotovideo")) {
            return Collections.emptyList();
        }

        List ret = new ArrayList<String>();

        try {
            Document page = Jsoup.connect(uri).get();

            page.getElementsByAttributeValue("data-selenium", "miniProductPageProduct").forEach(it -> {
                String product = it.getElementsByAttributeValue("data-selenium", "miniProductPageProductName").text();
                String link = LINK_PREFIX + it.getElementsByAttributeValue("data-selenium", "miniProductPageProductNameLink").attr("href");
                boolean itemInStock = it.getElementsByAttributeValue("data-selenium", "addToCartButton").size() > 0;

                if (!viewInStock) {
                    if (itemInStock) {
                        ret.add(product + "‽" + link);
                    }
                } else {
                    if (!itemInStock) {
                        ret.add(product + "‽" + link);
                    }
                }
            });
        } catch (IOException e) {
            System.err.println(e.getMessage() + " while reading BhPhotoVideo");
        }
        return ret;
    }


}
