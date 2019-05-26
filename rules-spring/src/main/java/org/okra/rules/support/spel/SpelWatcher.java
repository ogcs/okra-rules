package org.okra.rules.support.spel;

import org.okra.rules.core.RuleContext;
import org.okra.rules.core.api.Rule;
import org.okra.rules.core.api.Watcher;
import org.springframework.expression.spel.standard.SpelExpression;

/**
 * @author TinyZ.
 * @version 2019.05.25
 */
public class SpelWatcher implements Watcher {

    private SpelExpression beforeEvaluate;
    private SpelExpression afterEvaluate;
    private SpelExpression beforeExecute;
    private SpelExpression onSuccess;
    private SpelExpression onFailure;
    private SpelExpression onException;

    @Override
    public boolean beforeEvaluate(Rule rule, RuleContext context) {
        if (beforeEvaluate == null)
            return true;
        Spel.checkIllegalSpelContext(context);
        Boolean result = beforeEvaluate.getValue(((SpelRuleContext) context).getEvaluationContext(), Boolean.class);
        return result != null && result;
    }

    @Override
    public void afterEvaluate(Rule rule, RuleContext context, boolean evaluationResult) {
        if (null == afterEvaluate)
            return;
        Spel.checkIllegalSpelContext(context);
        afterEvaluate.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Override
    public void beforeExecute(Rule rule, RuleContext context) {
        if (null == beforeExecute)
            return;
        Spel.checkIllegalSpelContext(context);
        beforeExecute.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Override
    public void onSuccess(Rule rule, RuleContext context) {
        if (null == onSuccess)
            return;
        Spel.checkIllegalSpelContext(context);
        onSuccess.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Override
    public void onFailure(Rule rule, RuleContext context) {
        if (null == onFailure)
            return;
        Spel.checkIllegalSpelContext(context);
        onFailure.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Override
    public void onException(Rule rule, RuleContext context, Exception exception) {
        if (null == onException)
            return;
        Spel.checkIllegalSpelContext(context);
        onException.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    public void setBeforeEvaluate(String expression) {
        this.beforeEvaluate = Spel.getSpelParser().parseRaw(expression);
    }

    public void setAfterEvaluate(String expression) {
        this.afterEvaluate = Spel.getSpelParser().parseRaw(expression);
    }

    public void setBeforeExecute(String expression) {
        this.beforeExecute = Spel.getSpelParser().parseRaw(expression);
    }

    public void setOnSuccess(String expression) {
        this.onSuccess = Spel.getSpelParser().parseRaw(expression);
    }

    public void setOnFailure(String expression) {
        this.onFailure = Spel.getSpelParser().parseRaw(expression);
    }

    public void setOnException(String expression) {
        this.onException = Spel.getSpelParser().parseRaw(expression);
    }
}
