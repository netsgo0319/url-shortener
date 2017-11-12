package com.example.shortener.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shortener.exception.NotFoundShortUrlKeyException;
import com.example.shortener.repository.ShortenerMapRepository;
import com.example.shortener.type.ShortenerType;

@Service
public class ShortenerMapService implements ShortenerService {
    @Autowired
    private ShortenerMapRepository shortenerRepository;

    @Override
    public ShortenerType getShortenerType() {
        return ShortenerType.MAP;
    }

    public String shorten(String url) {
        return shortenerRepository.getIfPresentFromLong(url)
                                  .orElseGet(() -> {
                                      String shortUrlKey = createShortUrlKey(shortenerRepository.getSeed());
                                      shortenerRepository.put(url, shortUrlKey);
                                      return shortUrlKey;
                                  });
    }

    private String createShortUrlKey(long currentIndexSeed) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(currentIndexSeed).getBytes());
    }

    public String tryFindLongUrl(String shortUrlKey) {
        Optional<String> ifPresentFromShort = shortenerRepository.getIfPresentFromShort(shortUrlKey);
        return ifPresentFromShort.orElseThrow(() -> new NotFoundShortUrlKeyException(
                "There is no long url matching the short url key [" + shortUrlKey + "]. Try making new one!"));
    }
}
