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
