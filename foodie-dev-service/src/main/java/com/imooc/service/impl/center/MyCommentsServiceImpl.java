package com.imooc.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.ItemsCommentsMapperCustomer;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.pojo.vo.MyCommentVO;
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
public class MyCommentsServiceImpl implements MyCommentsService {
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private ItemsCommentsMapperCustomer itemsCommentsMapperCustomer;
    @Autowired
    private Sid sid;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems query = new OrderItems();
        query.setOrderId(orderId);
        return orderItemsMapper.select(query);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {
        //1.保存评价 items_comments
        for(OrderItemsCommentBO oic:commentList){
            oic.setCommentId(sid.nextShort());
        }
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        map.put("commentList",commentList);
        itemsCommentsMapperCustomer.saveComments(map);
        //2.修改订单表改为已评价
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.Yes.type);
        ordersMapper.updateByPrimaryKeySelective(order);
        //3.修改订单状态表的留言时间order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        PageHelper.startPage(page,pageSize);
        List<MyCommentVO> myCommentVOS = itemsCommentsMapperCustomer.queryMyComments(map);
        return setPagedGridResult(page,myCommentVOS);
    }
    private PagedGridResult setPagedGridResult(Integer page,List<?> list) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        // 当前页数
        grid.setPage(page);
        //分页后的数据
        grid.setRows(list);
        //总页数
        grid.setTotal(pageList.getPages());
        //总记录数
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}