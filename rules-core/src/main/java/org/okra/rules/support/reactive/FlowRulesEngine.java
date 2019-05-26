package org.okra.rules.support.reactive;

import org.okra.rules.core.RulesEngine;
import org.okra.rules.support.serial.FireEvent;

import java.util.concurrent.Flow;

/**
 * @author TinyZ.
 * @version 2019.05.19
 */
public class FlowRulesEngine extends RulesEngine {

    Flow.Publisher<FireEvent> publisher;
    Flow.Processor<FireEvent, Boolean> processor;

    public void test() {

    }


}
