package com.yryz.smart.jdbc.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.yryz.smart.jdbc.demo.dao.persistence.OrderDao;
import com.yryz.smart.jdbc.demo.dao.persistence.OrderItemDao;
import com.yryz.smart.jdbc.demo.entity.Order;
import com.yryz.smart.jdbc.demo.entity.OrderItem;
import com.yryz.smart.jdbc.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Transactional
@Service
public class DemoServiceImpl implements DemoService {

    private static final Logger LOG = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public void demo() {

        LOG.info("################################################################################");
        LOG.info("");
        LOG.info("1.Create--------------Start");
        orderDao.createIfNotExistsTable();
        orderItemDao.createIfNotExistsTable();
        LOG.info("1.Create--------------End");
        LOG.info("");
        LOG.info("################################################################################");

        LOG.info("################################################################################");
        LOG.info("");
        LOG.info("2.Truncate--------------Start");
        orderDao.truncateTable();
        orderItemDao.truncateTable();
        LOG.info("2.Truncate--------------End");
        LOG.info("");
        LOG.info("################################################################################");

        LOG.info("################################################################################");
        LOG.info("");
        LOG.info("3.Insert--------------Start");
        List<Long> orderIds = new ArrayList<>(10);
        Random random = new Random(1000);
        for (int i = 0; i < 2; i++) {
            long orderId = 1 * i;
            long orderItemId = 2 * i;
            long userId = 3 * i;

            Order order = new Order();
            order.setOrderId(orderId);
            order.setUserId(userId);
            order.setStatus("INSERT_ORDER_TEST");
            orderDao.insert(order);

            OrderItem item = new OrderItem();
            item.setOrderItemId(orderItemId);
            item.setOrderId(orderId);
            item.setUserId(userId);
            item.setStatus("INSERT_ORDER_ITEM_TEST");
            orderItemDao.insert(item);

            orderIds.add(orderId);

//            if(i == 1){
//                throw new RuntimeException("主动打断");
//            }
        }
        LOG.info("3.Insert--------------End");
        LOG.info("");
        LOG.info("################################################################################");

        LOG.info("################################################################################");
        LOG.info("");
        LOG.info("4.Select--------------Start");
        LOG.info(JSON.toJSONString(orderItemDao.selectAll()));
        LOG.info("4.Select--------------End");
        LOG.info("");
        LOG.info("################################################################################");

        LOG.info("################################################################################");
        LOG.info("");
        LOG.info("5.Delete--------------Start");
        for (Long each : orderIds) {
            orderDao.delete(each);
            orderItemDao.delete(each);
        }
        LOG.info(JSON.toJSONString(orderItemDao.selectAll()));
        LOG.info("5.Delete--------------End");
        LOG.info("");
        LOG.info("################################################################################");

//        LOG.info("################################################################################");
//        LOG.info("");
//        LOG.info("6.Drop--------------Start");
//        orderItemDao.dropTable();
//        orderDao.dropTable();
//        LOG.info("6.Drop--------------End");
//        LOG.info("");
//        LOG.info("################################################################################");
    }
}
