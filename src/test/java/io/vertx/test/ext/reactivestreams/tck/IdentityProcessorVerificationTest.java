package io.vertx.test.ext.reactivestreams.tck;

import io.vertx.core.buffer.Buffer;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.IdentityProcessorVerification;
import org.reactivestreams.tck.TestEnvironment;

/**
 * @author ouertani@gmail.com
 *         Date: 23/08/2014
 */
public class IdentityProcessorVerificationTest extends IdentityProcessorVerification<Buffer> {

    public IdentityProcessorVerificationTest() {
        super(null, 0);
    }
    public IdentityProcessorVerificationTest(TestEnvironment testEnvironment, long l) {
        super(testEnvironment, l);
    }

    public IdentityProcessorVerificationTest(TestEnvironment testEnvironment, long l, int i) {
        super(testEnvironment, l, i);
    }

    @Override
    public Processor<Buffer, Buffer> createIdentityProcessor(int i) {
        return null;
    }

    @Override
    public Publisher<Buffer> createHelperPublisher(long l) {
        return null;
    }

    @Override
    public Publisher<Buffer> createErrorStatePublisher() {
        return null;
    }
}
