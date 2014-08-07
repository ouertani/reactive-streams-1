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
import io.vertx.core.streams.ReadStream;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class ReactiveReadStream<T> implements ReadStream<ReactiveReadStream>, Subscriber<T> {

  @Override
  public ReactiveReadStream dataHandler(Handler<Buffer> handler) {
    return null;
  }

  @Override
  public ReactiveReadStream pause() {
    return null;
  }

  @Override
  public ReactiveReadStream resume() {
    return null;
  }

  @Override
  public ReactiveReadStream endHandler(Handler<Void> endHandler) {
    return null;
  }

  @Override
  public ReactiveReadStream exceptionHandler(Handler<Throwable> handler) {
    return null;
  }

  @Override
  public void onSubscribe(Subscription subscription) {

  }

  @Override
  public void onNext(T t) {

  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onComplete() {

  }
}
