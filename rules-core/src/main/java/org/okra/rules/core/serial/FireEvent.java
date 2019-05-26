package org.okra.rules.core.serial;

import org.okra.rules.core.RuleContext;
import org.okra.rules.core.api.Rule;

/**
 * @author TinyZ.
 * @version 2019.03.16
 */
public class FireEvent {

    private final SerialRulesEngine engine;
    private final Rule rule;
    private final RuleContext ctx;

    public FireEvent(SerialRulesEngine engine, Rule rule, RuleContext ctx) {
        this.engine = engine;
        this.rule = rule;
        this.ctx = ctx;
    }

    public SerialRulesEngine getEngine() {
        return engine;
    }

    public Rule getRule() {
        return rule;
    }

    public RuleContext getCtx() {
        return ctx;
    }
}
