package org.gmjm.jscientist.result;

public class ToggledBasicResult<T> extends BasicResult<T>
{

	public ToggledBasicResult(String experimentName, Status status, String message, T control, Long controlRt, T candidate, Long candidateRt)
	{
		super(experimentName, status, message, control, controlRt, candidate, candidateRt);
	}

	@Override
	public T outcome() {

		String propName = String.format("testlab.%s.use",getExperimentName());

		String use = System.getenv(propName);

		if("candidate".equals(use)) {
			return getCandidate();
		}

		return getControl();
	}
}
