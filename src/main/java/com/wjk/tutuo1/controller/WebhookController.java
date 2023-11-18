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

    @PostMapping("/hook")
    public Result receiveMessage(@RequestBody Map<String, Map<String, Object>> mapMap) {
        Map<String, Object> map = mapMap.get("data");
        String title = map.get("title").toString();
        String post_ = map.get("body").toString();
        //System.out.println(title);
        Pattern pattern = Pattern.compile("<a[^>]*>(.*?)</a>");
        Matcher matcher = pattern.matcher(post_);
        String post = matcher.replaceAll("");
        MarkdownUtils markdownUtils = new MarkdownUtils();
        String imgUrl = markdownUtils.imgUrl(post);
        String[] index = markdownUtils.analyse(post);
            //System.out.println(index[i]);//基本信息、属性（忽略）、元素构成、适用场景、不适用场景、描述、数据结构描述、效果图、数据结构、mermaid、渲染数据、拓展数据、形状、图形、功能
        Long id = null;
        Attr attr = Attr.builder().content(title).createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).shape(index[12]).image(imgUrl)
                .category(index[13]).feature(index[14]).build();
        if (!diagramService.ifexist(title)) {
            attrService.add(attr);
            id = attr.getId();
            Diagram diagram = Diagram.builder().a(id).name(title).intro(index[0])
                    .img(imgUrl).element(index[2]).apply(index[3]).unapply(index[4])
                    .createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                    .paintingDescribe(index[5]).dataStructDescribe(index[6])
                    .mermaidImg(index[7]).dataStructure(index[8]).mermaidCode(index[9])
                    .renderingData(index[10]).extendingData(index[11])
                    .build();
            diagramService.add(diagram);
        }else {
            Long Id = diagramService.getA(title);
            Long a = diagramService.getId(title);
//            System.out.println(Id);
            Attr updateAttr = Attr.builder().content(title).createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now()).shape(index[12]).image(imgUrl)
                    .category(index[13]).feature(index[14]).id(a).build();
            attrService.update(updateAttr);
            Diagram diagram = Diagram.builder().name(title).intro(index[0])
                    .img(imgUrl).element(index[2]).apply(index[3]).unapply(index[4])
                    .createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                    .paintingDescribe(index[5]).dataStructDescribe(index[6])
                    .mermaidImg(index[7]).dataStructure(index[8]).mermaidCode(index[9])
                    .renderingData(index[10]).extendingData(index[11]).id(Id)
                    .build();
//            System.out.println(diagram);

            diagramService.update(diagram);
        }

        return Result.success(id);
    }
}
