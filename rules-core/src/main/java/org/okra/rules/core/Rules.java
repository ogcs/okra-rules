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

package org.okra.rules.core;

import org.okra.rules.core.api.Action;
import org.okra.rules.core.api.Condition;
import org.okra.rules.core.api.Identify;
import org.okra.rules.core.api.Rule;
import org.okra.rules.core.api.Watcher;

import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TinyZ.
 * @since 2019.03.06
 */
public abstract class Rules {

    private Rules() {
        //  no-op
    }

    public static RuleBuilder newBuilder() {
        return RuleBuilder.create();
    }

    public static Rule of(String identify, String description, int priority) {
        return of(identify, description, priority, Condition.TRUE, null);
    }

    public static Rule of(Condition condition, List<Action> actions) {
        return of(Rule.DEFAULT_IDENTIFY, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY, condition, actions, null);
    }

    public static Rule of(String identify, String description, int priority, Condition condition, List<Action> actions) {
        return new DefaultRule(identify, description, priority, condition, actions, null);
    }

    public static Rule of(Condition condition, List<Action> actions, List<Watcher> listeners) {
        return of(Rule.DEFAULT_IDENTIFY, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY, condition, actions, listeners);
    }

    public static Rule of(String identify, String description, int priority, Condition condition, List<Action> actions, List<Watcher> listeners) {
        return new DefaultRule(identify, description, priority, condition, actions, listeners);
    }

    public static Rule complex(String identify, String description, int priority, Rule... rules) {
        return new ComplexRule(identify, description, priority, rules);
    }

    public static Rule complex(Rule... rules) {
        return new ComplexRule(Rule.DEFAULT_IDENTIFY, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY, rules);
    }


    public static Rule asRule(final Object rule) {
        Rule result;
        if (rule instanceof Rule) {
            result = (Rule) rule;
        } else {
            RuleDefinitionValidator.create().validateRuleDefinition(rule);
            BasicRuleProxy ruleProxy = new BasicRuleProxy(rule);
            ruleProxy.initialize();

            List<Class<?>> proxyClazzAry = new ArrayList<>();
            proxyClazzAry.add(Rule.class);
            Class<?>[] interfaces = rule.getClass().getInterfaces();
            if (interfaces != null && interfaces.length > 0) {
                proxyClazzAry.addAll(Arrays.asList(interfaces));
            }
            result = (Rule) Proxy.newProxyInstance(
                    Rule.class.getClassLoader(),
                    proxyClazzAry.toArray(new Class[0]),
                    ruleProxy);
        }
        return result;
    }
}
