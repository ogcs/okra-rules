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
import org.okra.rules.core.api.Rule;
import org.okra.rules.core.api.Watcher;
import org.okra.rules.function.TriConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author TinyZ.
 * @since 2019.03.05
 */
public final class RuleBuilder {

    private String identify = Rule.DEFAULT_IDENTIFY;
    private String description = Rule.DEFAULT_DESCRIPTION;
    private int priority = Rule.DEFAULT_PRIORITY;
    private Condition condition = Condition.FALSE;
    private List<Action> actions = new ArrayList<>();
    private List<Watcher> watchers = new ArrayList<>();

    private RuleBuilder() {
        //  no-op
    }

    public static RuleBuilder create() {
        return new RuleBuilder();
    }

    /**
     * Set rule identify.
     *
     * @param identify of the rule
     * @return the builder instance
     */
    public RuleBuilder identify(String identify) {
        this.identify = identify;
        return this;
    }

    public RuleBuilder identify(Enum<?> em) {
        this.identify = em.name();
        return this;
    }

    /**
     * Set rule description.
     *
     * @param description of the rule
     * @return the builder instance
     */
    public RuleBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set rule priority.
     *
     * @param priority of the rule
     * @return the builder instance
     */
    public RuleBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Set rule condition.
     *
     * @param condition of the rule
     * @return the builder instance
     */
    public RuleBuilder when(Condition condition) {
        this.condition = null == this.condition
                ? condition
                : this.condition.and(condition);
        return this;
    }

    public RuleBuilder when(Condition... conditions) {
        if (conditions == null || conditions.length == 0)
            return this;
        else if (conditions.length == 1) {
            when(conditions[0]);
            return this;
        } else {
            Condition base = conditions[0];
            for (int i = 1; i < conditions.length; i++) {
                base = base.and(conditions[i]);
            }
            when(base);
        }
        return this;
    }

    public RuleBuilder whenAnd(Condition condition) {
        this.condition = this.condition.and(condition);
        return this;
    }

    public RuleBuilder whenOr(Condition condition) {
        this.condition = this.condition.or(condition);
        return this;
    }

    /**
     * Add an action to the rule.
     *
     * @param action to add
     * @return the builder instance
     */
    public RuleBuilder then(Action action) {
        this.actions.add(action);
        return this;
    }

    public RuleBuilder then(Action... actions) {
        this.actions.addAll(Arrays.asList(actions));
        return this;
    }

    public RuleBuilder watch(Watcher listener) {
        this.watchers.add(listener);
        return this;
    }

    public RuleBuilder watchBeforeEvaluate(BiFunction<Rule, RuleContext, Boolean> func) {
        watch(new Watcher() {
            @Override
            public boolean beforeEvaluate(Rule rule, RuleContext context) {
                return func.apply(rule, context);
            }
        });
        return this;
    }

    public RuleBuilder watchAfterEvaluate(TriConsumer<Rule, RuleContext, Boolean> consumer) {
        watch(new Watcher() {
            @Override
            public void afterEvaluate(Rule rule, RuleContext context, boolean evaluationResult) {
                consumer.accept(rule, context, evaluationResult);
            }
        });
        return this;
    }

    public RuleBuilder watchBeforeExecute(BiConsumer<Rule, RuleContext> consumer) {
        watch(new Watcher() {
            @Override
            public void beforeExecute(Rule rule, RuleContext context) {
                consumer.accept(rule, context);
            }
        });
        return this;
    }

    public RuleBuilder watchOnSuccess(BiConsumer<Rule, RuleContext> consumer) {
        watch(new Watcher() {
            @Override
            public void onSuccess(Rule rule, RuleContext context) {
                consumer.accept(rule, context);
            }
        });
        return this;
    }

    public RuleBuilder watchOnFailure(BiConsumer<Rule, RuleContext> consumer) {
        watch(new Watcher() {
            @Override
            public void onFailure(Rule rule, RuleContext context) {
                consumer.accept(rule, context);
            }
        });
        return this;
    }

    public RuleBuilder watchOnException(TriConsumer<Rule, RuleContext, Exception> consumer) {
        watch(new Watcher() {
            @Override
            public void onException(Rule rule, RuleContext context, Exception exception) {
                consumer.accept(rule, context, exception);
            }
        });
        return this;
    }

    /**
     * Create a new {@link Rule}.
     *
     * @return a new rule instance
     */
    public Rule build() {
        return new DefaultRule(identify, description, priority, condition, actions, watchers);
    }
}
