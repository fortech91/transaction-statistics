package com.educlimax.statistics.util;

import com.educlimax.statistics.dto.Response;
import org.springframework.http.HttpStatus;

/**
 * @author FortunatusE
 * @date 12/9/2018
 */
public class ResponseUtils {


    public static Response createResponse(HttpStatus status){
        return new Response(status);
    }

    public static Response createResponse(HttpStatus status, Object body){
        return new Response(status, body);
    }
}
