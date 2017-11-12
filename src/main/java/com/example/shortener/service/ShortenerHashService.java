package com.example.shortener.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shortener.exception.NotFoundShortUrlKeyException;
import com.example.shortener.exception.UnshortenableUrlException;
import com.example.shortener.repository.ShortenerHashRepository;
import com.example.shortener.type.ShortenerType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShortenerHashService implements ShortenerService {
    @Autowired
    private ShortenerHashRepository shortenerRepository;

    @Override
    public ShortenerType getShortenerType() {
        return ShortenerType.HASH;
    }

    public String shorten(String url) {
        return shorten(url, url, 50);
    }

    private String shorten(String originalUrl, String changingUrl, int retry) {
        if (retry == 0) {
            throw new UnshortenableUrlException("Cannot short this url. Already tried more than retry count");
        }
        Optional<String> oldUrl = shortenerRepository.getIfPresentFromLong(changingUrl);
        return oldUrl.orElseGet(() -> {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                log.error(e.getMessage(), e);
                return "Cannot shorten because of unknown error.";
            }
            byte[] encodedhash = digest.digest(changingUrl.getBytes(StandardCharsets.UTF_8));
            log.debug("Encoded : " + Base64.getUrlEncoder().withoutPadding().encodeToString(encodedhash));
            String shortUrl = Base64.getUrlEncoder().withoutPadding().encodeToString(encodedhash).substring(0, 8);
            if (shortenerRepository.getIfPresentFromShort(shortUrl).isPresent()) {
                return shorten(originalUrl, changingUrl + retry, retry - 1);
            }
            shortenerRepository.put(originalUrl, shortUrl);
            return shortUrl;
        });
    }

    public String tryFindLongUrl(String shortUrlKey) {
        Optional<String> ifPresentFromShort = shortenerRepository.getIfPresentFromShort(shortUrlKey);
        return ifPresentFromShort.orElseThrow(() -> new NotFoundShortUrlKeyException(
                "There is no long url matching the short url key [" + shortUrlKey + "]. Try making new one!"));
    }
}
