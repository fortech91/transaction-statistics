package com.n26.statistics.util;

import java.time.Instant;

/**
 * @author FortunatusE
 * @date 12/8/2018
 */
public class TimeUtil {


    private static Instant offset;


    public static Instant getTimeOffset(){

        if (offset == null){
            offset = Instant.now();
        }
        return offset;
    }
}
