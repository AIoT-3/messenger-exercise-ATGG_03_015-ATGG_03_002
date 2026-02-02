package com.nhnacademy.messenger.client.ui.cli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 커맨드 매핑을 위한 어노테이션
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMapping {
    String command();
}
