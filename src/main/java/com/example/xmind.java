package com.example;

import java.io.IOException;

import org.jsoup.Jsoup;

public class xmind {
	public boolean checkSignatureValid() {
		return true;
	}
	
	public static void main(String[] args) {
		try {
		 System.out.println(Jsoup.connect("https://www.baidu.com").get());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
