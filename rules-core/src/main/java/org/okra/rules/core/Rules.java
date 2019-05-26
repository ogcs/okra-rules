package org.okra.rules.core;

import org.okra.rules.core.api.Action;
import org.okra.rules.core.api.Condition;
import org.okra.rules.core.api.Rule;
import org.okra.rules.core.api.Watcher;

import java.lang.reflect.Proxy;
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
            result = (Rule) Proxy.newProxyInstance(
                    Rule.class.getClassLoader(),
                    new Class[]{Rule.class, Comparable.class},
                    ruleProxy);
        }
        return result;
    }
}
