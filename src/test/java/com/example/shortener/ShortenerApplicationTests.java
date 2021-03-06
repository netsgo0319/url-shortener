package com.example.shortener;

import java.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShortenerApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(100000).getBytes()));
		System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(1000000).getBytes()));
		System.out.println(Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(10000000).getBytes()));
	}

}
