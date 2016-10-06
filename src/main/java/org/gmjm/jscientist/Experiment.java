package org.gmjm.jscientist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.gmjm.jscientist.hypothesis.Equals;
import org.gmjm.jscientist.result.ResultFactory;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Experiment
{
	Class hypothesis() default Equals.class;
	String underTest();
	String name() default "";
	ResultFactory.Type resultType() default ResultFactory.Type.BASIC;
}
