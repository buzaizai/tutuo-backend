package com.wjk.tutuo1.mapper;

import com.wjk.tutuo1.pojo.Attr;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.github.pagehelper.Page;
import java.util.List;

@Mapper
public interface AttrMapper {
//    @Select("select * from attr")
//    public List<Attr> page(Integer start, Integer pageSize);
//    @Select("select * from attr")
//    public List<Attr> list();
    public List<Attr> list(String shape, String category, String feature, String image);
    @Delete("delete from attr where id = #{id}")
    void delete(Integer id);
    @Insert("insert into attr(content, create_time, update_time, shape, category, feature) values(#{content}," +
            "#{createTime}, #{updateTime}, #{shape}, #{category}, #{feature})")
    void add(Attr attr);
    @Select("select * from attr where id = #{id}")
    Attr getById(Integer id);

    public void update(Attr attr);
    @Select("select count(*) from  attr")
    Long count();
}
