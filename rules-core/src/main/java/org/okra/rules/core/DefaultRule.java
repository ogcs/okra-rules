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

import java.util.List;

/**
 * @author TinyZ.
 * @since 2019.03.06
 */
public class DefaultRule extends BasicRule {

    private static final long serialVersionUID = -1108554704851596391L;
    /**
     * rule condition.
     */
    private final Condition condition;
    /**
     * rule action's list.
     */
    private final List<Action> actions;
    /**
     * rule watcher's list.
     */
    private final List<Watcher> listeners;

    public DefaultRule(Condition condition, List<Action> actions, List<Watcher> listeners) {
        this(Rule.DEFAULT_IDENTIFY, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY, condition, actions, listeners);
    }

    public DefaultRule(String identify, Condition condition, List<Action> actions, List<Watcher> listeners) {
        this(identify, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY, condition, actions, listeners);
    }

    public DefaultRule(String identify, String description, Condition condition, List<Action> actions, List<Watcher> listeners) {
        this(identify, description, Rule.DEFAULT_PRIORITY, condition, actions, listeners);
    }

    public DefaultRule(String identify, String description, int priority, Condition condition,
                       List<Action> actions, List<Watcher> listeners) {
        super(identify, description, priority);
        this.condition = condition;
        this.actions = actions;
        this.listeners = listeners;
    }

    @Override
    public boolean evaluate(RuleContext context) {
        return condition == null
                || condition.evaluate(context);
    }

    @Override
    public void execute(RuleContext context) throws Exception {
        if (null != this.actions) {
            for (Action action : this.actions) {
                action.execute(context);
            }
        }
    }

    public Watcher[] getWatchers() {
        return listeners.toArray(Watcher.EMPTY);
    }

    @Override
    public String toString() {
        return "DefaultRule{" +
                "condition=" + condition +
                ", actions=" + actions +
                ", listeners=" + listeners +
                "} " + super.toString();
    }
}
