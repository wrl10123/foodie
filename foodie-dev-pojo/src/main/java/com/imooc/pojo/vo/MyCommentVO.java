package com.imooc.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MyCommentVO {

    private String commentId;
    private String content;
    private String itemId;
    private String itemName;
    private String itemImg;
    private String specName;
    private Date createdTime;
}
