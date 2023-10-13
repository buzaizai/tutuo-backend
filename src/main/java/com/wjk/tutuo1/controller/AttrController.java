package com.wjk.tutuo1.controller;

import com.wjk.tutuo1.pojo.Attr;
import com.wjk.tutuo1.pojo.Diagram;
import com.wjk.tutuo1.pojo.Result;
import com.wjk.tutuo1.pojo.PageBean;
import com.wjk.tutuo1.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Attrs")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @GetMapping
    public Result list(@RequestParam(defaultValue = "1") Integer
                                   page,
                       @RequestParam(defaultValue = "10") Integer
                                   pageSize,String shape,String category,String feature,String image){
        PageBean pageBean = attrService.page(page, pageSize, shape, category, feature, image);
        return Result.success(pageBean);
    }
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        Attr attr = attrService.getById(id);
        return Result.success(attr);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        attrService.delete(id);
        return Result.success();
    }

    @PostMapping
    public Result add(@RequestBody Attr attr){
        attrService.add(attr);
        List <Long> response = new ArrayList<>();
        response.add(attr.getId());
        return Result.success(response);
    }

    @PutMapping
    public Result update(@RequestBody Attr attr){
        attrService.update(attr);
        return Result.success();
    }



}
