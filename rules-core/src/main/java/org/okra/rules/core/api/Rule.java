package org.okra.rules.core.api;


import org.okra.rules.core.RuleContext;

import java.io.Serializable;

/**
 * @author TinyZ.
 * @since 2019.03.06
 */
public interface Rule extends Identify, Comparable<Rule>, Serializable {

    /**
     * Default rule identify.
     */
    String DEFAULT_IDENTIFY = "rule";

    /**
     * Default rule description.
     */
    String DEFAULT_DESCRIPTION = "description";

    /**
     * Default rule priority.
     */
    int DEFAULT_PRIORITY = 0;

    /**
     * @return the rule's description. Default Value: {@link #id()}
     */
    default String getDescription() {
        return this.id();
    }

    /**
     * @return the rule's priority. Default Value: 0
     */
    default int getPriority() {
        return 0;
    }

    /**
     * Rule conditions abstraction : this method encapsulates the rule's conditions.
     * <strong>Implementations should handle any runtime exception and return true/false accordingly</strong>
     *
     * @return true if the rule should be applied given the provided facts, false otherwise. Default impl : return true
     */
    default boolean evaluate(RuleContext context) {
        return true;
    }

    /**
     * Rule actions abstraction : this method encapsulates the rule's actions.
     *
     * @throws Exception thrown if an exception occurs during actions performing
     */
    boolean execute(RuleContext context) throws Exception;

    /**
     * Rule listener abstraction : this method encapsulates the rule's listeners.
     *
     * @return rule listener list if rule has any listener. otherwise null.
     */
    default Watcher[] getWatchers() {
        return null;
    }

}
