package com.imooc.mapper;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.pojo.vo.MySubOrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomOrdersMapper {

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
     * 查询订单附属。里面用不着接口，xml里面有查询语句就可以了
     *
     * @param map
     * @return
     */
//    List<MySubOrderItemVO> getSubItems(@Param("paramMap") Map<String, Object> map);

    /**
     * 查询我的订单状态个数
     *
     * @param map
     * @return
     */
    int getMyOrderStatusCounts(@Param("paramMap") Map<String, Object> map);

    /**
     * 获取订单动向
     *
     * @param userId
     * @return
     */
    List<OrderStatus> getMyOrderTrend(String userId);
}
