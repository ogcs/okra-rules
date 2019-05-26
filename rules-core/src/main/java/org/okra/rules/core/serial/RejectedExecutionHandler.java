/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package org.okra.rules.core.serial;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A handler for tasks that cannot be executed by a {@link ThreadPoolExecutor}.
 */
public interface RejectedExecutionHandler {

    void rejectedExecution(Runnable r, ExecutorService executor);

    RejectedExecutionHandler AbortPolicy = (r, e) -> {
        throw new RejectedExecutionException("Task " + r.toString() +
                " rejected from " +
                e.toString());
    };
}
