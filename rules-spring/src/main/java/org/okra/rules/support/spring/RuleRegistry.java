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