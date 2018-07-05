package cn.tangxinyao.thrift.business.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolUtil {

    public static final int FixedThread = 0;
    public static final int CachedThread = 1;
    public static final int SingleThread = 2;

    private ExecutorService executor;
    private ScheduledExecutorService scheduledExecutor;

    public ThreadPoolUtil() {
        throw new UnsupportedOperationException("this class can not be instantiated");
    }

    public ThreadPoolUtil(final int type, final int poolSize) {
        scheduledExecutor = Executors.newScheduledThreadPool(poolSize);
        switch (type) {
            case FixedThread:
                executor = Executors.newFixedThreadPool(poolSize);
                break;
            case SingleThread:
                executor = Executors.newSingleThreadExecutor();
                break;
            case CachedThread:
                executor = Executors.newCachedThreadPool();
                break;
        }
    }

    public void execute(final Runnable command) {
        executor.execute(command);
    }

    public void execute(final List<Runnable> commands) {
        for (Runnable command : commands) {
            executor.execute(command);
        }
    }

    public void shutDown() {
        executor.shutdown();
    }

    public List<Runnable> shutDownNow() {
        return executor.shutdownNow();
    }

    public boolean isShutDown() {
        return executor.isShutdown();
    }

    public boolean isTerminated() {
        return executor.isTerminated();
    }

    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    public <T> Future<T> submit(final Callable<T> task) {
        return executor.submit(task);
    }

    public <T> Future<T> submit(final Runnable task, final T result) {
        return executor.submit(task, result);
    }

    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executor.invokeAll(tasks);
    }

    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        return executor.invokeAll(tasks, timeout, unit);
    }

    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws ExecutionException, InterruptedException {
        return executor.invokeAny(tasks);
    }

    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executor.invokeAny(tasks, timeout, unit);
    }

    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return scheduledExecutor.schedule(command, delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return scheduledExecutor.schedule(callable, delay, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return scheduledExecutor.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixeddDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return scheduledExecutor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

}
