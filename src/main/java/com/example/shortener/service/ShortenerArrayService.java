package com.example.shortener.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shortener.exception.NotFoundShortUrlKeyException;
import com.example.shortener.repository.ShortenerArrayRepository;
import com.example.shortener.type.ShortenerType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                int index = shortenerRepository.getSeed();
                String shortUrlKey = createShortUrlKey(index);
                shortenerRepository.put(index, url, shortUrlKey);
                return shortUrlKey;
            }
        });
    }

    private String createShortUrlKey(int currentIndexSeed) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(currentIndexSeed).getBytes());
    }

    private int decodeUrl(String shortUrlKey) {
        if (shortUrlKey.length() == 1) {
            throw new NotFoundShortUrlKeyException("Cannot parse short url " + shortUrlKey + " for type " + getShortenerType());
        }
        String encoded = shortUrlKey;
        if (shortUrlKey.length() % 4 == 2) {
            encoded += "==";
        } else if(shortUrlKey.length() % 4 == 3) {
            encoded += "=";
        }
        try {
            return Integer.valueOf(new String(Base64.getUrlDecoder().decode(encoded.getBytes())));
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            throw new NotFoundShortUrlKeyException("Cannot parse short url " + shortUrlKey + " for type " + getShortenerType());
        }
    }

    public String tryFindLongUrl(String shortUrlKey) {
        Optional<String> ifPresentFromShort = shortenerRepository.getIfPresentFromShort(decodeUrl(shortUrlKey));
        return ifPresentFromShort.orElseThrow(() -> new NotFoundShortUrlKeyException(
                "There is no long url matching the short url key [" + shortUrlKey + "]. Try making new one!"));
    }
}
