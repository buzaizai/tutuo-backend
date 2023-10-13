package com.wjk.tutuo1.service;

import com.wjk.tutuo1.pojo.Attr;
import com.wjk.tutuo1.pojo.PageBean;

public interface AttrService {


   PageBean page(Integer page, Integer pageSize, String shape, String category, String feature, String image);

   void delete(Integer id);

   void add(Attr attr);

   Attr getById(Integer id);

   void update(Attr attr);
}
