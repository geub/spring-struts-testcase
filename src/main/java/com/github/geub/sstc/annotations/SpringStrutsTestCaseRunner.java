package com.github.geub.sstc.annotations;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class SpringStrutsTestCaseRunner extends BlockJUnit4ClassRunner {

	public SpringStrutsTestCaseRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

}
