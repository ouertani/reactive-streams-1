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

  private Set<Subscriber<Buffer>> subscribers = new HashSet<Subscriber<Buffer>>();

  private Queue<Buffer> pending = new LinkedList<Buffer>();

  private AtomicInteger tokens = new AtomicInteger();

  private Handler<Void> drainHandler;

  private int writeQueueMaxSize = 10; /// Whatever


  @Override
  public void subscribe(Subscriber<Buffer> subscriber) {
    subscribers.add(subscriber);
    subscriber.onSubscribe(new SubscriptionImpl());
  }

  @Override
  public ReactiveWriteStream writeBuffer(Buffer data) {
    if (tokens.get() > 0 && !subscribers.isEmpty()) {
      sendToSubscribers(data);
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

  private void checkSend() {
    if (!subscribers.isEmpty()) {
      int availableTokens = tokens.get();
      int toSend = Math.min(availableTokens, pending.size());
      for (int i = 0; i < toSend; i++) {
        sendToSubscribers(pending.poll());
      }
    }
  }

  private void sendToSubscribers(Buffer data) {
    for (Subscriber subscriber: subscribers) {
      subscriber.onNext(data);
    }
  }

  class SubscriptionImpl implements Subscription {

    @Override
    public void request(int i) {
      tokens.addAndGet(i);
    }

    @Override
    public void cancel() {

    }
  }
}
