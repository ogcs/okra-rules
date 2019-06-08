package org.okra.rules.support.spel;

import org.okra.rules.core.BasicRule;
import org.okra.rules.core.RuleContext;
import org.okra.rules.core.api.Action;
import org.okra.rules.core.api.Condition;
import org.okra.rules.core.api.Watcher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ParserContext;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author TinyZ.
 * @version 2019.05.25
 */
public class SpelRule extends BasicRule {

    private static final long serialVersionUID = 652698741134247214L;
    private ParserContext parserContext;
    private Condition condition;
    private Action[] actions;
    private Watcher[] watchers;

    public SpelRule() {
        //  empty
    }

    public SpelRule(String identify) {
        super(identify);
    }

    public SpelRule(String identify, String description) {
        super(identify, description);
    }

    public SpelRule(String identify, String description, int priority) {
        super(identify, description, priority);
    }

    public SpelRule(String identify, String description, int priority, ParserContext parserContext) {
        super(identify, description, priority);
        this.parserContext = parserContext;
    }

    /**
     * If "parserContext" is exists. this field must initialized before others.
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void setParserContext(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setConditionExpression(String expression) {
        Objects.requireNonNull(this.parserContext, "parserContext");
        this.condition = new SpelCondition(this.parserContext, expression);
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public void setActionExpressions(String[] expressionArray) {
        if (expressionArray == null || expressionArray.length <= 0)
            return;
        Objects.requireNonNull(this.parserContext, "parserContext");
        this.actions = Arrays.stream(expressionArray)
                .filter(expression -> !expression.isEmpty())
                .map(expression -> new SpelAction(this.parserContext, expression))
                .toArray(Action[]::new);
    }

    public void setWatchers(Watcher[] watchers) {
        this.watchers = watchers;
    }

    @Override
    public boolean evaluate(RuleContext context) {
        return condition.evaluate(context);
    }

    @Override
    public void execute(RuleContext context) throws Exception {
        for (Action action : actions) {
            action.execute(context);
        }
    }

    @Override
    public Watcher[] getWatchers() {
        return watchers;
    }
}
