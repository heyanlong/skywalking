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

package org.apache.skywalking.oap.server.core.analysis.manual.trace;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.core.analysis.IDManager;
import org.apache.skywalking.oap.server.core.analysis.MetricsExtension;
import org.apache.skywalking.oap.server.core.analysis.Stream;
import org.apache.skywalking.oap.server.core.analysis.metrics.Metrics;
import org.apache.skywalking.oap.server.core.analysis.worker.MetricsStreamProcessor;
import org.apache.skywalking.oap.server.core.remote.grpc.proto.RemoteData;
import org.apache.skywalking.oap.server.core.source.DefaultScopeDefine;
import org.apache.skywalking.oap.server.core.storage.StorageID;
import org.apache.skywalking.oap.server.core.storage.annotation.Column;
import org.apache.skywalking.oap.server.core.storage.type.Convert2Entity;
import org.apache.skywalking.oap.server.core.storage.type.Convert2Storage;
import org.apache.skywalking.oap.server.core.storage.type.StorageBuilder;

@Stream(name = BlockTraffic.INDEX_NAME, scopeId = DefaultScopeDefine.BLOCK,
        builder = BlockTraffic.Builder.class, processor = MetricsStreamProcessor.class)
@MetricsExtension(supportDownSampling = false, supportUpdate = false)
@EqualsAndHashCode(of = {
        "service_name",
        "service_instance_name",
        "peer",
})
public class BlockTraffic extends Metrics {

    public static final String INDEX_NAME = "block_traffic";

    public static final String PEER = "peer";
    public static final String SERVICE_NAME = "service_name";
    public static final String SERVICE_INSTANCE_NAME = "service_instance_name";

    @Setter
    @Getter
    @Column(name = SERVICE_NAME)
    private String serviceName;

    @Setter
    @Getter
    @Column(name = SERVICE_INSTANCE_NAME)
    private String serviceInstanceName;

    @Setter
    @Getter
    @Column(name = PEER)
    private String peer;

    @Override
    public boolean combine(Metrics metrics) {
        return true;
    }

    @Override
    public void calculate() {

    }

    @Override
    public Metrics toHour() {
        return null;
    }

    @Override
    public Metrics toDay() {
        return null;
    }

    @Override
    protected StorageID id0() {
        String id = IDManager.BlockID.buildId(serviceName, serviceInstanceName, peer);
        return new StorageID().appendMutant(new String[] {
                SERVICE_NAME, SERVICE_INSTANCE_NAME, PEER
        }, id);
    }

    @Override
    public int remoteHashCode() {
        return this.hashCode();
    }

    @Override
    public void deserialize(RemoteData remoteData) {
        setServiceName(remoteData.getDataStrings(1));
        setServiceInstanceName(remoteData.getDataStrings(2));
        setPeer(remoteData.getDataStrings(3));
        setTimeBucket(remoteData.getDataLongs(0));
    }

    @Override
    public RemoteData.Builder serialize() {
        final RemoteData.Builder builder = RemoteData.newBuilder();
        builder.addDataStrings(serviceName);
        builder.addDataStrings(serviceInstanceName);
        builder.addDataStrings(peer);
        builder.addDataLongs(getTimeBucket());
        return builder;
    }

    public static class Builder implements StorageBuilder<BlockTraffic> {

        @Override
        public BlockTraffic storage2Entity(final Convert2Entity converter) {
            final BlockTraffic blockTraffic = new BlockTraffic();
            blockTraffic.setServiceName((String) converter.get(SERVICE_NAME));
            blockTraffic.setServiceInstanceName((String) converter.get(SERVICE_INSTANCE_NAME));
            blockTraffic.setPeer((String) converter.get(PEER));
            blockTraffic.setTimeBucket((Long) converter.get(TIME_BUCKET));
            return blockTraffic;
        }

        @Override
        public void entity2Storage(final BlockTraffic storageData, final Convert2Storage converter) {
            converter.accept(SERVICE_NAME, storageData.getServiceName());
            converter.accept(SERVICE_INSTANCE_NAME, storageData.getServiceInstanceName());
            converter.accept(PEER, storageData.getPeer());
            converter.accept(TIME_BUCKET, storageData.getTimeBucket());
        }
    }
}
