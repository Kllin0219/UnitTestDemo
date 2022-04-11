package com.jimmy.test.demo;

public class Utils {
    public static boolean isCNSku() {
        return "cn".equals(System.getProperty("sku"));
    }

    public static int dpToPx(int dp) {
        return dp / 10;
    }
}
