package com.epcs.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * This tool is used for restarting a thread when some exception happened
 */
public class ThreadKeeper implements Runnable {

    HashMap<Runnable, Thread> threadList = new HashMap<Runnable, Thread>();
    private volatile boolean shutdownRequested = false;
    private static final Logger logger = Logger.getLogger(ThreadKeeper.class);

    public void register(Thread thread, Runnable runnable) {
        synchronized (threadList) {
            threadList.put(runnable, thread);
        }
    }

    public void unRegister(Thread thread, Runnable runnable) {
        synchronized (threadList) {
            threadList.remove(runnable);
        }
    }

    public final void shutdownRequest() {
        shutdownRequested = true;
    }

    @Override
    public void run() {

        while (!shutdownRequested) {
            try {
                //check every 10s
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (threadList) {

                Iterator<Entry<Runnable, Thread>> iter = threadList.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Runnable r = (Runnable) entry.getKey();
                    Thread t = (Thread) entry.getValue();
                    if (!t.isAlive()) {
                        System.out.println(t + " finished or died, restart");
                        logger.info("beforeJFinalStop mKeeperThread exit");
                        t = new Thread(r);
                        threadList.put(r, t);
                        t.start();
                    }
                }

            }
        }
    }
}
