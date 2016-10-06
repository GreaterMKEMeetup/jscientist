package org.gmjm.jscientist.result;

public interface Result<T>
{
	enum Status {PASSED,FAILED}

	String getExperimentName();
	Status getStatus();
	String getMessage();
	T getControl();
	T getCandidate();

	T outcome();
}
