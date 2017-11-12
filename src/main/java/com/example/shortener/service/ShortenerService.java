package com.example.shortener.service;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shortener.exception.NotFoundShortUrlKeyException;
import com.example.shortener.repository.ShortenerRepository;

@Service
public class ShortenerService {
    private static final Pattern HTTP_PATTERN = Pattern.compile("^(http|https)://");
    private static final Pattern WWW_PATTERN = Pattern.compile("www.");

    @Autowired
    private ShortenerRepository shortenerRepository;

    private static final String PREFIX = "http://www.";
    private static final String MY_HOST = "http://localhost:8080/";
    private static final String BASE_64;

    static {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder base64 = new StringBuilder(alphabet)
                .append(alphabet.toUpperCase());
        IntStream.range(0, 10).forEach(base64::append);
        base64.append("-_");

        BASE_64 = base64.toString();
    }

    public String shorten(String url) {
        String longUrl = convertToKeyUrl(url);
        Optional<String> oldUrl = shortenerRepository.getIfPresentFromLong(longUrl);
        if (oldUrl.isPresent()) {
            return MY_HOST + oldUrl.get();
        }
        String shortUrl = createShortUrlKey(shortenerRepository.getSeed());
        return MY_HOST + shortenerRepository.put(longUrl, shortUrl);
    }

    private String convertToKeyUrl(String originalUrl) {
        String converted = WWW_PATTERN.matcher(HTTP_PATTERN.matcher(originalUrl).replaceAll("")).replaceAll("");
        if (converted.endsWith("/")) {
            return converted.substring(0, converted.length() - 2);
        }
        return converted;
    }

    private String createShortUrlKey(long currentIndexSeed) {
        StringBuilder shortened = new StringBuilder();
        while (currentIndexSeed > 0) {
            shortened.append(BASE_64.charAt((int) (currentIndexSeed % 64)));
            currentIndexSeed /= 64;
        }
        return shortened.toString();
    }

    public String tryFindLongUrl(String shortUrlKey) {
        Optional<String> ifPresentFromShort = shortenerRepository.getIfPresentFromShort(shortUrlKey);
        return PREFIX + ifPresentFromShort.orElseThrow(() -> new NotFoundShortUrlKeyException(
                "There is no long url matching the short url key [" + shortUrlKey + "]. Try making new one!"));
    }
}
