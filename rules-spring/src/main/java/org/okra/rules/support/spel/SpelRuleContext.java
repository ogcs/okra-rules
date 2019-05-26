package org.okra.rules.support.spel;

import org.okra.rules.core.RuleContext;
import org.springframework.expression.EvaluationContext;

/**
 * @author TinyZ.
 * @version 2019.05.25
 */
public class SpelRuleContext extends RuleContext {

    private EvaluationContext spelContext;

    @Override
    public RuleContext with(String key, Object obj) {
        getEvaluationContext().setVariable(key, obj);
        return super.with(key, obj);
    }

    public EvaluationContext getEvaluationContext() {
        if (spelContext == null) {
            spelContext = Spel.newEvaluationContext();
        }
        return spelContext;
    }
}
