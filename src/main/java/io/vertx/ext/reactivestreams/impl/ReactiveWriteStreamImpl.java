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

package io.vertx.ext.reactivestreams.impl;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;
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
public class ReactiveWriteStreamImpl implements ReactiveWriteStream {

  private Set<SubscriptionImpl> subscriptions = new HashSet<SubscriptionImpl>();
  private Queue<Buffer> pending = new LinkedList<Buffer>();
  private Handler<Void> drainHandler;
  private int writeQueueMaxSize = DEFAULT_WRITE_QUEUE_MAX_SIZE;
  private int maxBufferSize = DEFAULT_MAX_BUFFER_SIZE;
  private int totPending;

  @Override
  public void subscribe(Subscriber<Buffer> subscriber) {
    SubscriptionImpl sub = new SubscriptionImpl(subscriber);
    subscriptions.add(sub);
    subscriber.onSubscribe(sub);
  }

  @Override
  public ReactiveWriteStreamImpl writeBuffer(Buffer data) {
    if (data.length() > maxBufferSize) {
      splitBuffers(data);
    } else {
      pending.add(data);
    }
    totPending += data.length();
    checkSend();
    return this;
  }

  @Override
  public ReactiveWriteStreamImpl setWriteQueueMaxSize(int maxSize) {
    if (writeQueueMaxSize < 1) {
      throw new IllegalArgumentException("writeQueueMaxSize must be >=1");
    }
    this.writeQueueMaxSize = maxSize;
    return this;
  }

  @Override
  public boolean writeQueueFull() {
    return totPending >= writeQueueMaxSize;
  }

  @Override
  public ReactiveWriteStreamImpl drainHandler(Handler<Void> handler) {
    this.drainHandler = handler;
    return this;
  }

  @Override
  public ReactiveWriteStreamImpl exceptionHandler(Handler<Throwable> handler) {
    return this;
  }

  @Override
  public ReactiveWriteStream setBufferMaxSize(int maxBufferSize) {
    if (maxBufferSize < 1) {
      throw new IllegalArgumentException("maxBufferSize must be >=1");
    }
    this.maxBufferSize = maxBufferSize;
    return this;
  }

  private void splitBuffers(Buffer data) {
    int pos = 0;
    while (pos < data.length() - 1) {
      int end = pos + Math.min(maxBufferSize, data.length() - pos);
      Buffer slice = data.slice(pos, end);
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
      if (drainHandler != null && pending.size() < writeQueueMaxSize) {
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
    boolean copy = subscriptions.size() > 1;
    for (SubscriptionImpl sub: subscriptions) {
      if (copy) {
        data = data.copy();
      }
      totPending -= data.length();
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
