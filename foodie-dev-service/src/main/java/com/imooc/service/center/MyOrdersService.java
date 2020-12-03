package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.utils.PagedGridResult;

public interface MyOrdersService {

    /**
     * 查询用户所有订单
     *
     * @param userId
     * @return
     */
    PagedGridResult queryMyOrders(String userId, Integer orderStatus,
                                  Integer page, Integer pageSize);

    /**
     * 更新订单状态： 已付款 --> 已发货
     *
     * @param orderId
     */
    void updateDeliverOrderStatus(String orderId);


    /**
     * 查询用户单个订单
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 更新订单状态： 确认收货
     * @param orderId
     * @return
     */
    Boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单（逻辑删除）
     * @param userId
     * @param orderId
     * @return
     */
    Boolean deleteOrderStatus(String userId, String orderId);
}
