package io.vertx.test.ext.reactivestreams.tck;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.SubscriberBlackboxVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class SubscriberBlackboxVerificationTest<T> extends SubscriberBlackboxVerification<T> {
    protected SubscriberBlackboxVerificationTest(TestEnvironment testEnvironment) {
        super(testEnvironment);
    }

    @Override
    public Subscriber<T> createSubscriber() {
        return null;
    }

    @Override
    public Publisher<T> createHelperPublisher(long l) {
        return null;
    }
}
