package org.okra.rules.spring;

import org.okra.rules.core.Rules;
import org.okra.rules.util.Reflects;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author TinyZ.
 * @version 2019.04.20
 */
public class RuleFactoryBean<R> implements FactoryBean<R> {

    private Class<?> clzOfRule;

    public RuleFactoryBean() {
    }

    public RuleFactoryBean(Class<?> clzOfRule) {
        this.clzOfRule = clzOfRule;
    }

    @Override
    public R getObject() throws Exception {
        return (R) Rules.asRule(Reflects.newInstance(clzOfRule));
    }

    @Override
    public Class<?> getObjectType() {
        return clzOfRule;
    }
}
