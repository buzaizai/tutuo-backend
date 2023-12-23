package com.wjk.tutuo1.controller;

import com.wjk.tutuo1.pojo.Attr;
import com.wjk.tutuo1.pojo.Diagram;
import com.wjk.tutuo1.pojo.Result;
import com.wjk.tutuo1.service.AttrService;
import com.wjk.tutuo1.service.DiagramService;
import com.wjk.tutuo1.utils.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author kunkun
 */
@CrossOrigin(origins = "https://yuque.com", methods = RequestMethod.POST)
@RestController
public class WebhookController {
    @Autowired
    private DiagramService diagramService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private MarkdownUtils markdownUtils;

    @PostMapping("/hook")
    public Result receiveMessage(@RequestBody Map<String, Map<String, Object>> mapMap) {
        Map<String, Object> map = mapMap.get("data");
        String title = map.get("title").toString();
        String post_ = map.get("body").toString();
        //System.out.println(title);
        Pattern pattern = Pattern.compile("<a[^>]*>(.*?)</a>");
        Matcher matcher = pattern.matcher(post_);
        String post = matcher.replaceAll("");
        String imgUrl = markdownUtils.imgUrl(post);
        String[] index = markdownUtils.analyse(post);
            //System.out.println(index[i]);//基本信息、属性（忽略）、元素构成、适用场景、不适用场景、描述、数据结构描述、效果图、数据结构、mermaid、渲染数据、拓展数据、形状、图形、功能
        Long id = null;
        Attr attr = Attr.builder().content(title).createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).shape(index[13]).image(imgUrl)
                .category(index[14]).feature(index[15]).build();
        if (!diagramService.ifexist(title)) {
            attrService.add(attr);
            id = attr.getId();
            Diagram diagram = Diagram.builder().a(id).name(title).intro(index[0])
                    .img(imgUrl).element(index[2]).elementImg(index[3]).apply(index[4]).unapply(index[5])
                    .createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                    .paintingDescribe(index[6]).dataStructDescribe(index[7])
                    .mermaidImg(index[8]).dataStructure(index[9]).mermaidCode(index[10])
                    .renderingData(index[11]).extendingData(index[12])
                    .build();
            diagramService.add(diagram);
        }else {
            Long Id = diagramService.getA(title);
            Long a = diagramService.getId(title);
//            System.out.println(Id);
            Attr updateAttr = Attr.builder().content(title).createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now()).shape(index[13]).image(imgUrl)
                    .category(index[14]).feature(index[15]).id(a).build();
            attrService.update(updateAttr);
            Diagram diagram = Diagram.builder().name(title).intro(index[0])
                    .img(imgUrl).element(index[2]).apply(index[4]).unapply(index[5])
                    .createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                    .paintingDescribe(index[6]).dataStructDescribe(index[7])
                    .mermaidImg(index[8]).elementImg(index[3]).dataStructure(index[9]).mermaidCode(index[10])
                    .renderingData(index[11]).extendingData(index[12]).id(Id)
                    .build();
//            System.out.println(diagram);

            diagramService.update(diagram);
        }

        return Result.success(id);
    }
}
