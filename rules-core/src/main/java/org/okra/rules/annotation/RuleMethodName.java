package org.okra.rules.annotation;

public enum RuleMethodName {
    /**
     * {@link org.okra.rules.core.api.Rule#id()}
     */
    ID,
    /**
     * {@link org.okra.rules.core.api.Rule#getDescription()}
     */
    GETDESCRIPTION,
    /**
     * {@link org.okra.rules.core.api.Rule#getPriority()}
     */
    GETPRIORITY,
    /**
     * {@link org.okra.rules.core.api.Rule#evaluate(org.okra.rules.core.RuleContext)}
     */
    EVALUATE,
    /**
     * {@link org.okra.rules.core.api.Rule#execute(org.okra.rules.core.RuleContext)}
     */
    EXECUTE,
    /**
     * {@link org.okra.rules.core.api.Rule#getWatchers()}
     */
    GETWATCHERS,

    /// Basic Object Method

    /**
     * Basic object method #equals().
     */
    EQUALS,
    /**
     * Basic object method #hashCode().
     */
    HASHCODE,
    /**
     * Basic object method #toString().
     */
    TOSTRING;
}
