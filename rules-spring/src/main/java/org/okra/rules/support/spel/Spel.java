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
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * SpEL utilities.
 * <p>
 * Use "@{" and "}" replace spring framework's default expression context "#{" prefix and "}" suffix.
 *
 * @author TinyZ.
 * @version 2019.05.25
 */
public final class Spel {

    private static final String EXPRESSION_PREFIX = "@{";
    private static final String EXPRESSION_SUFFIX = "}";
    private static SpelParserConfiguration spelParserConfiguration;
    private static SpelExpressionParser spelParser;
    private static ParserContext parserContext;

    private Spel() {
        //  no-op
    }

    public static void checkIllegalSpelContext(RuleContext ctx) {
        if (ctx == null || ctx instanceof SpelRuleContext)
            return;
        throw new IllegalArgumentException("ctx not SpEL context. actual type:" + ctx.getClass());
    }

    public static void setSpelParserConfiguration(SpelParserConfiguration spelParserConfiguration) {
        Spel.spelParserConfiguration = spelParserConfiguration;
    }

    public static SpelParserConfiguration getSpelParserConfiguration() {
        if (spelParserConfiguration == null) {
            spelParserConfiguration = new SpelParserConfiguration();
        }
        return spelParserConfiguration;
    }

    public static void setSpelParser(SpelExpressionParser spelParser) {
        Spel.spelParser = spelParser;
    }

    public static SpelExpressionParser getSpelParser() {
        if (null == spelParser) {
            spelParser = new SpelExpressionParser(getSpelParserConfiguration());
        }
        return spelParser;
    }

    public static void setParserContext(ParserContext parserContext) {
        Spel.parserContext = parserContext;
    }

    public static ParserContext getParserContext() {
        if (parserContext == null) {
            parserContext = new TemplateParserContext(EXPRESSION_PREFIX, EXPRESSION_SUFFIX);
        }
        return parserContext;
    }
}
