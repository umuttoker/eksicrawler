package com.hasssektor.controller;

import com.hasssektor.handler.CrawlerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by umut on 3.08.2017.
 */

@RestController
public class CrawlController {

    @Autowired
    CrawlerHandler crawlerHandler;

    @RequestMapping("/crawlUsers")
    String home() {
        crawlerHandler.crawlUsers("cutta");
        return "Success";
    }
}
