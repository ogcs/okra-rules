package org.okra.rules.examples.autowired;

import org.okra.rules.core.api.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author TinyZ.
 * @version 2019.05.26
 */
@Component
public class MockComponent {

    @Autowired
    @Qualifier("mockRule")
    private MockRule rule;
    @Autowired
    private MockRule rule2;

}
