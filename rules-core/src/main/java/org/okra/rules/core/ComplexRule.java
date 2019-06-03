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
