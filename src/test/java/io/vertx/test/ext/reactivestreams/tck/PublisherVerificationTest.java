package io.vertx.test.ext.reactivestreams.tck;

import io.vertx.core.buffer.Buffer;
import io.vertx.test.ext.reactivestreams.MyPublisher;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class PublisherVerificationTest extends PublisherVerification<Buffer> {

    public PublisherVerificationTest(TestEnvironment testEnvironment, long l) {
        super(testEnvironment, l);
    }

    @Override
    public Publisher<Buffer> createPublisher(long l) {
        return new MyPublisher();
    }

    @Override
    public Publisher<Buffer> createErrorStatePublisher() {
        return MyPublisher publisher = new MyPublisher();;
    }
}
