package com.wjk.tutuo1.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wjk.tutuo1.mapper.AttrMapper;
import com.wjk.tutuo1.mapper.DiagramMapper;
import com.wjk.tutuo1.pojo.Attr;
import com.wjk.tutuo1.pojo.PageBean;
import com.wjk.tutuo1.service.AttrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AttrServiceImpl implements AttrService {
    @Autowired
    private AttrMapper attrMapper;
    @Autowired
    private DiagramMapper diagramMapper;

    @Override
    public PageBean page(Integer page, Integer pageSize, String shape, String category, String feature, String image) {
        // 设置分页参数
        PageHelper.startPage(page,pageSize);
        List<Attr> attrList = attrMapper.list(shape, category, feature,image);
        Page<Attr> p = (Page<Attr>) attrList;
        //3. 封装PageBean对象

        return new PageBean(p.getTotal(), p.getResult());
    }
    @Override
    @Transactional
    public void delete(Integer id){
        attrMapper.delete(id);
        diagramMapper.delete(id);
    }

    @Override
    public void add(Attr attr) {
        attr.setUpdateTime(LocalDateTime.now());
        attr.setCreateTime(LocalDateTime.now());
        attrMapper.add(attr);
    }

    @Override
    public Attr getById(Integer id) {
        return attrMapper.getById(id);
    }

    @Override
    public void update(Attr attr) {
        attr.setUpdateTime(LocalDateTime.now());
        attrMapper.update(attr);
    }
}
