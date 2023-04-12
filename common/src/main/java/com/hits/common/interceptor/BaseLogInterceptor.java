package com.hits.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
public class BaseLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Map<String, Object> inputMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);

        log.info("Incoming request is " + inputMap);
        log.info("Incoming url is: "+ request.getRequestURL());

        return true;
    }
}
