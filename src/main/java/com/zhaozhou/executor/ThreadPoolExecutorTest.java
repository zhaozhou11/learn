package com.zhaozhou.executor;

import java.util.concurrent.*;

/**
 * Created by zhaozhou on 2017/7/12.
 */
public class ThreadPoolExecutorTest {
    ExecutorService executor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new  ArrayBlockingQueue(1024));
}
