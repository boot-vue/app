package com.bootvue.utils.other;

import com.google.common.util.concurrent.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolUtil<T> {

    private static final ThreadPoolExecutor EXECUTOR;
    private static final ListeningExecutorService EXECUTORSERVICE;
    private static final ThreadFactory THREADFACTORY = new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build();

    //初始化线程池
    static {
        EXECUTOR = new ThreadPoolExecutor(10, 30,
                5, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), THREADFACTORY,
                new ThreadPoolExecutor.DiscardPolicy());
        EXECUTOR.allowCoreThreadTimeOut(true);
        EXECUTORSERVICE = MoreExecutors.listeningDecorator(EXECUTOR);
    }

    /**
     * 提交任务
     *
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> ListenableFuture<T> submit(Callable<T> callable) {
        return EXECUTORSERVICE.submit(callable);
    }

    //demo 例子
    public static void main(String[] args) {
        int i = 0;
        List<ListenableFuture<Long>> futures = new ArrayList<>();
        while (i < 50) {
            ListenableFuture<Long> future = ThreadPoolUtil.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    System.out.println("正在处理..." + Thread.currentThread().getName());
                    Thread.sleep(3000L);
                    return Thread.currentThread().getId();
                }
            });
            futures.add(future);
            i++;
        }

        ListenableFuture<List<Long>> all = Futures.allAsList(futures);
        Futures.addCallback(all, new FutureCallback<List<Long>>() {
            @Override
            public void onSuccess(@Nullable List<Long> result) {
                System.out.println("成功的线程ID " + result + "  " + result.size());
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("失败: " + t.getMessage());
            }
        }, ThreadPoolUtil.EXECUTORSERVICE);


    }
}

