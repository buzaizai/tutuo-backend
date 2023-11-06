package com.wjk.tutuo1.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diagram {
    private Long id;
    private String name;
    private String img;
    private String intro;
    private String element;
    private String apply;
    private String unapply;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
    private Long a;
    private String paintingDescribe;
    private String dataStructDescribe;
    private String dataStructure;
    private String mermaidCode;
    private String mermaidImg;
    private String renderingData;
    private String extendingData;

}
