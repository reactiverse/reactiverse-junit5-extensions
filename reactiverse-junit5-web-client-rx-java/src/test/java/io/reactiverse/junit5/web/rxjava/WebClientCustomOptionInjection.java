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
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.reactiverse.junit5.web.WebClientOptionsInject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.WebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class WebClientCustomOptionInjection {

  @WebClientOptionsInject
  public WebClientOptions options = new WebClientOptions().setDefaultPort(9001).setDefaultHost("localhost");

  @Test
  void test(Vertx vertx, VertxTestContext testContext, WebClient client) {
    vertx.createHttpServer().requestHandler(req -> {
      req.response().end();
    }).listen(9001, testContext.succeeding(httpServer -> {
      client.get("/bla").send(testContext.completing());
    }));
  }

}
