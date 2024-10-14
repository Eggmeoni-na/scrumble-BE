package com.eggmeonina.scrumble.common.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //타겟은 파라미터 레벨
@Retention(RetentionPolicy.RUNTIME)
public @interface Member {
}
