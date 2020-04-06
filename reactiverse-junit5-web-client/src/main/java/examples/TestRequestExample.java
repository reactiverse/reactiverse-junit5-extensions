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

package examples;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static io.reactiverse.junit5.web.TestRequest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestRequestExample {

  @Test
  public void testWithWebClient(Vertx vertx, WebClient client, VertxTestContext testContext) {
    client
      .get("/hello")
      .addQueryParam("name", "francesco")
      .putHeader("x-my", "foo")
      .send(testContext.succeeding(response -> {
        testContext.verify(() -> {
          assertEquals(200, response.statusCode());
          assertEquals("OK", response.statusMessage());

          Object body = Json.decodeValue(response.bodyAsBuffer());
          String contentTypeHeader = response.getHeader("content-type");
          String otherHeader = response.getHeader("x-my");
          assertEquals(new JsonObject().put("value", "Hello Francesco!"), body);
          assertEquals("application/json", contentTypeHeader);
          assertEquals("bar", otherHeader);
        });
        testContext.completeNow(); // or checkpoint.flag() if you are using Checkpoint
      }));
  }

  @Test
  public void testWithTestRequest(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(client, HttpMethod.GET, "/hello")
      .with(queryParam("name", "francesco"), requestHeader("x-my", "foo"))
      .expect(
        jsonBodyResponse(new JsonObject().put("value", "Hello Francesco!")),
        responseHeader("x-my", "bar")
      )
      .send(testContext);
  }

  @Test
  public void testWithTestRequestCheckpoint(Vertx vertx, WebClient client, VertxTestContext testContext) {
    Checkpoint checkpoint = testContext.checkpoint(1);

    testRequest(client, HttpMethod.GET, "/hello")
      .with(queryParam("name", "francesco"), requestHeader("x-my", "foo"))
      .expect(
        jsonBodyResponse(new JsonObject().put("value", "Hello Francesco!")),
        responseHeader("x-my", "bar")
      )
      .send(testContext, checkpoint);
  }

  @Test
  public void testWithTestRequestWrapping(Vertx vertx, WebClient client, VertxTestContext testContext) {
    testRequest(
      client
        .get("/hello")
        .addQueryParam("name", "francesco")
        .putHeader("x-my", "foo")
    )
      .expect(
        jsonBodyResponse(new JsonObject().put("value", "Hello Francesco!")),
        responseHeader("x-my", "bar")
      )
      .send(testContext);
  }

  @Test
  public void testWithTestRequestCustomAssert(Vertx vertx, WebClient client, VertxTestContext testContext) {
    // No need to wrap in testContext#verify
    Consumer<HttpResponse<Buffer>> myAssert = req -> assertNotEquals(200, req.statusCode());

    testRequest(client.get("/hello"))
      .expect(
        jsonBodyResponse(new JsonObject().put("value", "Hello Francesco!")),
        myAssert
      )
      .send(testContext);
  }

  @Test
  public void testWithTestRequestChaining(Vertx vertx, WebClient client, VertxTestContext testContext) {
    Checkpoint checkpoint = testContext.checkpoint(3);

    testRequest(client, HttpMethod.GET, "/hello")
      .with(queryParam("name", "francesco"), requestHeader("x-my", "foo"))
      .expect(
        jsonBodyResponse(new JsonObject().put("value", "Hello Francesco!")),
        responseHeader("x-my", "bar")
      )
      .send(testContext, checkpoint) // Flag the checkpoint when all asserts succeed
      // Use Future#compose to chain a new request after the end of the first one
      .compose(response1 ->
        testRequest(client, HttpMethod.GET, "/hello")
          .expect(statusCode(200))
          .send(testContext, checkpoint)
      )
      // And another one
      .compose(response2 ->
        testRequest(client, HttpMethod.GET, "/hello")
          .expect(statusCode(500))
          .send(testContext, checkpoint)
      );
  }

}
