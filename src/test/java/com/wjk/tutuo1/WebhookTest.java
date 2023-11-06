package com.wjk.tutuo1;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebhookTest {

    public static void main(String[] args) {
        String webhookUrl = "https://54bf8f1b.r8.cpolar.top/hook"; // 替换成你的webhook URL

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(webhookUrl)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println("Webhook源地址: " + responseBody);
            } else {
                System.out.println("请求失败: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
