package com.wjk.tutuo1.controller;

import com.wjk.tutuo1.pojo.Attr;
import com.wjk.tutuo1.pojo.Diagram;
import com.wjk.tutuo1.service.AttrService;
import com.wjk.tutuo1.service.DiagramService;
import org.json.JSONObject;
import com.wjk.tutuo1.pojo.Result;
import com.wjk.tutuo1.utils.OpenAiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author kunkun
 */
@RestController
public class GPTWithWebHook {
    @Autowired
    private OpenAiUtils openAiUtils;
    @Autowired
    private DiagramService diagramService;
    @Autowired
    private AttrService attrService;
    @PostMapping("/hookWithGPT")
    public Result receiveMessage(@RequestBody Map<String, Map<String, Object>> mapMap) {
        Map<String, Object> map = mapMap.get("data");
        String title = map.get("title").toString();
        String post_ = map.get("body").toString();
        JSONObject jsonObject = openAiUtils.requestJson(post_);

        String image = jsonObject.getString("image");
        String basic_info = jsonObject.getString("basic_info");
        JSONObject chart_attributes = jsonObject.getJSONObject("chart_attributes");
        String shape = chart_attributes.getString("shape");
        String chart_type = chart_attributes.getString("chart_type");
        String function = chart_attributes.getString("function");
        JSONObject chart_analysis = jsonObject.getJSONObject("chart_analysis");
        String element_construction = chart_analysis.getString("element_construction");
        String element_construction_image = chart_analysis.getString("element_construction_image");
        String applicable_scenarios = chart_analysis.getString("applicable_scenarios");
        String inapplicable_scenarios = chart_analysis.getString("inapplicable_scenarios");
        JSONObject drawing = jsonObject.getJSONObject("drawing");
        String description = drawing.getString("description");
        String data_structure_description = drawing.getString("data_structure_description");
        String data_structure_example = drawing.getString("data_structure_example");
        String mermaid_code = drawing.getString("mermaid_code");
        String mermaid_image = drawing.getString("mermaid_image");
        String rendering_data = drawing.getString("rendering_data");
        String extended_data = drawing.getString("extended_data");
        Long id = null;
        Attr attr = Attr.builder().content(title).createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).shape(shape).image(image)
                .category(chart_type).feature(function).build();
        if (!diagramService.ifexist(title)) {
            attrService.add(attr);
            id = attr.getId();
            Diagram diagram = Diagram.builder().a(id).name(title).intro(basic_info)
                    .img(image).element(element_construction).elementImg(element_construction_image).apply(applicable_scenarios).unapply(inapplicable_scenarios)
                    .createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                    .paintingDescribe(description).dataStructDescribe(data_structure_description)
                    .mermaidImg(mermaid_image).dataStructure(data_structure_example).mermaidCode(mermaid_code)
                    .renderingData(rendering_data).extendingData(extended_data)
                    .build();
            diagramService.add(diagram);
        }else {
            Long Id = diagramService.getA(title);
            Long a = diagramService.getId(title);
//            System.out.println(Id);
            Attr updateAttr = Attr.builder().content(title).createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now()).shape(shape).image(image)
                    .category(chart_type).feature(function).id(a).build();
            attrService.update(updateAttr);
            Diagram diagram = Diagram.builder().name(title).intro(basic_info)
                    .img(image).element(element_construction).apply(applicable_scenarios).unapply(inapplicable_scenarios)
                    .createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                    .paintingDescribe(description).dataStructDescribe(data_structure_description)
                    .mermaidImg(mermaid_image).elementImg(element_construction_image).dataStructure(data_structure_example).mermaidCode(mermaid_code)
                    .renderingData(rendering_data).extendingData(extended_data).id(Id)
                    .build();
//            System.out.println(diagram);

            diagramService.update(diagram);
        }

        return Result.success(id);
    }
}
