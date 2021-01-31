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

package org.okra.rules.support.serial;

import org.okra.rules.core.RuleContext;
import org.okra.rules.core.RulesEngine;
import org.okra.rules.core.api.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serial Rule Engine. Thread-safe. Make sure rule execute serial by unique key.
 *
 * @author TinyZ.
 * @since 2019.03.06
 */
public abstract class SerialRulesEngine extends RulesEngine {

    private static final Logger LOG = LoggerFactory.getLogger(SerialRulesEngine.class);

    protected Map<Object, SerialRuleRunWorker> runWorkerMap = new ConcurrentHashMap<>();

    @Override
    public void fire(Rule rule, RuleContext context) {
        SerialRuleRunWorker observer = getSerialRunObserver(context);
        observer.offer(this, rule, context);
    }

    public void onFireImpl(Rule rule, RuleContext ctx) {
        super.fireImpl(rule, ctx);
    }

    private SerialRuleRunWorker getSerialRunObserver(RuleContext context) {
        Object key = getSerialRunWorkerKey(context);
        Objects.requireNonNull(key);
        return runWorkerMap.computeIfAbsent(key, o -> newSerialRunWorker(context));
    }

    public void invalidate(RuleContext context) {
        this.invalidate(getSerialRunWorkerKey(context));
    }

    public void invalidate(Object key) {
        Objects.requireNonNull(key);
        runWorkerMap.remove(key);
    }

    public void invalidateAll() {
        runWorkerMap.clear();
    }

    public abstract Object getSerialRunWorkerKey(RuleContext context);

    /**
     * Should be implement by real logic.
     */
    public abstract SerialRuleRunWorker newSerialRunWorker(RuleContext context);
}
