package org.okra.rules.examples.autowired;

import org.okra.rules.annotation.Action;
import org.okra.rules.annotation.Condition;
import org.okra.rules.annotation.Param;
import org.okra.rules.annotation.Rule;

/**
 * @author TinyZ.
 * @version 2019.05.26
 */
@Rule(identify = "mock 1", description = "desc 1", priority = 1)
public class MockRule {

    @Condition
    public boolean isAccepted() {
        System.out.println("isAccepted. ");
        return true;
    }

    @Action()
    public void doAction(@Param("xx") String xx) {
        System.out.println("doAction. xx:" + xx);
    }
}
