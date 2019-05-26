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
