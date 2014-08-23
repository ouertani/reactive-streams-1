package io.vertx.test.ext.reactivestreams.tck;

import io.vertx.core.buffer.Buffer;
import io.vertx.test.ext.reactivestreams.MyPublisher;
import io.vertx.test.ext.reactivestreams.MySubscription;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class PublisherVerificationTest extends PublisherVerification<Buffer> {

    public static final long DEFAULT_TIMEOUT_MILLIS = 300L;
    public static final long PUBLISHER_REFERENCE_CLEANUP_TIMEOUT_MILLIS = 1000L;

    public PublisherVerificationTest() {
        super(new TestEnvironment(DEFAULT_TIMEOUT_MILLIS), PUBLISHER_REFERENCE_CLEANUP_TIMEOUT_MILLIS);
    }

    public PublisherVerificationTest(TestEnvironment testEnvironment, long l) {
        super(testEnvironment, l);
    }

    @Override
    public Publisher<Buffer> createPublisher(long l) {
        return new MyPublisher();
    }

    @Override
    public Publisher<Buffer> createErrorStatePublisher() {
        return subscriber -> subscriber.onError(new RuntimeException("Can't subscribe subcriber: " + subscriber + ", because of reasons."));

    }
}
