package com.imooc.pojo.vo;

import lombok.Data;

/**
 * 我的订单，嵌套的itemVO
 */
@Data
public class MySubOrderItemVO {

    private String itemId;
    private String itemImg;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer buyCounts;
    private Integer price;
}
