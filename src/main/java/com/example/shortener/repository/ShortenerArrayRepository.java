package com.example.shortener.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class ShortenerArrayRepository {
    private static final Map<String, String> LONG_SHORT_MAP = new ConcurrentHashMap<>();
    private static final List<String> LONG_URL_LIST = Collections.synchronizedList(new ArrayList<>());

    public Optional<String> getIfPresentFromLong(String longUrl) {
        return Optional.ofNullable(LONG_SHORT_MAP.get(longUrl));
    }
    public Optional<String> getIfPresentFromShort(int index) {
        return LONG_URL_LIST.size() > index ? Optional.of(LONG_URL_LIST.get(index)) : Optional.empty();
    }

    public int getSeed() {
        return LONG_URL_LIST.size();
    }

    public String put(int index, String longUrl, String shortUrl) {
        if (index < LONG_URL_LIST.size()) {
            throw new IllegalArgumentException("Cannot add [" + shortUrl + "] in " + index + " of array. Already occupied.");
        }
        LONG_SHORT_MAP.put(longUrl, shortUrl);
        LONG_URL_LIST.add(index, longUrl);
        return shortUrl;
    }
}
