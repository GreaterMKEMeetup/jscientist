package org.gmjm.jscientist.result;

public class BasicResult<T> implements Result<T>
{

	private final String experimentName;
	private final Status status;
	private final String message;
	private final T control;
	private final Long controlRt;
	private final T candidate;
	private final Long candidateRt;

	public BasicResult(
		String experimentName,
		Status status,
		String message,
		T control,
		Long controlRt,
		T experimental,
		Long experimentalRt)
	{
		this.experimentName = experimentName;
		this.status = status;
		this.message = message;
		this.control = control;
		this.controlRt = controlRt;
		this.candidate = experimental;
		this.candidateRt = experimentalRt;
	}


	@Override
	public String getExperimentName()
	{
		return experimentName;
	}


	@Override
	public Status getStatus()
	{
		return status;
	}


	@Override
	public String getMessage()
	{
		return this.toString();
	}


	@Override
	public T getControl()
	{
		return control;
	}


	public T getCandidate()
	{
		return candidate;
	}


	@Override
	public T outcome()
	{
		return Status.PASSED.equals(this.status) ? candidate : control;
	}


	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("BasicResult{");
		sb.append("experimentName='").append(experimentName).append('\'');
		sb.append(", status=").append(status);
		sb.append(", message='").append(message).append('\'');
		sb.append(", control=").append(control);
		sb.append(", controlRt=").append(controlRt);
		sb.append(", candidate=").append(candidate);
		sb.append(", candidateRt=").append(candidateRt);
		sb.append('}');
		return sb.toString();
	}
}
