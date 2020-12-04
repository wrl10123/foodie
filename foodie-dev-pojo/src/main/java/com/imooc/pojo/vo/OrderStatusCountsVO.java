package com.imooc.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusCountsVO {

    private int waitPayCounts;
    private int waitDeliverCounts;
    private int waitReceiveCounts;
    private int waitCommentCounts;

}
