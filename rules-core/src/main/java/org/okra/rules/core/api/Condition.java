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

package org.okra.rules.core.api;

import org.okra.rules.core.RuleContext;

/**
 * This interface represents a rule's condition.
 *
 * @author TinyZ.
 * @since 2019.03.06
 */
public interface Condition {

    /**
     * Evaluate the condition according to the known params.
     *
     * @param context known when evaluating the rule.
     * @return true if the rule should be triggered, false otherwise
     */
    boolean evaluate(RuleContext context);

    /**
     * A NoOp {@link Condition} that always returns false.
     */
    Condition FALSE = context -> false;

    /**
     * A NoOp {@link Condition} that always returns true.
     */
    Condition TRUE = context -> true;

    default Condition and(Condition condition) {
        return (context) -> this.evaluate(context) && condition.evaluate(context);
    }

    default Condition or(Condition condition) {
        return (context) -> this.evaluate(context) || condition.evaluate(context);
    }
}
