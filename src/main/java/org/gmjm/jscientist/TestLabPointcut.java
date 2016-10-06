package org.gmjm.jscientist;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.gmjm.jscientist.hypothesis.Hypothesis;
import org.gmjm.jscientist.result.Result;
import org.gmjm.jscientist.result.ResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("jscientist")
public class TestLabPointcut
{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestLabPointcut.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ResultFactory resultFactory;

	@Around("@annotation(experiment)")
	public Object logAction(
		ProceedingJoinPoint pjp, Experiment experiment)
		throws Throwable {

		MethodSignature methodSignature = ((MethodSignature) pjp.getStaticPart().getSignature());

		String experimentName = createName(experiment);


		Constructor<Hypothesis> hypothesisConstructor = experiment.hypothesis().getConstructor(ResultFactory.class, ResultFactory.Type.class);

		Hypothesis hypothesis = hypothesisConstructor.newInstance(resultFactory,experiment.resultType());

		Result result = hypothesis.test(
			experimentName,
			() -> {
				try
				{
					return pjp.proceed();
				} catch(Throwable e) {
					return e;
				}
			}, () -> {
				try
				{
					Object objectUnderTest = applicationContext.getBean(experiment.underTest());

					String methodName = pjp.getSignature().getName();
					Class[] paramTypes = methodSignature.getParameterTypes();

					Method methodUnderTest = objectUnderTest.getClass().getMethod(
						methodName,
						paramTypes);

					if (methodUnderTest == null)
					{
						throw new RuntimeException("Could not find methodUnderTest. methodName: " + methodName + " paramTypes: " + Arrays.toString(paramTypes));
					}

					return methodUnderTest.invoke(objectUnderTest, pjp.getArgs());
				} catch (Exception e) {
					LOGGER.error("failed",e);
					return e;
				}

			});


		LOGGER.info(result.getMessage());

		return result.outcome();

	}


	private String createName(Experiment experiment)
	{
		return "".equals(experiment.name()) ? "Experiment: " + UUID.randomUUID() : experiment.name();
	}

}
