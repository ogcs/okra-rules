/*
 *
 *
 *          Copyright (c) 2021. - TinyZ.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.okra.rules.support.spring;

import org.okra.rules.annotation.Rule;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.Set;
import java.util.stream.Stream;

public class ClassPathRuleScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathRuleScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public ClassPathRuleScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public ClassPathRuleScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public ClassPathRuleScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    public void registerFilters() {
        //  1. Rule annotation.
        this.addIncludeFilter((metadataReader, metadataReaderFactory) -> metadataReader.getAnnotationMetadata().hasAnnotation(Rule.class.getName()));
        //  2. this class implement Rule interface.
        this.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            if (metadataReader.getClassMetadata().isConcrete()
                    && metadataReader.getClassMetadata().hasSuperClass()) {
                return Stream.of(metadataReader.getClassMetadata().getInterfaceNames())
                        .anyMatch(clzName -> {
                            if (org.okra.rules.core.api.Rule.class.getName().equalsIgnoreCase(clzName)) {
                                return true;
                            }
                            try {
                                Class<?> clzOfInterface = Class.forName(clzName);
                                return org.okra.rules.core.api.Rule.class.isAssignableFrom(clzOfInterface);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            return false;
                        });
            }
            return false;
        });
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    public boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return super.checkCandidate(beanName, beanDefinition);
    }

    @Override
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        super.registerBeanDefinition(definitionHolder, registry);
    }
}
