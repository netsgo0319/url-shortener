package com.example.shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shortener.exception.NotFoundShortUrlKeyException;
import com.example.shortener.service.ShortenerService;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ShortenerController {

    @Autowired
    private ShortenerService shortenerService;

    @RequestMapping(value = "/", method= RequestMethod.GET)
    public String shortener() {
        return "shortenerService";
    }

    @RequestMapping(value = "/shorten/", method= RequestMethod.POST)
    @ResponseBody
    public String shortenMap(@RequestBody JsonNode node) {
        String urlString = node.get("longUrl").asText();
        return shortenerService.shorten(urlString);
    }

    @RequestMapping(value = "/{shortUrlKey}", method= RequestMethod.GET)
    public String redirect(@PathVariable("shortUrlKey") String shortUrlKey, Model model) {
        try {
            return "redirect:" + shortenerService.tryFindLongUrl(shortUrlKey);
        } catch (NotFoundShortUrlKeyException e) {
            log.error(e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "shortenerService";
        }
    }
}
