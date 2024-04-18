/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.server.analyzer.provider.trace.parser.listener;

import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.core.config.NamingControl;
import org.apache.skywalking.oap.server.core.source.Block;

public class BlockSourceBuilder {
    private final NamingControl namingControl;

    @Getter
    @Setter
    protected long timeBucket;

    @Setter
    @Getter
    private String serviceName;

    @Setter
    @Getter
    private String serviceInstanceName;

    @Setter
    @Getter
    private String peer;

    BlockSourceBuilder(final NamingControl namingControl) {
        this.namingControl = namingControl;
    }

    public Block toBlock() {
        Block block = new Block();
        block.setServiceName(this.serviceName);
        block.setServiceInstanceName(this.serviceInstanceName);
        block.setPeer(this.peer);
        block.setTimeBucket(timeBucket);
        return block;
    }
}
