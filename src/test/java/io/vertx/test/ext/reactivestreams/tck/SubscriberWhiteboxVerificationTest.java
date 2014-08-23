package io.vertx.test.ext.reactivestreams.tck;

import io.vertx.core.buffer.Buffer;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.SubscriberWhiteboxVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class SubscriberWhiteboxVerificationTest extends SubscriberWhiteboxVerification<Buffer> {


    public SubscriberWhiteboxVerificationTest(){
        super(null);
    }
    protected SubscriberWhiteboxVerificationTest(TestEnvironment testEnvironment) {
        super(testEnvironment);
    }

    @Override
    public Subscriber<Buffer> createSubscriber(WhiteboxSubscriberProbe<Buffer> tWhiteboxSubscriberProbe) {
        return null;
    }

    @Override
    public Publisher<Buffer> createHelperPublisher(long l) {
        return null;
    }
}
