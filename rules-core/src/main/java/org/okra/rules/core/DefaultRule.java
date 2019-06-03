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
        return listeners.toArray(new Watcher[0]);
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
