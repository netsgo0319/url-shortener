package com.example.shortener.service;

import com.example.shortener.type.ShortenerType;

public interface ShortenerService {
    ShortenerType getShortenerType();
    String shorten(String longUrl);
    String tryFindLongUrl(String shortUrl);
}
