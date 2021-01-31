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
