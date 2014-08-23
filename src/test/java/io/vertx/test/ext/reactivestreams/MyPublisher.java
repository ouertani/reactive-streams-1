package io.vertx.test.ext.reactivestreams;


import io.vertx.core.buffer.Buffer;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class MyPublisher implements Publisher<Buffer> {

    MySubscription subscription;
    Subscriber<Buffer> subscriber;

    @Override
    public void subscribe(Subscriber<Buffer> subscriber) {
        this.subscriber = subscriber;
        subscription = new MySubscription();
        subscriber.onSubscribe(subscription);
    }
}
