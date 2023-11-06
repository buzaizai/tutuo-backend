package com.wjk.tutuo1.utils;

import java.util.regex.*;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock; // 注释1
import org.commonmark.ext.gfm.tables.TablesExtension; // 注释2
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension; // 注释3
import org.commonmark.node.*;
import org.commonmark.parser.Parser; // 注释6
import org.commonmark.renderer.html.AttributeProvider; // 注释7
import org.commonmark.renderer.html.AttributeProviderContext; // 注释8
import org.commonmark.renderer.html.AttributeProviderFactory; // 注释9
import org.commonmark.renderer.html.HtmlRenderer; // 注释10


/**
 * Created by limi on 2017/10/22.
 */
public class MarkdownUtils {

    /*
    * 去除标题
    * */
    public static boolean found(String title){
        String[] tt = {"基本信息介绍","图表属性","图表分析","元素构成","适用场景","不适用场景",
                "绘制","描述","数据结构描述","Mermaid代码","数据结构示例",
                "Mermaid效果图","渲染数据","拓展数据"};
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
    public static void markdownToHtml(String markdown,StringBuilder stringBuilder) {
        Parser parser = Parser.builder().build(); // 创建解析器实例 // 注释12
        Node document = parser.parse(markdown); // 解析Markdown文本为节点树 // 注释13
        printChildren(document,stringBuilder);
    }


    public static void printChildren(Node node,StringBuilder stringBuilder) {
        Node child = node.getFirstChild();
        while (child != null) {
            if(child instanceof IndentedCodeBlock){
                stringBuilder.append("·").append(((IndentedCodeBlock) child).getLiteral()).append("·").append("\n");
            } else if (child instanceof Text) {
                stringBuilder.append("~").append(((Text) child).getLiteral()).append("~").append("\n");
            } else if (child instanceof FencedCodeBlock) {
                stringBuilder.append("```").append(((FencedCodeBlock) child).getLiteral()).append("```").append("\n");
            } else if (child instanceof Image) {
                stringBuilder.append("~").append("<img src=\"").append(((Image) child).getDestination()).append("\">").append("~");
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
        return result;
    }

    public  String[] analyse(String input) {
        String[] result1 = new String[17];
        String a = input;
        StringBuilder stringBuilder = new StringBuilder();
        markdownToHtml(a,stringBuilder); // 将示例字符串转换为HTML并输出 // 注释24
        StringBuilder sb = new StringBuilder();
        String regex1 = "·(.*?)·";
        String regex2 = "~(.*?)~";
        String regex3 = "```(.*?)```";
        String regex4 = "形状[: ：](.*?)~\\n";
        String regex5 = "图类[: ：](.*?)~\\n";
        String regex6 = "功能[: ：](.*?)~\\n";
        Pattern pattern1 = Pattern.compile(regex1,Pattern.DOTALL);
        Pattern pattern2 = Pattern.compile(regex2,Pattern.DOTALL);
        Pattern pattern3 = Pattern.compile(regex3,Pattern.DOTALL);
        Pattern pattern4 = Pattern.compile(regex4,Pattern.DOTALL);
        Pattern pattern5 = Pattern.compile(regex5,Pattern.DOTALL);
        Pattern pattern6 = Pattern.compile(regex6,Pattern.DOTALL);
        Matcher matcher1 = pattern1.matcher(stringBuilder);
        Matcher matcher2 = pattern2.matcher(stringBuilder);
        Matcher matcher3 = pattern3.matcher(stringBuilder);
        Matcher matcher4 = pattern4.matcher(stringBuilder);
        Matcher matcher5 = pattern5.matcher(stringBuilder);
        Matcher matcher6 = pattern6.matcher(stringBuilder);
        while (matcher1.find()) {
            String content = matcher1.group(1);
            sb.append(content).append("\n");
        }
        while (matcher2.find()) {
            String content = matcher2.group(1);
            if(content.matches("image\\..*")){
                continue;
            } else if (found(content)){
                sb.append("tdhed0").append("\n");
                continue;
            }
            sb.append(content).append("\n");
        }
        String patternString = "(?<=tdhed0)([^t]*(?:t(?!dhed0)[^t]*)*)(?=tdhed0)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(sb);
        int i =0;
        while (matcher.find()){
            String result = matcher.group(1);
            if (!result.trim().isEmpty()) {
                result1[i++]=matcher.group().trim();
            }
        }
        while (matcher3.find()) {
            String content = matcher3.group(1);
            result1[i++]=content;
        }
        while (matcher4.find()) {
            String content = matcher4.group(1);
            result1[i++]=content;
        }
        while (matcher5.find()) {
            String content = matcher5.group(1);
            result1[i++]=content;
        }
        while (matcher6.find()) {
            String content = matcher6.group(1);
            result1[i++]=content;
        }
        return result1;
//        System.out.println(result1[10]);//基本信息、属性（忽略）、元素构成、适用场景、不适用场景、描述、数据结构描述、效果图、数据结构、mermaid、渲染数据、拓展数据、形状、图形、功能
    }
}
