package com.pulu.scraper;

import com.pulu.buyer.NeweggEngine;
import com.pulu.scraper.engine.Scraper;
import com.pulu.scraper.engine.impl.BestbuyScraper;
import com.pulu.scraper.engine.impl.BhPhotoScraper;
import com.pulu.scraper.engine.impl.DefaultScraper;
import com.pulu.scraper.engine.impl.NeweggScraper;
import com.pulu.scraper.model.Product;
import org.apache.commons.lang3.StringUtils;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Runner {

    private static BufferedInputStream bufferedInputStream;
    private static AudioInputStream audioInputStream;
    private static FileHandler fh;
    private static final Logger logger = Logger.getLogger("pooplog");


    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "scraper/src/main/resources/chromedriver.exe");
        System.setProperty("webdriver.chrome.args", "--disable-logging");

        List<Product> scrape3080;
        List<Product> scrape6800xt;
        List<Product> scrape3070;
        List<Product> scrapeCpu;
        List<Product> scrapePs5;

        Set<Product> visitedProducts = new HashSet<>();

        Scraper scraper = new DefaultScraper(new BestbuyScraper(), new NeweggScraper(), new BhPhotoScraper());

        try {
            fh = new FileHandler("poop.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            View view = new View();


            while (true) {
                logger.info("Scraping... ");
                List<String> urls = view.getUrls();
                if (view.inStock) {
                    scrape3080 = scraper.scrape(Arrays.asList(urls.get(0), urls.get(5)), false);

                    scrape6800xt = scraper.scrape(Arrays.asList(urls.get(1), urls.get(6)), false);

                    scrape3070 = scraper.scrape(Arrays.asList(urls.get(2), urls.get(7)), false);

                    scrapeCpu = scraper.scrape(Arrays.asList(urls.get(3), urls.get(8), urls.get(13)), false);

                    scrapePs5 = scraper.scrape(Arrays.asList(urls.get(4), urls.get(9), urls.get(14)), false);

                    List<Product> products = Stream.of(scrape3080, scrape6800xt, scrape3070, scrapeCpu, scrapePs5).
                            flatMap(Collection::stream).collect(Collectors.toList());

                    startAlert(view, products);

                    List<String> neweggLinks = products.stream()
                            .filter(Predicate.not(visitedProducts::contains))
                            .filter(p -> StringUtils.containsIgnoreCase(p.getUrl(), "newegg"))
                            .map(Product::getUrl)
                            .collect(Collectors.toList());

                    visitedProducts.addAll(products);

                    triggerBuyer(neweggLinks);

                } else {
                    scrape3080 = scraper.scrape(Arrays.asList(urls.get(0), urls.get(5)), true);

                    scrape6800xt = scraper.scrape(Arrays.asList(urls.get(1), urls.get(6)), true);

                    scrape3070 = scraper.scrape(Arrays.asList(urls.get(2), urls.get(7)), true);

                    scrapeCpu = scraper.scrape(Arrays.asList(urls.get(3), urls.get(8), urls.get(13)), true);

                    scrapePs5 = scraper.scrape(Arrays.asList(urls.get(4), urls.get(9), urls.get(14)), true);
                }
                view.set3080Content(scrape3080);
                view.set6800xtContent(scrape6800xt);
                view.set3070Content(scrape3070);
                view.setCpuContent(scrapeCpu);
                view.setPs5Content(scrapePs5);
            }
        } catch (Exception e) {
            logger.warning("Main Error detected: " + e.toString() + "\r\n         Stacktrace: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private static void triggerBuyer(List<String> neweggLinks) {
        neweggLinks.forEach(url ->
                new Thread(() -> new NeweggEngine().start(url)).start()
        );
    }

    private static void startAlert(View view, List<Product> scrapeResult) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        for (Product product : scrapeResult) {
            logger.info(product.getName() + " IN STOCK\r\n      " + product.getUrl());
        }
        if (!view.muteSound && !scrapeResult.isEmpty()) {
            bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(Runner.class.getClassLoader().getResourceAsStream("alert.wav")));
            audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

        }
    }

    public static void closer() {
        try {
            fh.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
            if (audioInputStream != null)
                audioInputStream.close();
        } catch (IOException e) {
            // Do nothing
        }
    }
}
