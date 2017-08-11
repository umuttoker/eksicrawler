package com.hasssektor.controller;

import com.hasssektor.handler.CrawlerHandler;
import com.hasssektor.handler.InstagramCrawler;
import com.hasssektor.handler.TwitterCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * Created by umut on 3.08.2017.
 */

@RestController
public class CrawlController {

    Logger LOGGER = LoggerFactory.getLogger(CrawlController.class);

    @Autowired
    CrawlerHandler crawlerHandler;

    @Autowired
    InstagramCrawler instagramcrawler;

    @Autowired
    TwitterCrawler twitterCrawler;

    @RequestMapping("/startcrawling/{topicId}")
    String startCrawling(@PathVariable("topicId") String topicId) {
        crawlerHandler.startCrawling(topicId);
        return "Success";
    }

    @RequestMapping("/setEntryCounts")
    String updateUserData() {
        crawlerHandler.updateUserData();
        return "Success";
    }

    @RequestMapping("/crawlintagraminfo")
    String crawlInstaData() {
        instagramcrawler.crawlUserData();
        return "Success";
    }

    @RequestMapping("/crawltwitterinfo")
    String crawlTwitterData() {
        twitterCrawler.crawlUserData();
        return "Success";
    }
}
