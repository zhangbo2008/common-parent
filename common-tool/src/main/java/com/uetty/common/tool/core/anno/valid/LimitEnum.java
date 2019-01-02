package com.uetty.common.tool.core.anno.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字符串取值限制为枚举类型的name
 * <p>为了方便阅读，这里的中文暂时没有用unicode编码，正式使用的时候再修改掉
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface LimitEnum {
	/**
	 * 取值限制为枚举类型字符串
	 */
	Class<? extends Enum<?>> enums();
	String msg() default "变量取值非法";
}
