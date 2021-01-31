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
import org.okra.rules.core.api.Rule;

/**
 * @author TinyZ.
 * @version 2019.03.16
 */
public class FireEvent {

    private final SerialRulesEngine engine;
    private final Rule rule;
    private final RuleContext ctx;

    public FireEvent(SerialRulesEngine engine, Rule rule, RuleContext ctx) {
        this.engine = engine;
        this.rule = rule;
        this.ctx = ctx;
    }

    public SerialRulesEngine getEngine() {
        return engine;
    }

    public Rule getRule() {
        return rule;
    }

    public RuleContext getCtx() {
        return ctx;
    }
}
