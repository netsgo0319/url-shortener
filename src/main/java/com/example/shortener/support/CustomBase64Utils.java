package com.example.shortener.support;

import java.util.stream.IntStream;

public class CustomBase64Utils {
    private static final String BASE_64;

    static {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder base64 = new StringBuilder(alphabet)
                .append(alphabet.toUpperCase());
        IntStream.range(0, 10).forEach(base64::append);
        base64.append("-_");

        BASE_64 = base64.toString();
    }

    private String createShortUrlKey(long currentIndexSeed) {
        StringBuilder shortened = new StringBuilder();
        while (currentIndexSeed > 0) {
            shortened.append(BASE_64.charAt((int) (currentIndexSeed % 64)));
            currentIndexSeed /= 64;
        }
        return shortened.toString();
    }

    private long reverseShortUrlKey(String urlKey) {
        long index = 0;
        long power = 0;
        char[] chars = urlKey.toCharArray();
        for (int i = chars.length - 1 ; i >= 0 ; i--) {
            index += BASE_64.indexOf(chars[i]) * Math.pow(64, power);
            power++;
        }
        return index;
    }
}
