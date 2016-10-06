package org.gmjm.jscientist.hypothesis;

import java.util.function.Supplier;

import org.gmjm.jscientist.result.Result;

public interface Hypothesis<T>
{
	Result<T> test(String experimentName, Supplier<T> controlSupplier, Supplier<T> candidateSupplier);
}
