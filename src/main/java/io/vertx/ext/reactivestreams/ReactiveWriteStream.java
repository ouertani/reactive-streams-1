/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.ext.reactivestreams;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.WriteStream;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ReactiveWriteStream implements WriteStream<ReactiveWriteStream>, Publisher<Buffer> {

  private Set<SubscriptionImpl> subscriptions = new HashSet<SubscriptionImpl>();

  private Queue<Buffer> pending = new LinkedList<Buffer>();

  //private AtomicInteger tokens = new AtomicInteger();

  private Handler<Void> drainHandler;

  private int writeQueueMaxSize = 10; /// Whatever

  private int maxBufferSize = 8 * 1024;


  @Override
  public void subscribe(Subscriber<Buffer> subscriber) {
    SubscriptionImpl sub = new SubscriptionImpl(subscriber);
    subscriptions.add(sub);
    subscriber.onSubscribe(sub);
  }

  @Override
  public ReactiveWriteStream writeBuffer(Buffer data) {
    if (data.length() > maxBufferSize) {
      splitBuffers(data);
    } else {
      pending.add(data);
    }
    return this;
  }

  @Override
  public ReactiveWriteStream setWriteQueueMaxSize(int maxSize) {
    this.writeQueueMaxSize = maxSize;
    return this;
  }

  @Override
  public boolean writeQueueFull() {
    return pending.size() >= writeQueueMaxSize;
  }

  @Override
  public ReactiveWriteStream drainHandler(Handler<Void> handler) {
    this.drainHandler = handler;
    return this;
  }

  @Override
  public ReactiveWriteStream exceptionHandler(Handler<Throwable> handler) {
    return this;
  }

  private void splitBuffers(Buffer data) {
    int pos = 0;
    while (pos < data.length() - 1) {
      Buffer slice = data.slice(pos, Math.max(pos + maxBufferSize, data.length() - 1));
      pending.add(slice);
      pos += maxBufferSize;
    }
  }

  private void checkSend() {
    if (!subscriptions.isEmpty()) {
      int availableTokens = getAvailable();
      int toSend = Math.min(availableTokens, pending.size());
      takeTokens(toSend);
      for (int i = 0; i < toSend; i++) {
        sendToSubscribers(pending.poll());
      }
      if (pending.size() < writeQueueMaxSize) {
        drainHandler.handle(null);
      }
    }
  }

  private int getAvailable() {
    int min = Integer.MAX_VALUE;
    for (SubscriptionImpl subscription: subscriptions) {
      min = Math.min(subscription.tokens.get(), min);
    }
    return min;
  }

  private void takeTokens(int toSend) {
    for (SubscriptionImpl subscription: subscriptions) {
      subscription.tokens.addAndGet(-toSend);
    }
  }

  private void sendToSubscribers(Buffer data) {
    for (SubscriptionImpl sub: subscriptions) {
      sub.subscriber.onNext(data);
    }
  }

  class SubscriptionImpl implements Subscription {

    Subscriber<Buffer> subscriber;

    AtomicInteger tokens = new AtomicInteger();

    SubscriptionImpl(Subscriber<Buffer> subscriber) {
      this.subscriber = subscriber;
    }

    @Override
    public void request(int i) {
      tokens.addAndGet(i);
      checkSend();
    }

    @Override
    public void cancel() {

    }
  }
}
