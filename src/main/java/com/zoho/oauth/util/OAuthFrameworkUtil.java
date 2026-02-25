package com.zoho.oauth.util;

public final class OAuthFrameworkUtil {

    private OAuthFrameworkUtil() { }

    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) return input;
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = true;
        for (char c : input.toCharArray()) {
            if (c == '_' || c == '-' || c == '/' || c == ' ') {
                nextUpper = true;
                continue;
            }
            if (nextUpper) {
                sb.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toSubPackage(String input) {
        if (input == null) return "";
        return input.replace('/', '.').replace('-', '.').replace(' ', '.');
    }

    public static String normalise(String input) {
        if (input == null) return "";
        return input.toLowerCase().replace("-", "").replace("_", "");
    }
}