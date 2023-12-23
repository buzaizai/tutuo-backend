package com.wjk.tutuo1.utils;


import cn.hutool.core.convert.ConvertException;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Astar
 * ClassName:OpenAIAPI.java
 * date:2023-03-03 16:49
 * Description:
 */
@Component
public class OpenAiUtils{
    @Autowired
    private AliOSSUtils aliOSSUtils;
    /**
     * 聊天端点
     */
    String chatEndpoint = "https://api.openai.com/v1/chat/completions";
    /**
     * api密匙
     */
     String apiKey = "GUESS";

    public  String chat(String txt, String history) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", "gpt-3.5-turbo-16k");
        List<Map<String, String>> dataList = new ArrayList<>();
        if (history == null) {
            dataList.add(new HashMap<String, String>() {{
                put("role", "user");
                put("content", txt);
            }});
            paramMap.put("messages", dataList);
        }else {
            dataList = new ArrayList<>();
            dataList.add(new HashMap<String, String>(){{
                put("role", "user");
                put("content", "帮我解析成json格式的数据，图片只要url，除去html标签，只回答json格式文本的内容，不用回答其它内容:\njson格式如下：\n{\n" +
                        "    \"image\":\"\",\n" +
                        "  \"basic_info\": \"\",\n" +
                        "  \"chart_attributes\": {\n" +
                        "    \"shape\": \"\",\n" +
                        "    \"chart_type\": \"\",\n" +
                        "    \"function\": \"\"\n" +
                        "  },\n" +
                        "  \"chart_analysis\": {\n" +
                        "    \"element_construction\": \"\",\n" +
                        "    \"element_construction_chart\": \"\",\n" +
                        "    \"applicable_scenarios\": \"\",\n" +
                        "    \"inapplicable_scenarios\": \"\",\n" +
                        "  }\n" +
                        "  \"drawing\": {\n" +
                        "    \"description\": \"\",\n" +
                        "    \"data_structure_description\": \"\",\n" +
                        "    \"data_structure_example\": \"\",\n" +
                        "      \"mermaid_code\":\"\",\n" +
                        "      \"mermain_image\":\"\",\n" +
                        "  \"rendering_data\":\"\",\n" +
                        "  \"extended_data\": \"\"\n" +
                        "  }\n" +
                        "}"+ txt);
            }});
            dataList.add(new HashMap<String, String>() {{
                put("role", "assistant");
                put("content", history);
            }});
            dataList.add(new HashMap<String, String>(){{
                put("role", "user");
                put("content", "紧接着上一次没回答完的内容继续");
            }});
            paramMap.put("messages",dataList);
//            System.out.println(paramMap);
        }
        cn.hutool.json.JSONObject message = null;
        try {
            String body = HttpRequest.post(chatEndpoint)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(JSON.toJSONString(paramMap))
                    .execute()
                    .body();
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(body);
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject result = choices.get(0, JSONObject.class, Boolean.TRUE);
            message = result.getJSONObject("message");
        } catch (HttpException | ConvertException e) {
            return "出现了异常";
        }
        return message.getStr("content");
    }


    public org.json.JSONObject requestJson(String input) {
        String answer = chat("帮我解析成json格式的数据，图片只要url，除去html标签，只回答json格式文本的内容，不用回答其它内容:\njson格式如下：\n{\n" +
                "    \"image\":\"\",\n" +
                "  \"basic_info\": \"\",\n" +
                "  \"chart_attributes\": {\n" +
                "    \"shape\": \"\",\n" +
                "    \"chart_type\": \"\",\n" +
                "    \"function\": \"\"\n" +
                "  },\n" +
                "  \"chart_analysis\": {\n" +
                "    \"element_construction\": \"\",\n" +
                "    \"element_construction_image\": \"\",\n" +
                "    \"applicable_scenarios\": \"\",\n" +
                "    \"inapplicable_scenarios\": \"\",\n" +
                "  }\n" +
                "  \"drawing\": {\n" +
                "    \"description\": \"\",\n" +
                "    \"data_structure_description\": \"\",\n" +
                "    \"data_structure_example\": \"\",\n" +
                "      \"mermaid_code\":\"\",\n" +
                "      \"mermaid_image\":\"\",\n" +
                "  \"rendering_data\":\"\",\n" +
                "  \"extended_data\": \"\"\n" +
                "  }\n" +
                "}" + input,null);
//        System.out.println(answer);
        if(answer.charAt(answer.length()-1)!='}'){
            String answerRemain = chat(input,answer);
//            System.out.println(answerRemain);
            answer+=answerRemain;
        }
        System.out.println(answer);
        org.json.JSONObject jsonObject = new org.json.JSONObject(answer);
        jsonObject.put("image",uploadImg(jsonObject.getString("image")))  ;
        org.json.JSONObject chart_analysis = jsonObject.getJSONObject("chart_analysis");
        chart_analysis.put("element_construction_image", uploadImg(chart_analysis.getString("element_construction_image")));
        jsonObject.put("chart_analysis", chart_analysis);
        org.json.JSONObject drawing = jsonObject.getJSONObject("drawing");
        drawing.put("mermaid_image",uploadImg( drawing.getString("mermaid_image")));
        jsonObject.put("mermaid_image", chart_analysis);
        return jsonObject;
    }
    private String uploadImg(String url) {
        String fileType = "jpg";
        String[] fileTypes = new String[]{"jpeg", "jpg", "img", "svg", "png"};
        for (String type : fileTypes) {
            if (url.contains(type)) {
                fileType = type;
                break;
            }
        }
        try {
            File tempFile = File.createTempFile("temp", "." + fileType);
            FileUtils.copyURLToFile(new URL(url), tempFile);
            FileInputStream fileInputStream = new FileInputStream(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(tempFile.getName(), fileInputStream);
            String ossUrl = aliOSSUtils.upload(multipartFile);
            tempFile.delete();
            return ossUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
