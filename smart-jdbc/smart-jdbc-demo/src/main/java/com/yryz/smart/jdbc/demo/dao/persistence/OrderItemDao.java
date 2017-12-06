/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.yryz.smart.jdbc.demo.dao.persistence;


import com.github.pagehelper.Page;
import com.yryz.smart.jdbc.demo.entity.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDao {

    void createIfNotExistsTable();

    void truncateTable();

    int insert(OrderItem model);

    void delete(Long orderItemId);

    void dropTable();

    List<OrderItem> selectAll();
}
