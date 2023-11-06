package com.wjk.tutuo1.service;

import com.wjk.tutuo1.pojo.Diagram;

import java.util.List;

public interface DiagramService {
     List<Diagram> post(Integer id);

     void add(Diagram diagram);

     void update(Diagram diagram);

    boolean ifexist(String title);
}
