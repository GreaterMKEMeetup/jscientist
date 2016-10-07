# jscientist
A library that supports running parallel code paths, and inspecting the results.  Based off of the [Scientist: Measure Twice, Cut Over Once](http://githubengineering.com/scientist/) post on the GitHub [Engineering blog](http://githubengineering.com/).

# Concept
**jscientist** is a library that is meant to allow you run experimental code paths at runtime, capture the resulting objects, and compare them.
When woring on large and complex legacy code bases, it can be risky to refactor parts of the code.  The goal of this library is
to pick up where unit, and  integration test fall short.  Many times, the effort to setup unit and integration tests on these complex
code bases is too high to justify.  It is a much better option to cover as much as possible with unit / integration tests, and then use **jscientist**
to cover the remaining risk in QA environments. It is possible, but not recommended to run **jscientist** in product code at the moment.

## API
The main API objects you'll interact with are Experiment, Hypothesis, and Result.

* **Experiment** - An experiment is a pointcut in the code where you'd like to run two different code paths, and compare the results.
* **Hypothesis** - An experiment contains a Hypothesis.  A Hypothesis is the assumption you'd like to validate on the two resulting objects.
The existing code path is the __control__, and the new code path is the __candidate__.
* **Result** - Running a Hypothesis returns a Result.  A Result contains both the __candidate__ and __control__
objects, the execution time, weather the assumption PASSED or FAILED, and any other message the Hypothesis passes on.

# Basic Usage
The example below is contrived, and is only meant to give an idea how to use the library.
```java
//Add the @Experiment annotation to an existing public method
@Experiment(name="TEST-hello",underTest="helloThere")
@RequestMapping("/hello")
public String sayHello(@RequestParam String name) {
	return "Hello " + name.trim();
}

//Create a bean that contains a method with the same method signature as the control.
@Component
public class HelloThere {

	public String sayHello(String name) {
		String trimName = name.trim().toUpperCase();
		return "Hello " + trimName;
	}

}
```
The above Experiment will run an Equals Hypothesis, and return a BasicResult.  These defaults will be described in detail later on,
but just understand this means the __control__ and __candidate__ objects were compared using `control.equals(candidate)`.  The BasicResult object
will log the following depending on the outcome.

Assume the following two requests were made to the @Experimental method.


```
sayHello("DAVE");
log - BasicResult{experimentName='TEST-hello', status=FAILED, message='control.equals(candidate) == false', control=Hello DAVE, controlRt=11, candidate=Hello DAVE, candidateRt=0}

sayHello("Dave");
log - BasicResult{experimentName='TEST-hello', status=PASSED, message='control.equals(candidate) == true', control=Hello Dave, controlRt=0, candidate=Hello DAVE, candidateRt=0}
```

# Using Spring's @Profile

When jscientist is on the classpath, and the jscientist profile is active, all experiments will be run.
If the profile is not enabled, the bean is not created, and the @Experimental annotations have no impact at runtime at all.
This makes them minimally invasive into existing codebases, and is a great way to test out new code paths.

**application.yml**
```yaml
spring:
  profiles:
    active: jscientist
```
# Advanced Experiments
@Experiment can be used to go beyond using the basic Equals Hypothesis.  You
can specify any class that implements the Hypothesis interface.  This allows
your experiment to compare complex, domain specific objects.  More detail to come here.

# Advanced Result Types
Advanced result types can be used.  These types can be used to determine weather
the control, or candidate objects should be returned using environment variables,
or other toggle mechanisms.
