package com.yryz.smart.jdbc.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
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
        LOG.info("1.Create--------------Start");
        orderDao.createIfNotExistsTable();
        orderItemDao.createIfNotExistsTable();
        LOG.info("1.Create--------------End");
        LOG.info("################################################################################");
        LOG.info("");

        LOG.info("################################################################################");
        LOG.info("2.Truncate--------------Start");
        orderDao.truncateTable();
        orderItemDao.truncateTable();
        LOG.info("2.Truncate--------------End");
        LOG.info("################################################################################");
        LOG.info("");

        LOG.info("################################################################################");
        LOG.info("3.Insert--------------Start");
        List<Long> orderIds = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            long orderId = 1 * i;
            long orderItemId = 2 * i;
            long createUserId = 3 * i;

            Order order = new Order();
            order.setKid(orderId);
            order.setCreateUserId(String.valueOf(createUserId));
            orderDao.insert(order);

            OrderItem item = new OrderItem();
            item.setKid(orderItemId);
            item.setOrderKid(orderId);
            item.setCreateUserId(String.valueOf(createUserId));
            orderItemDao.insert(item);

            orderIds.add(orderId);
        }
        LOG.info("3.Insert--------------End");
        LOG.info("################################################################################");
        LOG.info("");

        LOG.info("################################################################################");
        LOG.info("4.Select--------------Start");
        PageHelper.startPage(1, 10);
        List<OrderItem> resultList = orderItemDao.selectAll();
        LOG.info(resultList.toString());
        LOG.info(JSON.toJSONString(resultList));
        LOG.info("4.Select--------------End");
        LOG.info("################################################################################");
        LOG.info("");

//        LOG.info("################################################################################");
//        LOG.info("5.Delete--------------Start");
//        for (Long each : orderIds) {
//            orderDao.delete(each);
//            orderItemDao.delete(each);
//        }
//        PageHelper.startPage(1, 10);
//        resultList = orderItemDao.selectAll();
//        LOG.info(resultList.toString());
//        LOG.info(JSON.toJSONString(resultList));
//        LOG.info("5.Delete--------------End");
//        LOG.info("################################################################################");
//        LOG.info("");

//        LOG.info("################################################################################");
//        LOG.info("6.Drop--------------Start");
//        orderItemDao.dropTable();
//        orderDao.dropTable();
//        LOG.info("6.Drop--------------End");
//        LOG.info("################################################################################");
//        LOG.info("");
    }
}
