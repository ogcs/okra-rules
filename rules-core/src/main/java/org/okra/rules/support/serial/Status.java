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

package org.okra.rules.support.serial;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 状态.
 */
public class Status implements Serializable {

    private static final long serialVersionUID = -3791457133748590285L;

    protected volatile int status;
    private static final AtomicIntegerFieldUpdater<Status> STATUS_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(Status.class, "status");

    /**
     * Get Status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Change Status.
     */
    public boolean changeStatus(int expect, int status) {
        return STATUS_UPDATER.compareAndSet(this, expect, status);
    }

    public boolean changeStatus(Enum<?> expect, Enum<?> status) {
        return changeStatus(expect.ordinal(), status.ordinal());
    }
}
