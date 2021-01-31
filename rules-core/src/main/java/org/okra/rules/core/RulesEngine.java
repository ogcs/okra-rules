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


import org.okra.rules.core.api.Rule;
import org.okra.rules.core.api.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Thread-safe.
 * @author TinyZ.
 * @since 2019.03.06
 */
public class RulesEngine {

    private static final Logger LOG = LoggerFactory.getLogger(RulesEngine.class);

    /**
     * Rule Book. Manage all rule defined in this engine.
     */
    private ConcurrentHashMap<String/* rule id */, Rule> rulebook = new ConcurrentHashMap<>();
    /**
     * Global watcher.
     */
    private Set<Watcher> globalWatcherSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private ExecutorService executor;

    public RulesEngine() {
        this(ForkJoinPool.commonPool());
    }

    public RulesEngine(ExecutorService executor) {
        this.executor = executor;
    }

    public ExecutorService executor() {
        return this.executor;
    }

    public void addRule(Rule rule) {
        Objects.requireNonNull(rule);
        addRule(rule.id(), rule);
    }

    public void addRule(String identify, Rule rule) {
        Objects.requireNonNull(rule);
        Rule prev = rulebook.putIfAbsent(identify, rule);
        if (prev != null) {
            LOG.info("the rule id:{} has exists. previous info: {}", prev.id(), prev);
        }
    }

    public void addRule(String identify, Object ruleImpl) {
        Objects.requireNonNull(ruleImpl);
        Rule prev = rulebook.putIfAbsent(identify, Rules.asRule(ruleImpl));
        if (prev != null) {
            LOG.info("the rule id:{} has exists. previous info: {}", prev.id(), prev);
        }
    }

    public void removeRule(Rule rule) {
        Objects.requireNonNull(rule);
        removeRule(rule.id());
    }

    public void removeRule(String identify) {
        Rule prev = rulebook.remove(identify);
        if (prev != null) {
            LOG.info("the rule id:{} has bean removed. rule info: {}", prev.id(), prev);
        }
    }

    public CompletableFuture<RuleContext> fireAsync(final String page, final RuleContext ctx) {
        return CompletableFuture.supplyAsync(() -> {
            fire(page, ctx);
            return ctx;
        }, executor());
    }

    public void fire(String page) {
        fire(page, null);
    }

    public void fire(String page, RuleContext context) {
        Rule rule = rulebook.get(page);
        if (null == rule) {
            return;
        }
        this.fire(rule, context);
    }

    public void fire(Rule rule, RuleContext context) {
        this.fireImpl(rule, context);
    }

    /**
     * the real fire logic will be invoked.
     *
     * @param rule    the fired rule
     * @param context thr rule execute's context.
     */
    protected void fireImpl(Rule rule, RuleContext context) {
        if (!fireBeforeEvaluate(rule, context)) {
            return;
        }
        boolean evaluateResult = rule.evaluate(context);
        fireAfterEvaluate(rule, context, evaluateResult);
        if (evaluateResult) {
            try {
                fireBeforeExecute(rule, context);
                rule.execute(context);
                fireOnSuccess(rule, context);
            } catch (Exception e) {
                fireOnException(rule, context, e);
                LOG.error("call fire(). rule:{} throws exception.", rule.id(), e);
            }
        } else {
            fireOnFailure(rule, context);
        }
    }

    /// <editor-fold desc="Define Rule Watcher" defaultstate="collapsed">

    /**
     * All global watcher.
     */
    public RulesEngine watch(Watcher listener) {
        this.globalWatcherSet.add(listener);
        return this;
    }

    private Collection<Watcher> findListeners(Rule rule) {
        Watcher[] listeners = rule.getWatchers();
        if (null == listeners || listeners.length <= 0)
            return Collections.unmodifiableCollection(globalWatcherSet);
        else {
            List<Watcher> list = Arrays.asList(listeners);
            list.addAll(globalWatcherSet);
            return Collections.unmodifiableCollection(list);
        }
    }

    private boolean fireBeforeEvaluate(Rule rule, RuleContext context) {
        for (Watcher listener : findListeners(rule)) {
            try {
                if (!listener.beforeEvaluate(rule, context)) {
                    return false;
                }
            } catch (Exception e) {
                LOG.error("fireBeforeEvaluate. rule:{} throws exception.", rule.id(), e);
                return false;
            }
        }
        return true;
    }

    private void fireAfterEvaluate(Rule rule, RuleContext context, boolean evaluationResult) {
        for (Watcher listener : findListeners(rule)) {
            try {
                listener.afterEvaluate(rule, context, evaluationResult);
            } catch (Exception e) {
                LOG.error("fireAfterEvaluate. rule:{} throws exception.", rule.id(), e);
            }
        }
    }

    private void fireBeforeExecute(Rule rule, RuleContext context) {
        for (Watcher listener : findListeners(rule)) {
            try {
                listener.beforeExecute(rule, context);
            } catch (Exception e) {
                LOG.error("fireBeforeExecute. rule:{} throws exception.", rule.id(), e);
            }
        }
    }

    private void fireOnSuccess(Rule rule, RuleContext context) {
        Collection<Watcher> listeners = findListeners(rule);
        for (Watcher listener : listeners) {
            try {
                listener.onSuccess(rule, context);
            } catch (Exception e) {
                LOG.error("fireOnSuccess. rule:{} throws exception.", rule.id(), e);
            }
        }
    }

    private void fireOnFailure(Rule rule, RuleContext context) {
        for (Watcher listener : findListeners(rule)) {
            try {
                listener.onFailure(rule, context);
            } catch (Exception e) {
                LOG.error("fireOnFailure. rule:{} throws exception.", rule.id(), e);
            }
        }
    }

    private void fireOnException(Rule rule, RuleContext context, Exception exception) {
        for (Watcher listener : findListeners(rule)) {
            try {
                listener.onException(rule, context, exception);
            } catch (Exception e) {
                LOG.error("fireOnException. rule:{} throws exception.", rule.id(), e);
            }
        }
    }

    /// </editor-fold>
}
