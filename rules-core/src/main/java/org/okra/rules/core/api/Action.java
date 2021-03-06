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

/**
 * @author TinyZ.
 * @since 2019.03.06
 */
public interface Action {

    void execute(RuleContext context) throws Exception;


    default Action next(Action after) {
        return (RuleContext context) -> {
            this.execute(context);
            after.execute(context);
        };
    }

    default Action prev(Action before) {
        return (RuleContext context) -> {
            before.execute(context);
            this.execute(context);
        };
    }

}
