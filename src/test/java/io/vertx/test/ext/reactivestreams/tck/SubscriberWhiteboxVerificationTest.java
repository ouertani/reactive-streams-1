package io.vertx.test.ext.reactivestreams.tck;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.SubscriberWhiteboxVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class SubscriberWhiteboxVerificationTest<T> extends SubscriberWhiteboxVerification<T> {

    protected SubscriberWhiteboxVerificationTest(TestEnvironment testEnvironment) {
        super(testEnvironment);
    }

    @Override
    public Subscriber<T> createSubscriber(WhiteboxSubscriberProbe<T> tWhiteboxSubscriberProbe) {
        return null;
    }

    @Override
    public Publisher<T> createHelperPublisher(long l) {
        return null;
    }
}
