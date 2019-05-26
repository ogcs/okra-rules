package org.okra.rules.support.spel;

import org.okra.rules.core.BasicRule;
import org.okra.rules.core.RuleContext;
import org.okra.rules.core.api.Watcher;
import org.springframework.expression.spel.standard.SpelExpression;

import java.util.Objects;

/**
 * @author TinyZ.
 * @version 2019.05.25
 */
public class SpelRule extends BasicRule {
    private static final long serialVersionUID = -5384415039230528459L;

    private SpelExpression evaluateExpression;
    private SpelExpression executeExpression;
    private Watcher[] watchers;

    public SpelRule(String identify) {
        super(identify);
    }

    public SpelRule(String identify, String description) {
        super(identify, description);
    }

    public SpelRule(String identify, String description, int priority) {
        super(identify, description, priority);
    }

    @Override
    public boolean evaluate(RuleContext context) {
        Spel.checkIllegalSpelContext(context);
        Objects.requireNonNull(evaluateExpression, "SpEL with #evaluate() is null.");
        Boolean result = evaluateExpression.getValue(((SpelRuleContext) context).getEvaluationContext(), Boolean.class);
        return result == null || result;
    }

    @Override
    public boolean execute(RuleContext context) throws Exception {
        Spel.checkIllegalSpelContext(context);
        Objects.requireNonNull(executeExpression, "SpEL with #execute() is null.");
        Boolean result = executeExpression.getValue(((SpelRuleContext) context).getEvaluationContext(), Boolean.class);
        return result == null || result;
    }

    @Override
    public Watcher[] getWatchers() {
        return watchers;
    }

    public void setEvaluateExpression(String expression) {
        this.evaluateExpression = Spel.getSpelParser().parseRaw(expression);
    }

    public void setExecuteExpression(String expression) {
        this.executeExpression = Spel.getSpelParser().parseRaw(expression);
    }

    public void setWatchers(Watcher[] watchers) {
        this.watchers = watchers;
    }
}
