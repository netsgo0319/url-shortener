package com.example.shortener.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class ShortenerHashRepository {
    private static final Map<String, String> LONG_SHORT_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> SHORT_LONG_MAP = new ConcurrentHashMap<>();

    public Optional<String> getIfPresentFromLong(String longUrl) {
        return Optional.ofNullable(LONG_SHORT_MAP.get(longUrl));
    }
    public Optional<String> getIfPresentFromShort(String shortUrl) {
        return Optional.ofNullable(SHORT_LONG_MAP.get(shortUrl));
    }

    public String put(String longUrl, String shortUrl) {
        LONG_SHORT_MAP.put(longUrl, shortUrl);
        SHORT_LONG_MAP.put(shortUrl, longUrl);
        return shortUrl;
    }

}
