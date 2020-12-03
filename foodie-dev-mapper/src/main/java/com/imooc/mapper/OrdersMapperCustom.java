package com.imooc.mapper;

import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.pojo.vo.MySubOrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {

    /**
     * 直接一条语句，分页查询会有bug
     *
     * @param map
     * @return
     */
    List<MyOrdersVO> queryMyOrdersDoNot(@Param("paramMap") Map<String, Object> map);


    /**
     * 查询订单
     *
     * @param map
     * @return
     */
    List<MyOrdersVO> queryMyOrders(@Param("paramMap") Map<String, Object> map);

    /**
     * 查询订单附属
     *
     * @param orderId
     * @return
     */
    List<MySubOrderItemVO> getSubItems(String orderId);

}
