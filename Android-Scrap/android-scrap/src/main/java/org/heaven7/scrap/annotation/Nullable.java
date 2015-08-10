package org.heaven7.scrap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate the param or field can be null.
 * Created by heaven7 on 2015/8/9.
 */
@Retention(RetentionPolicy.CLASS)
@Target( { ElementType.FIELD, ElementType.PARAMETER})
public @interface Nullable {
}
