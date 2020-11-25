package com.pulu.scraper;

import com.pulu.scraper.model.Product;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View {

    public boolean inStock = false;
    public boolean muteSound = false;

    private List<Product> data3080 = new ArrayList<>();
    private List<Product> data6800xt = new ArrayList<>();
    private List<Product> data3070 = new ArrayList<>();
    private List<Product> dataCpu = new ArrayList<>();
    private List<Product> dataPS5 = new ArrayList<>();

    JFrame frame = new JFrame();
    private final UrlTextPane pane3080 = new UrlTextPane();
    private final UrlTextPane pane6800xt = new UrlTextPane();
    private final UrlTextPane pane3070 = new UrlTextPane();
    private final UrlTextPane paneCpu = new UrlTextPane();
    private final UrlTextPane panePS5 = new UrlTextPane();

    private final JTextField urlNewegg3080 = new JTextField("https://www.newegg.com/p/pl?d=rtx+3080&N=100007709%20601357282%204021&isdeptsrh=1");
    private final JTextField urlNewegg6800xt = new JTextField("https://www.newegg.com/p/pl?d=radeon+6800+XT&N=100007709&isdeptsrh=1");
    private final JTextField urlNewegg3070 = new JTextField("https://www.newegg.com/p/pl?d=rtx+3070&N=100007709&isdeptsrh=1&LeftPriceRange=0+650");
    private final JTextField urlNeweggCpu = new JTextField("https://www.newegg.com/p/pl?d=ryzen+5000&N=50001028%20601301117%20100007671");
    private final JTextField urlNeweggPS5 = new JTextField("https://www.newegg.com/p/pl?d=playstation+5&N=101696840&isdeptsrh=1");

    private final JTextField urlBestbuy3080 = new JTextField("https://www.bestbuy.com/site/searchpage.jsp?st=3080+rtx");
    private final JTextField urlBestbuy6800xt = new JTextField("https://www.bestbuy.com/site/searchpage.jsp?_dyncharset=UTF-8&id=pcat17071&iht=y&keys=keys&ks=960&list=n&qp=category_facet%3DGPUs%20%2F%20Video%20Graphics%20Cards~abcat0507002&sc=Global&st=radeon%206800%20xt&type=page&usc=All%20Categories");
    private final JTextField urlBestbuy3070 = new JTextField("https://www.bestbuy.com/site/searchpage.jsp?st=3070+rtx");
    private final JTextField urlBestbuyCpu = new JTextField("https://www.bestbuy.com/site/searchpage.jsp?_dyncharset=UTF-8&id=pcat17071&iht=y&keys=keys&ks=960&list=n&qp=brand_facet%3DBrand~AMD&sc=Global&st=amd%20ryzen%205900&type=page&usc=All%20Categories");
    private final JTextField urlBestbuyPS5 = new JTextField("https://www.bestbuy.com/site/playstation-5/ps5-consoles/pcmcat1587395025973.c?id=pcmcat1587395025973");

    private final JTextField urlBhPhoto3080 = new JTextField();
    private final JTextField urlBhPhoto6800xt = new JTextField();
    private final JTextField urlBhPhoto3070 = new JTextField();
    private final JTextField urlBhPhotoCpu = new JTextField("https://www.bhphotovideo.com/c/search?q=ryzen%205000&filters=fct_category%3Acpus_19865%2Cfct_price%3A500..550");
    private final JTextField urlBhPhotoPs5 = new JTextField("https://www.bhphotovideo.com/c/search?q=playstation%205&filters=fct_category%3Aplaystation_5_consoles_48557");

    private final JButton enableScrape = new JButton("In/Out Of Stock");
    private final JButton enableMute = new JButton("Mute Sound");

    public View() {

        JPanel panel3080 = new JPanel();
        JPanel panel6800xt = new JPanel();
        JPanel panel3070 = new JPanel();
        JPanel panelCpu = new JPanel();
        JPanel panelPs5 = new JPanel();

        Timer timer = new Timer(1000, e -> {
            pane3080.setText(convertText(data3080));
            pane6800xt.setText(convertText(data6800xt));
            pane3070.setText(convertText(data3070));
            paneCpu.setText(convertText(dataCpu));
            panePS5.setText(convertText(dataPS5));
        });
        timer.start();

        enableScrape.setBackground(Color.RED);
        enableScrape.addActionListener(arg0 -> {
            if (inStock) {
                inStock = false;
                enableScrape.setBackground(Color.RED);
            } else {
                inStock = true;
                enableScrape.setBackground(Color.GREEN);
            }
            setUrls(urlNewegg3080.getText(), urlNewegg6800xt.getText(), urlNewegg3070.getText(), urlNeweggCpu.getText(), urlNeweggPS5.getText(),
                    urlBestbuy3080.getText(), urlBestbuy6800xt.getText(), urlBestbuy3070.getText(), urlBestbuyCpu.getText(), urlBestbuyPS5.getText(),
                    urlBhPhoto3080.getText(), urlBhPhoto6800xt.getText(), urlBhPhoto3070.getText(), urlBhPhotoCpu.getText(), urlBhPhotoPs5.getText());
        });

        enableMute.setBackground(Color.RED);
        enableMute.addActionListener(arg0 -> {
            if (muteSound) {
                muteSound = false;
                enableMute.setBackground(Color.RED);
            } else {
                muteSound = true;
                enableMute.setBackground(Color.GREEN);
            }
        });

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new GridLayout(1, 5));
        mainContainer.add(new JScrollPane(setUpPanel(panel3080, pane3080, "Nvidia RTX 3080 Tracker", "RTX 3080")));
        mainContainer.add(new JScrollPane(setUpPanel(panel6800xt, pane6800xt, "AMD Radeon 6800XT Tracker", "Radeon 6800XT")));
        mainContainer.add(new JScrollPane(setUpPanel(panel3070, pane3070, "Nvidia RTX 3070 Tracker", "RTX 3070")));
        mainContainer.add(new JScrollPane(setUpPanel(panelCpu, paneCpu, "AMD Ryzen 5000 Series Tracker", "Ryzen 5000 Series")));
        mainContainer.add(new JScrollPane(setUpPanel(panelPs5, panePS5, "Playstation 5 Tracker", "Playstation 5 Console")));
        mainContainer.setPreferredSize(new Dimension(1900, 800));

        JPanel urlContainer = new JPanel();
        urlContainer.setLayout(new GridLayout(4, 5));
        urlContainer.setPreferredSize(new Dimension(1880, 110));
        urlContainer.add(new JLabel());
        urlContainer.add(new JLabel("Geforce RTX 3080 URLS"));
        urlContainer.add(new JLabel("Radeon 6800XT URLS"));
        urlContainer.add(new JLabel("Geforce RTX 3070 URLS"));
        urlContainer.add(new JLabel("AMD Ryzen 5000 Series CPU URLS"));
        urlContainer.add(new JLabel("Playstation 5 URLS"));
        JLabel neweggLabel = new JLabel("Newegg");
        urlContainer.add(neweggLabel);
        urlContainer.add(urlNewegg3080);
        urlContainer.add(urlNewegg6800xt);
        urlContainer.add(urlNewegg3070);
        urlContainer.add(urlNeweggCpu);
        urlContainer.add(urlNeweggPS5);
        JLabel bestbuyLabel = new JLabel("Bestbuy");
        urlContainer.add(bestbuyLabel);
        urlContainer.add(urlBestbuy3080);
        urlContainer.add(urlBestbuy6800xt);
        urlContainer.add(urlBestbuy3070);
        urlContainer.add(urlBestbuyCpu);
        urlContainer.add(urlBestbuyPS5);
        JLabel bhBhotoLabel = new JLabel("BH Photo Video");
        urlContainer.add(bhBhotoLabel);
        urlContainer.add(urlBhPhoto3080);
        urlContainer.add(urlBhPhoto6800xt);
        urlContainer.add(urlBhPhoto3070);
        urlContainer.add(urlBhPhotoCpu);
        urlContainer.add(urlBhPhotoPs5);


        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new GridLayout(1, 2));
        buttonContainer.add(enableScrape);
        buttonContainer.add(enableMute);
        buttonContainer.setPreferredSize(new Dimension(1880, 50));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1920, 1020));
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.add(mainContainer, BorderLayout.NORTH);
        frame.add(urlContainer, BorderLayout.SOUTH);
        frame.add(buttonContainer, BorderLayout.SOUTH);
        frame.setTitle("NE and BB Tracker");
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Runner.closer();
                System.exit(0);
            }
        });
    }

    public void set3080Content(List<Product> data) {
        data3080 = data;
    }

    public void set6800xtContent(List<Product> data) {
        data6800xt = data;
    }

    public void set3070Content(List<Product> data) {
        data3070 = data;
    }

    public void setCpuContent(List<Product> data) {
        dataCpu = data;
    }

    public void setPs5Content(List<Product> data) {
        dataPS5 = data;
    }

    public void setUrls(String... urls) {
        urlNewegg3080.setText(urls[0]);
        urlNewegg6800xt.setText(urls[1]);
        urlNewegg3070.setText(urls[2]);
        urlNeweggCpu.setText(urls[3]);
        urlNeweggPS5.setText(urls[4]);
        urlBestbuy3080.setText(urls[5]);
        urlBestbuy6800xt.setText(urls[6]);
        urlBestbuy3070.setText(urls[7]);
        urlBestbuyCpu.setText(urls[8]);
        urlBestbuyPS5.setText(urls[9]);
        urlBhPhoto3080.setText(urls[10]);
        urlBhPhoto6800xt.setText(urls[11]);
        urlBhPhoto3070.setText(urls[12]);
        urlBhPhotoCpu.setText(urls[13]);
        urlBhPhotoPs5.setText(urls[14]);
    }

    public List<String> getUrls() {
        List<String> ret = new ArrayList<>();
        ret.add(urlNewegg3080.getText());
        ret.add(urlNewegg6800xt.getText());
        ret.add(urlNewegg3070.getText());
        ret.add(urlNeweggCpu.getText());
        ret.add(urlNeweggPS5.getText());
        ret.add(urlBestbuy3080.getText());
        ret.add(urlBestbuy6800xt.getText());
        ret.add(urlBestbuy3070.getText());
        ret.add(urlBestbuyCpu.getText());
        ret.add(urlBestbuyPS5.getText());
        ret.add(urlBhPhoto3080.getText());
        ret.add(urlBhPhoto6800xt.getText());
        ret.add(urlBhPhoto3070.getText());
        ret.add(urlBhPhotoCpu.getText());
        ret.add(urlBhPhotoPs5.getText());
        return ret;
    }

    private Component setUpPanel(JPanel panel, UrlTextPane urlTextPane, String title, String subtitle) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleColor(Color.BLACK);

        panel.setBorder(titledBorder); //Set title to the Panel
        panel.setLayout(new BorderLayout());
        urlTextPane.setBorder(BorderFactory.createTitledBorder(subtitle));
        panel.add(urlTextPane, BorderLayout.NORTH);

        return panel;
    }

    private String convertText(List<Product> data) {

        if (data.isEmpty()) {
            return "";
        }

        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern urlPattern = Pattern.compile(regex);

        StringBuilder answer = new StringBuilder();
        answer.append("<html><body>");

        for (Product product : data) {
            String content = StringEscapeUtils.escapeHtml4(product.getUrl()).replace(" ", "%20");
            int lastIndex = 0;
            Matcher matcher = urlPattern.matcher(content);
            while (matcher.find()) {
                //Append everything since last update to the url:
                answer.append(content, lastIndex, matcher.start());
                String url = content.substring(matcher.start(), matcher.end()).trim();
                answer.append("<a href=\"" + url + "\">" + product.getName() + "</a>");
                lastIndex = matcher.end();
            }
            answer.append(content.substring(lastIndex));
            answer.append("<br><br>");
        }
        //Append end:
        return answer.toString();
    }

    private static class UrlTextPane extends JTextPane {

        public UrlTextPane() {
            this.setEditable(false);
            this.addHyperlinkListener(new UrlHyperlinkListener());
            this.setContentType("text/html");
        }

        private static class UrlHyperlinkListener implements HyperlinkListener {
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(event.getURL().toURI());
                    } catch (IOException | URISyntaxException e) {
                        throw new RuntimeException("Can't open URL", e);
                    }
                }
            }
        }
    }
}

