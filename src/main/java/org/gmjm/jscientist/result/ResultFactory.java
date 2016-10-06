package org.gmjm.jscientist.result;

import org.springframework.stereotype.Component;

@Component
public class ResultFactory
{

	public enum Type
	{
		BASIC, TOGGLED
	}


	public <T> Result<T> create(String experimentName, Result.Status status, String message, T control, Long controlRt, T candidate, Long candidateRt)
	{
		return create(Type.BASIC, experimentName, status, message, control, controlRt, candidate, candidateRt);
	}


	public <T> Result<T> create(Type type, String experimentName, Result.Status status, String message, T control, Long controlRt, T candidate, Long candidateRt)
	{
		switch (type)
		{
			case BASIC:
				return new BasicResult<>(
					experimentName,
					status,
					message,
					control,
					controlRt,
					candidate,
					candidateRt);

			case TOGGLED:
				return new ToggledBasicResult<>(
					experimentName,
					status,
					message,
					control,
					controlRt,
					candidate,
					candidateRt);

			default:
				return new BasicResult<>(
					experimentName,
					status,
					message,
					control,
					controlRt,
					candidate,
					candidateRt);
		}
	}
}
