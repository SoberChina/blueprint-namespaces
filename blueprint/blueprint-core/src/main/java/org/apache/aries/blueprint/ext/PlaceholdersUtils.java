/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.ext;

import org.apache.aries.blueprint.ComponentDefinitionRegistry;
import org.apache.aries.blueprint.metadata.ExtensibleMetadata;
import org.osgi.service.blueprint.container.ComponentDefinitionException;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.ValueMetadata;

/**
 * Utility for placeholders parsing / validation
 *
 * @version $Rev: 896324 $, $Date: 2010-01-06 06:05:04 +0000 (Wed, 06 Jan 2010) $
 */
public class PlaceholdersUtils {

    public static void validatePlaceholder(BeanMetadata metadata, ComponentDefinitionRegistry registry) {
        String prefix = getPlaceholderProperty(metadata, "placeholderPrefix");
        String suffix = getPlaceholderProperty(metadata, "placeholderSuffix");
        for (String id : registry.getComponentDefinitionNames()) {
            ComponentMetadata component = registry.getComponentDefinition(id);
            
            if (component instanceof ExtensibleMetadata && component instanceof BeanMetadata) {
                ExtensibleMetadata ext = (ExtensibleMetadata) component;
                BeanMetadata bean = (BeanMetadata) component;
                
                Class<?> runtimeClass = (Class<?>) ext.retrieveCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.RUNTIME_CLASS_KEY);
                
                if (runtimeClass != null && AbstractPropertyPlaceholder.class.isAssignableFrom(runtimeClass)) {
                    String otherPrefix = getPlaceholderProperty(bean, "placeholderPrefix");
                    String otherSuffix = getPlaceholderProperty(bean, "placeholderSuffix");
                    if (prefix.equals(otherPrefix) && suffix.equals(otherSuffix)) {
                        throw new ComponentDefinitionException("Multiple placeholders with the same prefix and suffix are not allowed");
                    }
                }
            }
        }
    }

    private static String getPlaceholderProperty(BeanMetadata bean, String name) {
        for (BeanProperty property : bean.getProperties()) {
            if (name.equals(property.getName())) {
                ValueMetadata value = (ValueMetadata) property.getValue();
                return value.getStringValue();
            }
        }
        return null;
    }

}
