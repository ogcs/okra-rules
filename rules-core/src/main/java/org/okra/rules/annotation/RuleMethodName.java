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

package org.okra.rules.annotation;

public enum RuleMethodName {
    /**
     * {@link org.okra.rules.core.api.Rule#id()}
     */
    ID,
    /**
     * {@link org.okra.rules.core.api.Rule#getDescription()}
     */
    GETDESCRIPTION,
    /**
     * {@link org.okra.rules.core.api.Rule#getPriority()}
     */
    GETPRIORITY,
    /**
     * {@link org.okra.rules.core.api.Rule#evaluate(org.okra.rules.core.RuleContext)}
     */
    EVALUATE,
    /**
     * {@link org.okra.rules.core.api.Rule#execute(org.okra.rules.core.RuleContext)}
     */
    EXECUTE,
    /**
     * {@link org.okra.rules.core.api.Rule#getWatchers()}
     */
    GETWATCHERS,

    /// Basic Object Method

    /**
     * Basic object method #equals().
     */
    EQUALS,
    /**
     * Basic object method #hashCode().
     */
    HASHCODE,
    /**
     * Basic object method #toString().
     */
    TOSTRING;
}
