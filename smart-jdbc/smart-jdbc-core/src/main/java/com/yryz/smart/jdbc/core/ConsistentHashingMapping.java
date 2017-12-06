package com.yryz.smart.jdbc.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/6 17:50
 * Created by lifan
 */
public class ConsistentHashingMapping {
    //默认映射
    private static Map<String, Integer> DEFAULT_MAPPING = new ConcurrentHashMap<>();
    //自定义映射
    private static Map<String, Integer> databaseMapping = new ConcurrentHashMap<>();
    private static Map<String, Integer> tableMapping = new ConcurrentHashMap<>();

    static {
        for (int i = 0; i < 1024; i++) {
//            DEFAULT_MAPPING.put(String.valueOf(i), 1024 / (2 << i));
        }
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            for (int j = 8; j > -2; j--) {
                int k = (2 << j);
                if (i >= k) {
                    int m = (2 << (7 - j));
                    System.out.println(i+"--------------"+k+"---------"+m);
                    list.add((m > 0 ? m : 1) * (k == 0 ? i : ((i - k) * 2 + 1)));
                    break;
                }
            }
        }
        System.out.println(list.size());
        System.out.println(list);
    }

    public void setDatabaseMapping(Map<String, Integer> databaseMapping) {
        ConsistentHashingMapping.databaseMapping.putAll(databaseMapping);
    }

    public void setTableMapping(Map<String, Integer> tableMapping) {
        ConsistentHashingMapping.tableMapping.putAll(tableMapping);
    }
}
