package com.ly.traffic.middleplatform.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liugw
 * @Package com.ly.traffic.middleplatform.infrastructure
 * @Description: 聚合根
 * @date 2020/7/6 17:12
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Aggregate {

    /**
     * 是否允许强制重写父类字段
     *
     * @return true ：允许 false：不允许，触发编译报错
     */
    boolean forceRewriteSuperField() default false;
}
