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

package org.okra.rules.core.api;


import org.okra.rules.core.RuleContext;

import java.io.Serializable;

/**
 * @author TinyZ.
 * @since 2019.03.06
 */
public interface Rule extends Identify, Comparable<Rule>, Serializable {

    /**
     * Default rule identify.
     */
    String DEFAULT_IDENTIFY = "rule";

    /**
     * Default rule description.
     */
    String DEFAULT_DESCRIPTION = "description";

    /**
     * Default rule priority.
     */
    int DEFAULT_PRIORITY = 0;

    /**
     * @return the rule's description. Default Value: {@link #id()}
     */
    default String getDescription() {
        return this.id();
    }

    /**
     * @return the rule's priority. Default Value: 0
     */
    default int getPriority() {
        return 0;
    }

    /**
     * Rule conditions abstraction : this method encapsulates the rule's conditions.
     * <strong>Implementations should handle any runtime exception and return true/false accordingly</strong>
     *
     * @return true if the rule should be applied given the provided params, false otherwise. Default impl : return true
     */
    default boolean evaluate(RuleContext context) {
        return true;
    }

    /**
     * Rule actions abstraction : this method encapsulates the rule's actions.
     *
     * @throws Exception thrown if an exception occurs during actions performing
     */
    void execute(RuleContext context) throws Exception;

    /**
     * Rule listener abstraction : this method encapsulates the rule's listeners.
     *
     * @return rule listener list if rule has any listener. otherwise null.
     */
    default Watcher[] getWatchers() {
        return Watcher.EMPTY;
    }

}
