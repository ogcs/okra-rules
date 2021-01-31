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

import org.okra.rules.core.Rules;
import org.okra.rules.core.RulesEngine;
import org.okra.rules.core.api.Rule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author TinyZ.
 * @version 2019.04.20
 */
public class RuleRegistry implements SmartInitializingSingleton, ApplicationContextAware {

    /**
     * the rule engine.
     */
    private final RulesEngine engine;

    private ApplicationContext ctx;

    public RuleRegistry(RulesEngine engine) {
        this.engine = engine;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Set<Rule> beans = new HashSet<>();
        Map<String, Object> beansOfAnnotation = this.ctx.getBeansWithAnnotation(org.okra.rules.annotation.Rule.class);
        if (!beansOfAnnotation.isEmpty()) {
            beans.addAll(
                    beansOfAnnotation.values().stream()
                            .map(Rules::asRule)
                            .collect(Collectors.toList())
            );
        }
        Map<String, Rule> beansOfType = this.ctx.getBeansOfType(Rule.class);
        if (!beansOfType.isEmpty()) {
            beans.addAll(beansOfType.values());
        }
        processRuleBeans(beans);
    }

    protected void processRuleBeans(Set<Rule> beans) {
        beans.forEach(engine::addRule);
    }
}