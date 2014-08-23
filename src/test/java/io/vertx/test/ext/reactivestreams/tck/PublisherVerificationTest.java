package io.vertx.test.ext.reactivestreams.tck;

import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class PublisherVerificationTest<T> extends PublisherVerification<T> {
    public PublisherVerificationTest(TestEnvironment testEnvironment, long l) {
        super(testEnvironment, l);
    }

    @Override
    public Publisher<T> createPublisher(long l) {
        return null;
    }

    @Override
    public Publisher<T> createErrorStatePublisher() {
        return null;
    }
}
