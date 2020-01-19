package com.educlimax.statistics.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FortunatusE
 * @date 12/8/2018
 */
public final class IndexGenerator {

    private static AtomicInteger counter = new AtomicInteger(0);

    public static int nextIndex(){
        return counter.getAndIncrement();
    }

    public static void resetCounter(int value){
        counter = new AtomicInteger(value);

    }

}
