package com.gq.crm.annotations;

import java.lang.annotation.*;

/**
 * @date 2023/6/19
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    // 权限码
    String code() default "";
}
