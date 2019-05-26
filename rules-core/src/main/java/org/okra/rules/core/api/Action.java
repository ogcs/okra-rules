package org.okra.rules.core.api;

import org.okra.rules.core.RuleContext;

/**
 * @author TinyZ.
 * @since 2019.03.06
 */
public interface Action {

    void execute(RuleContext context) throws Exception;


    default Action next(Action after) {
        return (RuleContext context) -> {
            this.execute(context);
            after.execute(context);
        };
    }

    default Action prev(Action before) {
        return (RuleContext context) -> {
            before.execute(context);
            this.execute(context);
        };
    }

}
