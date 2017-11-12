package com.example.shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.shortener.exception.NotFoundShortUrlKeyException;
import com.example.shortener.service.ShortenerCompositeService;
import com.example.shortener.type.ShortenerType;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ShortenerController {

    @Autowired
    private ShortenerCompositeService shortenerService;

    @RequestMapping(value = "/", method= RequestMethod.GET)
    public String shortener() {
        return "shortenerService";
    }

    @RequestMapping(value = "/{type}/shorten", method= RequestMethod.POST)
    @ResponseBody
    public String shortenMap(@PathVariable("type") ShortenerType type, @RequestBody JsonNode node) {
        String urlString = node.get("longUrl").asText().trim();
        return shortenerService.shorten(type, urlString);
    }

    @RequestMapping(value = "/{type}/{shortUrlKey}", method= RequestMethod.GET)
    public String redirect(@PathVariable("type") ShortenerType type,
                           @PathVariable("shortUrlKey") String shortUrlKey, Model model) {
        return "redirect:" + shortenerService.tryFindLongUrl(type, shortUrlKey);
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView databaseError(RuntimeException e) {
        ModelAndView model = new ModelAndView();
        model.addObject("errorMessage", e.getMessage());
        model.setViewName("shortenerService");
        return model;
    }
}
