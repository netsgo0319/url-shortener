package com.example.shortener.service;

import java.util.EnumMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.shortener.type.ShortenerType;

@Service
public class ShortenerCompositeService {
    private static final Pattern HTTP_PATTERN = Pattern.compile("^(http|https)://");
    private static final Pattern WWW_PATTERN = Pattern.compile("www.");
    private static final String PREFIX = "http://www.";
    private static final String MY_HOST = "http://localhost:8080/";
    private static final Pattern BASE_64_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1,8}$");

    @Autowired
    private List<ShortenerService> shortenerServiceList;

    private final EnumMap<ShortenerType, ShortenerService> SHORTENER_SERVICE_MAP = new EnumMap<>(ShortenerType.class);

    @PostConstruct
    public void init() {
        shortenerServiceList.forEach(shortenerService -> SHORTENER_SERVICE_MAP.put(shortenerService.getShortenerType(), shortenerService));
    }

    public String shorten(ShortenerType type, String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("Cannot shorten empty url");
        }
        return MY_HOST + SHORTENER_SERVICE_MAP.get(type).shorten(convertToKeyUrl(url));
    }

    private String convertToKeyUrl(String originalUrl) {
        String converted = WWW_PATTERN.matcher(HTTP_PATTERN.matcher(originalUrl).replaceAll("")).replaceAll("");
        if (converted.endsWith("/")) {
            return converted.substring(0, converted.length() - 2);
        }
        return converted;
    }

    public String tryFindLongUrl(ShortenerType type, String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("Cannot redirect with empty short url");
        }
        if (BASE_64_PATTERN.matcher(url).matches()) {
            return PREFIX + SHORTENER_SERVICE_MAP.get(type).tryFindLongUrl(url);
        }
        throw new IllegalArgumentException("Invalid short url.");
    }
}
