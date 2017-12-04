package com.yryz.smart.jdbc.demo.service.impl;

import com.yryz.smart.jdbc.demo.dao.persistence.OrderDao;
import com.yryz.smart.jdbc.demo.dao.persistence.OrderItemDao;
import com.yryz.smart.jdbc.demo.entity.Order;
import com.yryz.smart.jdbc.demo.entity.OrderItem;
import com.yryz.smart.jdbc.demo.service.DemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    public void demo() {
        orderDao.createIfNotExistsTable();
        orderItemDao.createIfNotExistsTable();
        orderDao.truncateTable();
        orderItemDao.truncateTable();
        List<Long> orderIds = new ArrayList<>(10);
        System.out.println("1.Insert--------------");
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setUserId(51);
            order.setStatus("INSERT_TEST");
            orderDao.insert(order);
            long orderId = order.getOrderId();
            orderIds.add(orderId);

            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setUserId(51);
            item.setStatus("INSERT_TEST");
            orderItemDao.insert(item);
        }
        System.out.println(orderItemDao.selectAll());
//        System.out.println("2.Delete--------------");
//        for (Long each : orderIds) {
//            orderDao.delete(each);
//            orderItemDao.delete(each);
//        }
//        System.out.println(orderItemDao.selectAll());
//        orderItemDao.dropTable();
//        orderDao.dropTable();
    }
}
