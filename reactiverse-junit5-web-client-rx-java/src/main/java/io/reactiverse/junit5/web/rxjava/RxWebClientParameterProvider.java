/*
 * Copyright (c) 2020 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reactiverse.junit5.web.rxjava;

import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.ParameterClosingConsumer;
import io.vertx.junit5.ScopedObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxExtensionParameterProvider;
import io.reactiverse.junit5.web.WebClientParameterProvider;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.WebClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.util.Objects;

/**
 * WebClient parameter provider.
 *
 * @author <a href="https://julien.ponge.org/">Julien Ponge</a>
 * @author <a href="https://slinkydeveloper.com/">Francesco Guardiani</a>
 */
public class RxWebClientParameterProvider implements VertxExtensionParameterProvider<WebClient> {

  @Override
  public Class<WebClient> type() {
    return WebClient.class;
  }

  @Override
  public String key() {
    return "WebClient";
  }

  @Override
  public WebClient newInstance(ExtensionContext extensionContext, ParameterContext parameterContext) {
    ExtensionContext.Store store = extensionContext.getStore(ExtensionContext.Namespace.GLOBAL);
    ScopedObject scopedObject = store.get(VertxExtension.VERTX_INSTANCE_KEY, ScopedObject.class);
    Objects.requireNonNull(scopedObject, "A Vertx instance must exist, try adding the Vertx parameter as the first method argument");
    Vertx vertx = (Vertx) scopedObject.get();
    WebClientOptions webClientOptions = WebClientParameterProvider.getWebClientOptions(extensionContext).orElse(new WebClientOptions());
    return WebClient.create(vertx, webClientOptions);
  }

  @Override
  public ParameterClosingConsumer<WebClient> parameterClosingConsumer() {
    return WebClient::close;
  }
}
