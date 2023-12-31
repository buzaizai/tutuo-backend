package com.wjk.tutuo1.mapper;

import com.wjk.tutuo1.pojo.Diagram;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DiagramMapper {
    @Select("select * from diagram where a=#{id}")
    List<Diagram> post(Integer id);
    @Delete("delete from diagram where a = #{id}")
    void delete(Integer id);
    @Insert("insert into diagram(a, name, img, intro, element,elementImg, apply, unapply, create_time, update_time, paintingDescribe, dataStructure, mermaidCode, mermaidImg, renderingData, extendingData, dataStructDescribe) values (#{a}, #{name}, #{img}, #{intro}, #{element},#{elementImg}, #{apply}, " +
            "#{unapply}, #{createTime}, #{updateTime}, #{paintingDescribe}, #{dataStructure}, #{mermaidCode}, #{mermaidImg}, #{renderingData},#{extendingData}, #{dataStructDescribe})")
    void add(Diagram diagram);
    @Update("update diagram set name = #{name}, img = #{img}, intro = #{intro}, element = #{element},elementImg=#{elementImg}, apply = " +
            "#{apply}, unapply = #{unapply}, update_time = #{updateTime},paintingDescribe=#{paintingDescribe}, dataStructure=#{dataStructure}," +
            "mermaidCode=#{mermaidCode}, mermaidImg=#{mermaidImg}, renderingData=#{renderingData}, extendingData=#{extendingData}, dataStructDescribe=#{dataStructDescribe} where id = #{id} ")
    void update(Diagram diagram);

    @Select("select * from diagram where name=#{title}")
    List<Diagram> ifexist(String title);

    @Select("select id from diagram where name=#{title}")
    Long getA(String title);
    @Select("select a from diagram where name=#{title}")
    Long getId(String title);
}
