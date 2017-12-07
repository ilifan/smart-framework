package com.yryz.smart.jdbc.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/6 17:50
 * Created by lifan
 * 一致性hash实现分片值和数据源、数据表的映射查找
 */
public class ConsistentHashingMapping {

    private static final Logger LOG = LoggerFactory.getLogger(ConsistentHashingMapping.class);

    //默认序号哈希映射(1024节点)
    private static final Map<String, Integer> INDEX_HASHING_MAPPING = new ConcurrentHashMap<>();
    //数据源哈希映射
    private static Map<String, Integer> databaseHashingMapping = new ConcurrentHashMap<>();
    //数据表哈希映射
    private static Map<String, Integer> tableHashingMapping = new ConcurrentHashMap<>();

    static {
        LOG.info("===================================================");
        LOG.info("初始默认序号哈希映射开始");
        //初始化默认序号哈希映射
        for (int i = 0; i < 1024; i++) {
            for (int j = 8; j > -2; j--) {
                int k = (2 << j);
                if (i >= k) {
                    int m = (2 << (7 - j));
                    int v = (m == 0 ? 1 : m) * (k == 0 ? i : ((i - k) * 2 + 1));
                    INDEX_HASHING_MAPPING.put(String.valueOf(i), v);
                    break;
                }
            }
        }
        LOG.info("共初始化【{}】个节点的映射关系如下：", INDEX_HASHING_MAPPING.size());
        LOG.info(INDEX_HASHING_MAPPING.toString());
        LOG.info("初始默认序号哈希映射完成");
        LOG.info("===================================================");
    }

    /**
     * 使用FNV1_32_HASH算法计算hash值
     * 结果为hash值与1024取余
     *
     * @param str
     * @return
     */
    private static int getHashing(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash % 1024;
    }

    /**
     * 组装hash值与目标数据名称的映射
     *
     * @param targetMapping
     * @param targetNames
     * @return
     */
    private static TreeMap<Integer, String> buildTargetHashing(Map<String, Integer> targetMapping, Collection<String> targetNames) {
        //组装hash数据名称映射
        TreeMap<Integer, String> hashingMap = new TreeMap<>();
        targetNames.forEach(targetName -> {
            if (targetMapping.containsKey(targetName)) {
                hashingMap.put(targetMapping.get(targetName), targetName);
            } else {
                int offset = targetName.lastIndexOf("_");
                if (offset > -1) {
                    String indexKey = targetName.substring(offset + 1);
                    hashingMap.put(INDEX_HASHING_MAPPING.get(indexKey), targetName);
                }
            }
        });
        return hashingMap;
    }

    /**
     * 根据分片原始数据计算hash值，然后从hashingMap获取目标数据名称
     *
     * @param hashingMap
     * @param shardingValue
     * @return
     */
    private static String getTargetName(TreeMap<Integer, String> hashingMap, String shardingValue) {
        if (hashingMap.isEmpty()) {
            return null;
        }
        //计算hash值
        int hashKey = getHashing(shardingValue);
        //获取真实key,找比hashKey大或者等的key值,找不到则取最小的key
        Integer realKey = hashingMap.ceilingKey(hashKey);
        if (null == realKey) {
            realKey = hashingMap.firstKey();
        }
        return hashingMap.get(realKey);
    }

    /**
     * 根据分片原始值获取目标数据源名称
     *
     * @param dataSourceNames
     * @param shardingValue
     * @return
     */
    public static String getTargetDataSourceName(Collection<String> dataSourceNames, String shardingValue) {
        if (null == dataSourceNames || dataSourceNames.isEmpty() || null == shardingValue) {
            return null;
        }
        //组装hash数据源映射
        TreeMap<Integer, String> hashingMap = buildTargetHashing(databaseHashingMapping, dataSourceNames);
        String targetDataSourceName = getTargetName(hashingMap, shardingValue);
        LOG.debug("分片值【{}】命中数据源【{}】", shardingValue, targetDataSourceName);
        return targetDataSourceName;
    }

    /**
     * 根据分片原始值获取目标数据表名称
     *
     * @param tableNames
     * @param shardingValue
     * @return
     */
    public static String getTargetTableName(Collection<String> tableNames, String shardingValue) {
        if (null == tableNames || tableNames.isEmpty() || null == shardingValue) {
            return null;
        }
        //组装hash数据表映射
        TreeMap<Integer, String> hashingMap = buildTargetHashing(tableHashingMapping, tableNames);
        String targetTableName = getTargetName(hashingMap, shardingValue);
        LOG.debug("分片值【{}】命中数据表【{}】", shardingValue, targetTableName);
        return targetTableName;
    }

    public void setDatabaseHashingMapping(Map<String, Integer> databaseHashingMapping) {
        if (null == databaseHashingMapping || databaseHashingMapping.isEmpty())
            return;
        LOG.info("===================================================");
        LOG.info("配置数据源哈希映射开始");
        ConsistentHashingMapping.databaseHashingMapping.putAll(databaseHashingMapping);
        LOG.info("共配置【{}】个数据源的映射关系如下：", databaseHashingMapping.size());
        LOG.info(ConsistentHashingMapping.databaseHashingMapping.toString());
        LOG.info("配置数据源哈希映射完成");
        LOG.info("===================================================");
    }

    public void setTableHashingMapping(Map<String, Integer> tableHashingMapping) {
        if (null == tableHashingMapping || tableHashingMapping.isEmpty())
            return;
        LOG.info("===================================================");
        LOG.info("配置数据表哈希映射开始");
        ConsistentHashingMapping.tableHashingMapping.putAll(tableHashingMapping);
        LOG.info("共配置【{}】个数据表的映射关系如下：", tableHashingMapping.size());
        LOG.info(ConsistentHashingMapping.tableHashingMapping.toString());
        LOG.info("配置数据表哈希映射完成");
        LOG.info("===================================================");
    }
}
