package org.okra.rules.core;

import org.okra.rules.core.api.Rule;

import java.util.Objects;

/**
 * @author TinyZ.
 * @since 2019.03.06
 */
public abstract class BasicRule implements Rule {

    private static final long serialVersionUID = 4314467409837884460L;
    /**
     * the unique identify.
     */
    private final String identify;
    /**
     * the rule's description.
     */
    private final String description;
    /**
     * the rule's priority.
     */
    private final int priority;

    public BasicRule() {
        this(Rule.DEFAULT_IDENTIFY, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public BasicRule(String identify) {
        this(identify, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public BasicRule(String identify, String description) {
        this(identify, description, Rule.DEFAULT_PRIORITY);
    }

    public BasicRule(String identify, String description, int priority) {
        this.identify = identify;
        this.priority = priority;
        this.description = description;
    }

    @Override
    public String id() {
        return this.identify;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(final Rule rule) {
        if (getPriority() == rule.getPriority()) {
            return this.id().compareTo(rule.id());
        } else {
            return this.getPriority() - rule.getPriority();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicRule basicRule = (BasicRule) o;
        return priority == basicRule.priority &&
                Objects.equals(identify, basicRule.identify) &&
                Objects.equals(description, basicRule.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identify, description, priority);
    }

    @Override
    public String toString() {
        return "BasicRule{" +
                "identify='" + identify + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }
}
