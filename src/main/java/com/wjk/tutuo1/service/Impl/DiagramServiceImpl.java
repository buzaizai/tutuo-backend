package com.wjk.tutuo1.service.Impl;

import com.wjk.tutuo1.mapper.DiagramMapper;
import com.wjk.tutuo1.pojo.Diagram;
import com.wjk.tutuo1.service.DiagramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DiagramServiceImpl  implements DiagramService {
    @Autowired
    private DiagramMapper diagramMapper;
    @Override
    public List<Diagram> post(Integer id){
        return diagramMapper.post(id);
    }

    @Override
    public void add(Diagram diagram) {
        diagram.setCreateTime(LocalDateTime.now());
        diagram.setUpdateTime(LocalDateTime.now());
        diagramMapper.add(diagram);
    }

    @Override
    public void update(Diagram diagram) {
        diagram.setUpdateTime(LocalDateTime.now());
        diagramMapper.update(diagram);
    }

    @Override
    public boolean ifexist(String title) {
        if (diagramMapper.ifexist(title) != null) {
            return true;
        }
        return false;
    }

}
