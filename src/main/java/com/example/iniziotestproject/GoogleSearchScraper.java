package com.example.iniziotestproject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GoogleSearchScraper {
    public List<String> googleScraper(String keyword) {
        List<String> titles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://www.google.com/search?q=" + keyword).get();
            Elements searchResults = doc.select("h3");
            for (Element searchResult : searchResults) {
                titles.add(searchResult.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }
}