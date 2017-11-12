package com.example.shortener.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shortener.exception.NotFoundShortUrlKeyException;
import com.example.shortener.repository.ShortenerArrayRepository;
import com.example.shortener.type.ShortenerType;

@Service
public class ShortenerArrayService implements ShortenerService {
    @Autowired
    private ShortenerArrayRepository shortenerRepository;

    @Override
    public ShortenerType getShortenerType() {
        return ShortenerType.ARRAY;
    }

    public String shorten(String url) {
        Optional<String> oldUrl = shortenerRepository.getIfPresentFromLong(url);
        return oldUrl.orElseGet(() -> {
            synchronized (ShortenerArrayService.class) {
                return createShortUrlKey(shortenerRepository.getSeed());
            }
        });
    }

    private String createShortUrlKey(int currentIndexSeed) {
        return Base64.getUrlEncoder().encodeToString(String.valueOf(currentIndexSeed).getBytes());
    }

    private int decodeUrl(String shortUrlKey) {
        return Integer.valueOf(new String(Base64.getUrlDecoder().decode(shortUrlKey.getBytes())));
    }

    public String tryFindLongUrl(String shortUrlKey) {
        Optional<String> ifPresentFromShort = shortenerRepository.getIfPresentFromShort(decodeUrl(shortUrlKey));
        return ifPresentFromShort.orElseThrow(() -> new NotFoundShortUrlKeyException(
                "There is no long url matching the short url key [" + shortUrlKey + "]. Try making new one!"));
    }
}
