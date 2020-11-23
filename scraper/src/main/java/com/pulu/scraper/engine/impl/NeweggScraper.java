package com.pulu.scraper.engine.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeweggScraper extends DefaultScraper {

    private static final String AUTO_NOTIFY = "AUTO NOTIFY";
    private static final String SOLD_OUT = "SOLD OUT";
    private static final String VIEW_DETAILS = "VIEW DETAILS";

    @Override
    public List<String> scrape(String uri, boolean inStock) {

        if (!uri.contains("newegg")) {
            return Collections.emptyList();
        }

        List ret = new ArrayList<String>();

        try {
            Document page = Jsoup.parse(new URL(uri), 8000);


            page.select("#app > div.page-content > section > div > div > div.row-body > div > div > div > div.row-body > div > div.list-wrap > div.item-cells-wrap > div.item-cell > div.item-container").forEach(it -> {
                String product = it.getElementsByAttributeStarting("a").get(0).attr("title");
                String link = it.getElementsByAttribute("href").get(0).attr("href");
                String stock = it.select("div.item-action > div.item-operate > div.item-button-area").text().toUpperCase();
                if (inStock) {
                    if (stock.contains(AUTO_NOTIFY) || stock.contains(SOLD_OUT)) {
                        ret.add(product + "‽" + link);
                    }
                } else {
                    if (!stock.contains(AUTO_NOTIFY) && !stock.contains(SOLD_OUT) && !stock.contains(VIEW_DETAILS)) {
                        ret.add(product + "‽" + link);
                    }
                }
            });
        } catch (IOException e) {
            System.err.println(e.getMessage() + " while reading Newegg");
        }

        return ret;
    }
}
