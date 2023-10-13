package com.wjk.tutuo1.controller;

import com.wjk.tutuo1.pojo.Diagram;
import com.wjk.tutuo1.pojo.Result;
import com.wjk.tutuo1.service.DiagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DiagramController {
    @Autowired
    private DiagramService diagramService;

    @GetMapping("/diagram/{id}")
    public Result post(@PathVariable Integer id) {
        List<Diagram> diagramList = diagramService.post(id);
        return Result.success(diagramList);
    }

    @PostMapping("/diagram")
    public Result addDiagram(@RequestBody Diagram diagram){
        diagramService.add(diagram);
        return Result.success();
    }

    @PutMapping("/diagram")
    public Result updateDiagram(@RequestBody Diagram diagram){
        diagramService.update(diagram);
        return Result.success();
    }
}
