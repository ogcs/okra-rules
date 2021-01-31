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
 * @author TinyZ.
 * @since 2019.03.05
 */
public interface Watcher {

    Watcher[] EMPTY = new Watcher[0];

    /**
     * Triggered before the evaluation of a rule.
     *
     * @param rule    being evaluated
     * @param context known params before evaluating the rule
     * @return true if the rule should be evaluated, false otherwise
     */
    default boolean beforeEvaluate(Rule rule, RuleContext context) {
        return true;
    }

    /**
     * Triggered after the evaluation of a rule.
     *
     * @param rule             that has been evaluated
     * @param context          known params after evaluating the rule
     * @param evaluationResult true if the rule evaluated to true, false otherwise
     */
    default void afterEvaluate(Rule rule, RuleContext context, boolean evaluationResult) {

    }

    /**
     * Triggered before the execution of a rule.
     *
     * @param rule    the current rule
     * @param context known params before executing the rule
     */
    default void beforeExecute(Rule rule, RuleContext context) {

    }

    /**
     * Triggered after a rule has been executed successfully.
     *
     * @param rule    the current rule
     * @param context known params after executing the rule
     */
    default void onSuccess(Rule rule, RuleContext context) {

    }

    /**
     * Triggered after a rule has been executed failure.
     *
     * @param rule    the current rule
     * @param context known params after executing the rule
     */
    default void onFailure(Rule rule, RuleContext context) {

    }

    /**
     * Triggered after a rule has been executed throw exception.
     *
     * @param rule    the current rule
     * @param context known params after executing the rule
     */
    default void onException(Rule rule, RuleContext context, Exception exception) {

    }
}
