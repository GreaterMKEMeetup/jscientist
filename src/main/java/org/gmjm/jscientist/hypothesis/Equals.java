package org.gmjm.jscientist.hypothesis;

import java.util.function.Supplier;

import org.gmjm.jscientist.result.Result;
import org.gmjm.jscientist.result.ResultFactory;

import static org.gmjm.jscientist.result.Result.Status.*;

public class Equals implements Hypothesis<Object>
{

	public ResultFactory resultFactory;
	public ResultFactory.Type resultType = null;

	public Equals(ResultFactory resultFactory)
	{
		this.resultFactory = resultFactory;
	}

	public Equals(ResultFactory resultFactory, ResultFactory.Type resultType)
	{
		this.resultFactory = resultFactory;
		this.resultType = resultType;
	}


	@Override
	public Result test(String experimentName, Supplier<Object> controlSupplier, Supplier<Object> candidateSupplier)
	{
		Long start = System.currentTimeMillis();
		Object control = controlSupplier.get();
		Long controlRt = System.currentTimeMillis() - start;

		start = System.currentTimeMillis();
		Object candidate = candidateSupplier.get();
		Long candidateRt = System.currentTimeMillis() - start;

		if(control == null && candidate == null)
		{
			return resultFactory.create(
				resultType,
				experimentName,
				PASSED,
				"Control and candidate objects were both null.",
				control,
				controlRt,
				candidate,
				candidateRt);
		} else if(control == null && candidate != null)
		{
			return resultFactory.create(
				resultType,
				experimentName,
				FAILED,
				"Control was null, but candidate was not null.",
				control,
				controlRt,
				candidate,
				candidateRt);
		} else {
			boolean equal = control.equals(candidate);
			return resultFactory.create(
				resultType,
				experimentName,
				equal ? PASSED : FAILED, equal ? "control.equals(candidate) == true" : "control.equals(candidate) == false",
				control,
				controlRt,
				candidate,
				candidateRt);
		}
	}
}
