package com.imooc.pojo.bo.center;

import lombok.Data;

@Data
public class OrderItemsCommentsBO {

    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer commentLevel;
    private String content;

}
