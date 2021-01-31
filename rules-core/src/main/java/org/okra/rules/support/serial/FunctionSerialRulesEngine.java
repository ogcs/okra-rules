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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Function;

/**
 * Serial Rule Engine. Thread-safe. Make sure rule execute serial by unique key.
 *
 * @author TinyZ.
 * @since 2019.03.06
 */
public class FunctionSerialRulesEngine extends SerialRulesEngine {

    private static final Logger LOG = LoggerFactory.getLogger(FunctionSerialRulesEngine.class);

    private final Function<RuleContext, Object> keyFunc;
    private final Function<RuleContext, SerialRuleRunWorker> workerFunc;

    protected FunctionSerialRulesEngine(Function<RuleContext, Object> keyFunc,
                                        Function<RuleContext, SerialRuleRunWorker> workerFunc) {
        Objects.requireNonNull(keyFunc);
        Objects.requireNonNull(workerFunc);
        this.keyFunc = keyFunc;
        this.workerFunc = workerFunc;
    }

    public Object getSerialRunWorkerKey(RuleContext context) {
        return keyFunc.apply(context);
    }

    /**
     * Should be implement by real logic.
     */
    public SerialRuleRunWorker newSerialRunWorker(RuleContext context) {
        return workerFunc.apply(context);
    }
}
