package com.pulu.scraper.engine.impl;

import com.pulu.scraper.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BestbuyScraper extends DefaultScraper {

    private static final String AUTO_NOTIFY = "AUTO NOTIFY";
    private static final String SOLD_OUT = "SOLD OUT";
    private static final String CHECK_STORES = "CHECK STORES";
    private static final String SHOP_OPEN_BOX = "SHOP OPEN-BOX";
    private static final String COMING_SOON = "COMING SOON";
    private static final String LINK_PREFIX = "https://www.bestbuy.com";

    @Override
    public List<Product> scrape(String uri, boolean inStock) {

        if (!uri.contains("bestbuy")) {
            return Collections.emptyList();
        }
        List<Product> ret = new ArrayList<>();

        try {
            Document page = Jsoup.parse(new URL(uri), 8000);


            page.select("div[class=right-column]").forEach(it -> {
                String name = it.select("h4[class=sku-header] > a").text();
                String link = LINK_PREFIX + it.select("h4[class=sku-header] > a").attr("href");
                String stock = it.select("div[class=sku-list-item-button]").text().toUpperCase();
                if (inStock) {
                    if (stock.contains(AUTO_NOTIFY) || stock.contains(SOLD_OUT) ||
                            stock.contains(CHECK_STORES) || stock.contains(SHOP_OPEN_BOX) ||
                            stock.contains(COMING_SOON)) {

                        ret.add(new Product(name, link));
                    }
                } else {
                    if (!stock.contains(AUTO_NOTIFY) && !stock.contains(SOLD_OUT) &&
                            !stock.contains(CHECK_STORES) && !stock.contains(SHOP_OPEN_BOX) &&
                            !stock.contains(COMING_SOON)) {
                        ret.add(new Product(name, link));
                    }
                }
            });
        } catch (IOException e) {
            System.err.println(e.getMessage() + " while reading BestBuy");
        }

        return ret;
    }

}
