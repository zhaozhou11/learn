package com.zhaozhou.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by zhaozhou on 2017/7/10.
 */
public class Mutex implements Lock{
    private static class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        @Override
        protected boolean tryAcquire(int arg) {
            if(compareAndSetState(0, 1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if(getState() == 0) throw new  IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        Condition newCondition(){return new ConditionObject();}
    }

    private final Sync sync = new Sync();

    public void lock() {
        sync.acquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }


    int count = 0;

    void add(){
        count ++;
        System.out.println("thread: " + Thread.currentThread().getId() + ", count:" + count);
    }


    public static void main(String[] args){

        final Mutex mutex = new Mutex();
        int cnt = 0;
        for(int i = 0; i < 100; i ++){
            new Thread(new Runnable() {
                public void run() {
                    mutex.lock();
                    mutex.add();
                    mutex.unlock();
                }
            }).start();
        }
    }
}
