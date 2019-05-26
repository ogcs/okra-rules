package org.okra.rules.support.serial;

import org.okra.rules.core.RuleContext;
import org.okra.rules.core.api.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TinyZ.
 * @version 2019.03.17
 */
public class SerialRuleRunWorker implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(SerialRuleRunWorker.class);

    private final ConcurrentLinkedQueue<FireEvent> queue = new ConcurrentLinkedQueue<>();
    private AtomicInteger queueSize = new AtomicInteger(0);

    private final ExecutorService service;
    private final int threshold;
    private final RejectedExecutionHandler handler;
    private final Status status;

    public SerialRuleRunWorker() {
        this(ForkJoinPool.commonPool(), Integer.MAX_VALUE, null);
    }

    public SerialRuleRunWorker(ExecutorService service, int threshold, RejectedExecutionHandler handler) {
        this.service = service;
        this.threshold = threshold;
        this.handler = handler;
        this.status = new Status();
    }

    public void offer(SerialRulesEngine engine, Rule realRule, RuleContext ctx) {
        if (handler != null
                && queueSize.get() >= this.threshold) {
            handler.rejectedExecution(this, this.service);
            return;
        }
        queue.offer(new FireEvent(engine, realRule, ctx));
        queueSize.incrementAndGet();

        run();
    }

    @Override
    public void run() {
        if (queue.isEmpty()) {
            return;
        }
        if (tryLock()) {
            service.submit(() -> {
                final FireEvent event = queue.poll();
                try {
                    if (event != null) {
                        queueSize.decrementAndGet();
                        event.getEngine().onFireImpl(event.getRule(), event.getCtx());
                    }
                } catch (Exception e) {
                    LOG.error("event:{} execute throw exception.", event, e);
                } finally {
                    unlock();
                    //  submit next rule action to ExecuteService.
                    run();
                }
            });
        }
    }

    enum LockStatus {
        UNLOCKED,
        LOCKED
    }

    private boolean tryLock() {
        return status.changeStatus(LockStatus.UNLOCKED, LockStatus.LOCKED);
    }

    private boolean unlock() {
        return status.changeStatus(LockStatus.LOCKED, LockStatus.UNLOCKED);
    }
}
