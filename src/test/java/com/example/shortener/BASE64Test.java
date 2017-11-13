package com.example.shortener;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class BASE64Test {

	@Test
	public void contextLoads1() {
		System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(100000).getBytes()));
		System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(999999).getBytes()));
		System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(1000000).getBytes()));
	}

}
