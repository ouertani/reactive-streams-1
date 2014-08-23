package io.vertx.test.ext.reactivestreams;

import org.reactivestreams.Subscription;

public class MySubscription implements Subscription {

    int requested;
    int requestedTimes;

    @Override
    public void request(int i) {
        requestedTimes++;
        requested += i;
    }

    @Override
    public void cancel() {

    }
}
