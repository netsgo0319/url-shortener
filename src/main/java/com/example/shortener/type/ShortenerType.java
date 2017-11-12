package com.example.shortener.type;

import com.example.shortener.service.ShortenerArrayService;
import com.example.shortener.service.ShortenerHashService;
import com.example.shortener.service.ShortenerMapService;
import com.example.shortener.service.ShortenerService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ShortenerType {
    MAP(ShortenerMapService.class),
    ARRAY(ShortenerArrayService.class),
    HASH(ShortenerHashService.class),
    ;

    public Class<? extends ShortenerService> serviceClass;
}
