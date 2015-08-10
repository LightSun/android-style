package org.heaven7.scrap.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * indicate the param or field can't be null.
 * Created by heaven7 on 2015/8/9.
 */
@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD})
public @interface NonNull {
}
