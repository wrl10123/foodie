package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.StatusEnum;
import com.imooc.mapper.CustomItemsCommentsMapper;
import com.imooc.mapper.ItemsCommentsMapper;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.ItemsComments;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentsBO;
import com.imooc.pojo.vo.MyCommentVO;
import com.imooc.service.BaseService;
import com.imooc.service.center.MyCommentsService;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private CustomItemsCommentsMapper customItemsCommentsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems queryOrder = new OrderItems();
        queryOrder.setOrderId(orderId);
        return orderItemsMapper.select(queryOrder);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String userId, String orderId, List<OrderItemsCommentsBO> commentList) {
        //1. 保存评价
        for (OrderItemsCommentsBO commentsBO : commentList) {
            ItemsComments insertItemComments = new ItemsComments();
            insertItemComments.setId(sid.nextShort());
            insertItemComments.setUserId(userId);
            insertItemComments.setCommentLevel(commentsBO.getCommentLevel());
            insertItemComments.setContent(commentsBO.getContent());
            insertItemComments.setItemId(commentsBO.getItemId());
            insertItemComments.setItemName(commentsBO.getItemName());
            insertItemComments.setItemSpecId(commentsBO.getItemSpecId());
            insertItemComments.setSepcName(commentsBO.getItemSpecName());
            insertItemComments.setUpdatedTime(new Date());
            insertItemComments.setCreatedTime(new Date());
            itemsCommentsMapper.insert(insertItemComments);
        }
        //2. 修改订单的评价状态
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(StatusEnum.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);
        //3. 更新订单状态的留言时间
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);

        List<MyCommentVO> myCommentVOS = customItemsCommentsMapper.queryMyComments(map);

        return setterPageGrid(myCommentVOS, page);
    }
}
