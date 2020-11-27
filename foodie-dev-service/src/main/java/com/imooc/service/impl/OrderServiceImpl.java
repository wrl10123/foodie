package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.StatusEnum;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String createOrder(SubmitOrderBO submitOrderBO) {
        //获取用户地址详细信息
        UserAddress address = addressService.queryUserAddress(
                submitOrderBO.getUserId(), submitOrderBO.getAddressId());
        //创建一个订单id
        String orderId = sid.nextShort();

        //1.循环根据itemSpecIds保存订单中的商品信息
        String[] itemSplitIdArr = submitOrderBO.getItemSpecIds().split(",");
        Integer totalAmount = 0;    //原价
        Integer realPayAmount = 0;  //优惠后的实际支付累计
        for (String itemSplitId : itemSplitIdArr) {
            //1.1 根据规格id，查询规格的价格详情，获取价格
            ItemsSpec itemsSpec = itemService.queryItemsBySpecId(itemSplitId);

            //todo 从redis里获取购物车中商品的数据
            int buyCounts = 1;

            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            //1.2 获取商品信息、商品图片
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            //1.3 保存子订单数据到数据库
            OrderItems orderItem = new OrderItems();
            orderItem.setId(sid.nextShort());
            orderItem.setOrderId(orderId);
            orderItem.setItemId(itemId);
            orderItem.setItemName(item.getItemName());
            orderItem.setItemImg(imgUrl);
            orderItem.setBuyCounts(buyCounts);
            orderItem.setItemSpecId(itemSplitId);
            orderItem.setItemSpecName(itemsSpec.getName());
            orderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(orderItem);

            //1.4 在用户提交订单以后，要扣除相应库存
            itemService.decreaseItemSpecStock(itemSplitId, buyCounts);

        }
        //2.保存订单
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(submitOrderBO.getUserId());
        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince()
                + address.getCity() + address.getDistrict() + address.getDetail());
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        newOrder.setPostAmount(0);
        newOrder.setPayMethod(submitOrderBO.getPayMethod());
        newOrder.setLeftMsg(submitOrderBO.getLeftMsg());
        newOrder.setIsComment(StatusEnum.NO.type);
        newOrder.setIsDelete(StatusEnum.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());
        ordersMapper.insert(newOrder);

        //3.保存订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);

        return orderId;
    }


}
