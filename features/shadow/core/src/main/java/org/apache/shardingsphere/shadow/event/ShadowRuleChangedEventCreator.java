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
 */

package org.apache.shardingsphere.shadow.event;

import org.apache.shardingsphere.infra.rule.event.GovernanceEvent;
import org.apache.shardingsphere.mode.event.DataChangedEvent;
import org.apache.shardingsphere.mode.event.NamedRuleItemChangedEventCreator;
import org.apache.shardingsphere.mode.event.UniqueRuleItemChangedEventCreator;
import org.apache.shardingsphere.mode.spi.RuleChangedEventCreator;
import org.apache.shardingsphere.shadow.metadata.nodepath.ShadowRuleNodePathProvider;
import org.apache.shardingsphere.shadow.subscriber.DefaultShadowAlgorithmNameChangedGenerator;
import org.apache.shardingsphere.shadow.subscriber.ShadowAlgorithmChangedGenerator;
import org.apache.shardingsphere.shadow.subscriber.ShadowDataSourceChangedGenerator;
import org.apache.shardingsphere.shadow.subscriber.ShadowTableChangedGenerator;

/**
 * Shadow rule changed event creator.
 */
public final class ShadowRuleChangedEventCreator implements RuleChangedEventCreator {
    
    @Override
    public GovernanceEvent create(final String databaseName, final DataChangedEvent event, final String itemType, final String itemName) {
        return new NamedRuleItemChangedEventCreator().create(databaseName, itemName, event, getNamedRuleItemConfigurationChangedGeneratorType(itemType));
    }
    
    @Override
    public GovernanceEvent create(final String databaseName, final DataChangedEvent event, final String itemType) {
        return new UniqueRuleItemChangedEventCreator().create(databaseName, event, getUniqueRuleItemConfigurationChangedGeneratorType(itemType));
    }
    
    private String getNamedRuleItemConfigurationChangedGeneratorType(final String itemType) {
        switch (itemType) {
            case ShadowRuleNodePathProvider.DATA_SOURCES:
                return ShadowDataSourceChangedGenerator.TYPE;
            case ShadowRuleNodePathProvider.TABLES:
                return ShadowTableChangedGenerator.TYPE;
            case ShadowRuleNodePathProvider.ALGORITHMS:
                return ShadowAlgorithmChangedGenerator.TYPE;
            default:
                throw new UnsupportedOperationException(itemType);
        }
    }
    
    private String getUniqueRuleItemConfigurationChangedGeneratorType(final String itemType) {
        if (itemType.equals(ShadowRuleNodePathProvider.DEFAULT_ALGORITHM)) {
            return DefaultShadowAlgorithmNameChangedGenerator.TYPE;
        }
        throw new UnsupportedOperationException(itemType);
    }
    
    @Override
    public String getType() {
        return "shadow";
    }
}
