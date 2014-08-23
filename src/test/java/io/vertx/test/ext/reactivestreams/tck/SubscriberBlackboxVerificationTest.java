package io.vertx.test.ext.reactivestreams.tck;

import io.vertx.core.buffer.Buffer;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.SubscriberBlackboxVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class SubscriberBlackboxVerificationTest extends SubscriberBlackboxVerification<Buffer> {
    protected SubscriberBlackboxVerificationTest(TestEnvironment testEnvironment) {
        super(testEnvironment);
    }

    @Override
    public Subscriber<Buffer> createSubscriber() {
        return null;
    }

    @Override
    public Publisher<Buffer> createHelperPublisher(long l) {
        return null;
    }
}
