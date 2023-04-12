package com.hits.common.annotation;

import com.hits.common.filter.JwtFilter;
import com.hits.common.handler.BaseExceptionHandler;
import com.hits.common.handler.BaseExceptionHandlerEnabler;
import com.hits.common.service.JwtProvider;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({BaseExceptionHandlerEnabler.class})
public @interface EnableBaseExceptionHandler {
}
