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
