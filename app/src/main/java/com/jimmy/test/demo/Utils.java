package com.jimmy.test.demo;

public class Utils {
    public static boolean isCNSku() {
        return "cn".equals(System.getProperty("sku"));
    }
}
