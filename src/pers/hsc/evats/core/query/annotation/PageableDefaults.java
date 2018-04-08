package pers.hsc.evats.core.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;

/**
 * https://github.com/synyx/hades/tree/master/hades/src/main/java/org/synyx/
 * 默认的分页数据，先从参数找，参数找不到从方法上找
 * 
 * @author hsc
 *
 * Mar 29, 2018
 */
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageableDefaults {

	/**
	 * The default-size the injected
	 * {@link org.springframework.data.domain.Pageable} should get if no
	 * corresponding parameter defined in request (default is 10).
	 */
	int value() default 10;

	/**
	 * The default-pagenumber the injected
	 * {@link org.synyx.hades.domain.Pageable} should get if no corresponding
	 * parameter defined in request (default is 0).
	 */
	int pageNumber() default 0;

	/**
	 * 默认的排序 格式为{"a=desc, a.b=desc"}
	 */
	String[] sort() default {};

}
