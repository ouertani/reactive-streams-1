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
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ReactiveReadStream implements ReadStream<ReactiveReadStream>, Subscriber<Buffer> {

  private final Vertx vertx;
  private int bufferSize;

  private Handler<Buffer> dataHandler;
  private Handler<Void> endHandler;
  private Handler<Throwable> exceptionHandler;

  private Subscription subscription;

  private Queue<Buffer> pausedData;
  private boolean paused;

  public ReactiveReadStream(Vertx vertx, int bufferSize) {
    this.vertx = vertx;
    this.bufferSize = bufferSize;
  }

  @Override
  public ReactiveReadStream dataHandler(Handler<Buffer> handler) {
    this.dataHandler = handler;
    if (dataHandler != null && !paused) {
      doRead();
    }
    return this;
  }

  @Override
  public ReactiveReadStream pause() {
    this.paused = true;
    return this;
  }

  @Override
  public ReactiveReadStream resume() {
    this.paused = false;
    if (pausedData != null) {
      Buffer data;
      while ((data = pausedData.peek()) != null) {
        handleData(data);
      }
    } else {
      doRead();
    }
    return this;
  }

  @Override
  public ReactiveReadStream endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }

  @Override
  public ReactiveReadStream exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    this.subscription = subscription;
    doRead();
  }

  @Override
  public void onNext(Buffer buffer) {
    handleData(buffer);
  }

  @Override
  public void onError(Throwable throwable) {
    if (exceptionHandler != null) {
      exceptionHandler.handle(throwable);
    }
  }

  @Override
  public void onComplete() {
    if (endHandler != null) {
      endHandler.handle(null);
    }
  }

  private void handleData(Buffer buffer) {
    if (paused) {
      if (pausedData == null) {
        pausedData = new ArrayDeque<>();
      }
      pausedData.add(buffer);
    } else if (dataHandler != null) {
      dataHandler.handle(buffer);
      doRead();
    }
  }

  private void doRead() {
    if (!paused && subscription != null) {
      subscription.request(bufferSize);
    }
  }
}
