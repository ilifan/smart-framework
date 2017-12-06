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

package com.yryz.smart.jdbc.core.algorithm;

import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public final class PreciseModuloDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    private static final Logger LOG = LoggerFactory.getLogger(PreciseModuloDatabaseShardingAlgorithm.class);

    @Override
    public String doSharding(final Collection<String> availableTargetNames, final PreciseShardingValue<String> shardingValue) {
        for (String each : availableTargetNames) {
            if (each.endsWith(Long.parseLong(shardingValue.getValue()) % 2 + "")) {
                LOG.info("KEY【{}】映射数据库【{}】", shardingValue.getValue(), each);
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}
