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

/**
 * Complex rule.
 *
 * @author TinyZ.
 * @since 2019.03.05
 */
public class ComplexRule extends BasicRule {

    private static final long serialVersionUID = 294326867446800971L;
    private final Rule[] rules;

    public ComplexRule(String identify, String description, int priority, Rule... rules) {
        super(identify, description, priority);
        this.rules = rules;
    }

    @Override
    public boolean evaluate(RuleContext context) {
        if (null != rules) {
            for (Rule rule : rules) {
                if (!rule.evaluate(context))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void execute(RuleContext context) throws Exception {
        if (null != rules) {
            for (Rule rule : rules) {
                rule.execute(context);
            }
        }
    }
}
