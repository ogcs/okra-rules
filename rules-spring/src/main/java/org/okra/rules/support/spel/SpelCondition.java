package org.okra.rules.support.spel;

import org.okra.rules.core.RuleContext;
import org.okra.rules.core.api.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;

import java.util.Objects;

/**
 * @author TinyZ.
 * @version 2019.05.31
 */
public class SpelCondition implements Condition {

    private static final Logger LOG = LoggerFactory.getLogger(SpelCondition.class);
    private ParserContext context;
    private Expression compiledExpression;

    public SpelCondition() {
        this(Spel.getParserContext(), null);
    }

    public SpelCondition(String expression) {
        this(Spel.getParserContext(), expression);
    }

    public SpelCondition(ParserContext context, String expression) {
        this.context = context;
        if (null != expression) {
            this.setExpression(expression);
        }
    }

    public void setExpression(String expression) {
        Objects.requireNonNull(expression, "expression");
        this.compiledExpression = Spel.getSpelParser().parseExpression(expression, context);
    }

    @Override
    public boolean evaluate(RuleContext context) {
        Spel.checkIllegalSpelContext(context);
        Objects.requireNonNull(compiledExpression, "SpEL with #execute() is null.");
        try {
            EvaluationContext ctx = ((SpelRuleContext) context).getEvaluationContext();
            Boolean result = compiledExpression.getValue(ctx, Boolean.class);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            LOG.error("@Condition evaluate failed. expression:{}", this);
            throw e;
        }
    }

    @Override
    public String toString() {
        return "SpelCondition{" +
                "expression=" + compiledExpression.getExpressionString() +
                '}';
    }
}
