package com.wjk.tutuo1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUrlMatcher {
    public static void main(String[] args) {
        String input = "<img src=\"https://1kunkun1.oss-cn-hangzhou.aliyuncs.com/e8032a76-aab4-4298-90bf-0bfbcc88e947.png\">";
        String regex = "<img\\s+src\\s*=\\s*\"([^\"]+)\"";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String imageUrl = matcher.group(1);
            System.out.println("Image URL: " + imageUrl);
        } else {
            System.out.println("No image URL found");
        }
    }
}
