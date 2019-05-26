package org.okra.rules.core.api;

/**
 * @author TinyZ.
 * @since 2019.03.05
 */
public interface Identify {

    /**
     * @return the component's unique id.
     */
    default String id() {
        return this.toString();
    }
}
