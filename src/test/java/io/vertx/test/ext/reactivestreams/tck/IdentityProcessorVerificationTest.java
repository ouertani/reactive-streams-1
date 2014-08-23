package io.vertx.test.ext.reactivestreams.tck;

import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.IdentityProcessorVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class IdentityProcessorVerificationTest<T> extends IdentityProcessorVerification<T> {
    public IdentityProcessorVerificationTest(TestEnvironment testEnvironment, long l) {
        super(testEnvironment, l);
    }

    public IdentityProcessorVerificationTest(TestEnvironment testEnvironment, long l, int i) {
        super(testEnvironment, l, i);
    }

    @Override
    public Processor<T, T> createIdentityProcessor(int i) {
        return null;
    }

    @Override
    public Publisher<T> createHelperPublisher(long l) {
        return null;
    }

    @Override
    public Publisher<T> createErrorStatePublisher() {
        return null;
    }
}
