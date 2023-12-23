package com.wjk.tutuo1.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.*;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import org.apache.commons.io.FileUtils;
import org.commonmark.ext.gfm.tables.TableBlock; // 注释1
import org.commonmark.ext.gfm.tables.TablesExtension; // 注释2
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension; // 注释3
import org.commonmark.node.*;
import org.commonmark.parser.Parser; // 注释6
import org.commonmark.renderer.html.AttributeProvider; // 注释7
import org.commonmark.renderer.html.AttributeProviderContext; // 注释8
import org.commonmark.renderer.html.AttributeProviderFactory; // 注释9
import org.commonmark.renderer.html.HtmlRenderer; // 注释10
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class MarkdownUtils {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    /*
    * 去除标题
    * */
    public static boolean found(String title){
        String[] tt = {"基本信息介绍","图表属性","图表分析","元素构成","适用场景","不适用场景",
                "绘制","描述","数据结构描述","Mermaid代码","数据结构示例",
                "Mermaid效果图","渲染数据","拓展数据","Echarts代码","Echarts效果图","元素构成图"};
        for(String str:tt){
            if(title.equals(str)){
                return true;
            }
        }
        return false;
    }

    /*
    * 解析markdown为节点
    * */
    public void markdownToHtml(String markdown,StringBuilder stringBuilder) {
        Parser parser = Parser.builder().build(); // 创建解析器实例
        Node document = parser.parse(markdown); // 解析Markdown文本为节点树
        printChildren(document,stringBuilder);
    }


    public  void printChildren(Node node,StringBuilder stringBuilder) {
        Node child = node.getFirstChild();
        while (child != null) {
            if(child instanceof IndentedCodeBlock){
                if (((IndentedCodeBlock) child).getLiteral().equals("//")) {
                    stringBuilder.append("·").append(" ").append("·").append("\n");
                    continue;
                } else {
                    stringBuilder.append("·").append(((IndentedCodeBlock) child).getLiteral()).append("·").append("\n");
                }
            } else if (child instanceof Text) {
                if (((Text) child).getLiteral().equals("//")) {
                    stringBuilder.append("~").append(" ").append("~").append("\n");
                } else {
                    stringBuilder.append("~").append(((Text) child).getLiteral()).append("~").append("\n");
                }
            } else if (child instanceof FencedCodeBlock) {
                if (((FencedCodeBlock) child).getLiteral().equals("//\n")) {
                    stringBuilder.append("```").append(" ").append("```").append("\n");
                } else {
                    stringBuilder.append("```").append(((FencedCodeBlock) child).getLiteral()).append("```").append("\n");
                }
            } else if (child instanceof Image) {
                String imageUrl = uploadImg(((Image) child).getDestination());
                stringBuilder.append("~").append("<img src=\"").append(imageUrl).append("\">").append("~");
                System.out.println(imageUrl);
            }
            printChildren(child,stringBuilder); // 递归输出子节点的子节点
            child = child.getNext(); // 指向下一个子节点
        }
    }
    public String imgUrl(String input) {
        Pattern pattern = Pattern.compile("!\\[\\]\\((.*?)\\)"); // 匹配 Markdown 图片链接的正则表达式
        Matcher matcher = pattern.matcher(input);
        String result = new String();
        if (matcher.find()) {
            String imageUrl = matcher.group(1);
            result = imageUrl;
        }
        System.out.println("upload process");
        return uploadImg(result);
    }

    public  String[] analyse(String input) {
        String[] result1 = new String[17];
        String a = input;
        StringBuilder stringBuilder = new StringBuilder();
        markdownToHtml(a,stringBuilder); // 将示例字符串转换为HTML并输出
        StringBuilder sb = new StringBuilder();
        String[] regexs = {"·(.*?)·", "~(.*?)~", "```(.*?)```", "形状[: ：](.*?)~\\n", "图类[: ：](.*?)~\\n", "功能[: ：](.*?)~\\n","(?<=tdhed0)([^t]*(?:t(?!dhed0)[^t]*)*)(?=tdhed0)","<img\\s+src\\s*=\\s*\"([^\"]+)\""};
        Pattern[] patterns = {Pattern.compile(regexs[0],Pattern.DOTALL), Pattern.compile(regexs[1],Pattern.DOTALL), Pattern.compile(regexs[2],Pattern.DOTALL), Pattern.compile(regexs[3],Pattern.DOTALL), Pattern.compile(regexs[4],Pattern.DOTALL), Pattern.compile(regexs[5],Pattern.DOTALL), Pattern.compile(regexs[6]),Pattern.compile(regexs[7])};
        Matcher[]  matchers = new Matcher[]{patterns[0].matcher(stringBuilder), patterns[1].matcher(stringBuilder), patterns[2].matcher(stringBuilder), patterns[3].matcher(stringBuilder), patterns[4].matcher(stringBuilder), patterns[5].matcher(stringBuilder)};
        while (matchers[0].find()) {
            String content = matchers[0].group(1);
            sb.append(content).append("\n");
        }
        while (matchers[1].find()) {
            String content = matchers[1].group(1);
            if(content.matches("image\\..*")){
                continue;
            } else if (found(content)){
                sb.append("tdhed0").append("\n");
                continue;
            }
            sb.append(content).append("\n");
        }
        int i =0;
        Matcher matchers6= patterns[6].matcher(sb);
        while (matchers6.find()){
            String result = matchers6.group(1);
            if (!result.trim().isEmpty()|| "\n \n".equals(result)) {
                if (!result.trim().isEmpty()) {
                    result1[i++] = matchers6.group().trim();
                } else {
                    result1[i++] = " ";
                }
            }
        }
        for(int j =2;j<=5;j++){
            while (matchers[j].find()) {
                String content = matchers[j].group(1);
                result1[i++]=content;
            }
        }
        matchers = new Matcher[]{patterns[7].matcher(result1[3]), patterns[7].matcher(result1[8])};

        for(int j = 0;j<=1;j++){
            if (matchers[j].find()) {
                result1[3] = matchers[j].group(1);
            }
        }
//        System.out.println(Arrays.toString(result1));
        return result1;
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
