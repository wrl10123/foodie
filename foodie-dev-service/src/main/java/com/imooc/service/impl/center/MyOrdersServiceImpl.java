package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.StatusEnum;
import com.imooc.mapper.CustomOrdersMapper;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.MyOrdersVO;
import com.imooc.pojo.vo.MySubOrderItemVO;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.BaseService;
import com.imooc.service.center.MyOrdersService;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyOrdersServiceImpl extends BaseService implements MyOrdersService {

    @Autowired
    private CustomOrdersMapper customOrdersMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(
            String userId, Integer orderStatus, String keywords, Integer page, Integer pageSize) {
        //扩展。根据关键字查询
        if (StringUtils.isNotBlank(keywords)) {
            return queryMyOrders2(userId, orderStatus, keywords, page, pageSize);
        }
        //原版的正常查询
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }
        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> myOrdersVOS = customOrdersMapper.queryMyOrders(map);
        return setterPageGrid(myOrdersVOS, page);
    }

    /**
     * 来个扩展，添加订单模糊搜索功能，如果是在业务逻辑中完成查询
     * 1. 查询拿到orderList
     * 2. 根据orderIdList查询订单item
     * 3. 组装MyOrdersVO的list
     *
     * @param userId
     * @param orderStatus
     * @param keywords
     * @param page
     * @param pageSize
     */
    private PagedGridResult queryMyOrders2(
            String userId, Integer orderStatus, String keywords, Integer page, Integer pageSize) {
        //根据用户id查询订单
        Example orderExample = new Example(Orders.class);
        Example.Criteria criteria = orderExample.createCriteria();
        criteria.andEqualTo("isDelete", 0)
                .andEqualTo("userId", userId);
        if (orderStatus != null) {
            criteria.andEqualTo("orderStatus", orderStatus);
        }
        orderExample.setOrderByClause("updated_time asc");
        PageHelper.startPage(page, pageSize);
        List<Orders> orderList = ordersMapper.selectByExample(orderExample);
        List<String> orderIdList = orderList.stream().map(e -> e.getId()).collect(Collectors.toList());

        //查询订单状态
        Example orderStatusExample = new Example(OrderItems.class);
        orderStatusExample.createCriteria().andIn("orderId", orderIdList);
        List<OrderStatus> orderStatusList = orderStatusMapper.selectByExample(orderStatusExample);
        Map<String, Integer> orderStatusMap = orderStatusList
                .stream().collect(Collectors.toMap(e -> e.getOrderId(), e -> e.getOrderStatus()));

        //查询订单item
        Example orderItemExample = new Example(OrderItems.class);
        orderItemExample.createCriteria().andIn("orderId", orderIdList)
                .andLike("itemName", "%" + keywords + "%");
        List<OrderItems> orderItemList = orderItemsMapper.selectByExample(orderItemExample);

        //组装vo
        List<MyOrdersVO> myOrdersVOS = transferOrdersVO(orderList, orderItemList, orderStatusMap);
        return setterPageGrid(myOrdersVOS, page);
    }

    //花里胡哨的order转orderVO
    private List<MyOrdersVO> transferOrdersVO(List<Orders> orderList,
                                              List<OrderItems> orderItemList,
                                              Map<String, Integer> orderStatusMap) {
        Set<String> orderIdSet = orderItemList.stream().map(e -> e.getOrderId()).collect(Collectors.toSet());
        //过滤 -> 这个order有item才组装vo
        return orderList.stream()
                .filter(e -> orderIdSet.contains(e.getId()))
                .map(e -> {
                    MyOrdersVO ordersVO = new MyOrdersVO();
                    BeanUtils.copyProperties(e, ordersVO);
                    ordersVO.setOrderId(e.getId());
                    ordersVO.setOrderStatus(orderStatusMap.get(e.getId()));
                    ordersVO.setSubOrderItemList(orderItemInfo(e.getId(), orderItemList));
                    return ordersVO;
                }).collect(Collectors.toList());
    }

    //花里胡哨的orderItems转orderItemsVO
    private List<MySubOrderItemVO> orderItemInfo(String orderId, List<OrderItems> orderItemList) {
        return orderItemList.stream()
                .filter(e -> orderId.equals(e.getOrderId()))
                .map(e -> {
                    MySubOrderItemVO orderItemVO = new MySubOrderItemVO();
                    BeanUtils.copyProperties(e, orderItemVO);
                    return orderItemVO;
                }).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        example.createCriteria()
                .andEqualTo("orderId", orderId)
                .andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(StatusEnum.NO.type);
        return ordersMapper.selectOne(orders);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        updateOrder.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        example.createCriteria()
                .andEqualTo("orderId", orderId)
                .andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int result = orderStatusMapper.updateByExampleSelective(updateOrder, example);
        return result == 1 ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Boolean deleteOrderStatus(String userId, String orderId) {
        Orders updateOrder = new Orders();
        updateOrder.setIsDelete(StatusEnum.YES.type);
        updateOrder.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        example.createCriteria()
                .andEqualTo("id", orderId)
                .andEqualTo("userId", userId);
        int result = ordersMapper.updateByExampleSelective(updateOrder, example);
        return result == 1 ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPay = customOrdersMapper.getMyOrderStatusCounts(map);
        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliver = customOrdersMapper.getMyOrderStatusCounts(map);
        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceive = customOrdersMapper.getMyOrderStatusCounts(map);
        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", StatusEnum.YES.type);
        int success = customOrdersMapper.getMyOrderStatusCounts(map);

        return new OrderStatusCountsVO(waitPay, waitDeliver, waitReceive, success);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrderTrend(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<OrderStatus> orderStatusList = customOrdersMapper.getMyOrderTrend(userId);
        return setterPageGrid(orderStatusList, page);
    }


}
