/*
 *
 *
 *          Copyright (c) 2021. - TinyZ.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.okra.rules.support.spel;

import org.okra.rules.core.RuleContext;
import org.okra.rules.core.api.Action;
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
public class SpelAction implements Action {

    private static final Logger LOG = LoggerFactory.getLogger(SpelAction.class);
    private ParserContext context;
    private Expression compiledExpression;

    public SpelAction() {
        this(Spel.getParserContext(), null);
    }

    public SpelAction(String expression) {
        this(Spel.getParserContext(), expression);
    }

    public SpelAction(ParserContext context, String expression) {
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
    public void execute(RuleContext context) throws Exception {
        Spel.checkIllegalSpelContext(context);
        Objects.requireNonNull(compiledExpression, "SpEL with #execute() is null.");
        try {
            EvaluationContext ctx = ((SpelRuleContext) context).getEvaluationContext();
            compiledExpression.getValue(ctx);
        } catch (Exception e) {
            LOG.error("@Action execute failed. expression:{}", this);
            throw e;
        }
    }

    @Override
    public String toString() {
        return "SpelAction{" +
                "expression=" + compiledExpression.getExpressionString() +
                '}';
    }
}
