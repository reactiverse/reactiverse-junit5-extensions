![CI](https://github.com/reactiverse/reactiverse-junit5-extensions/workflows/CI/badge.svg)

# Reactiverse Vert.x JUnit 5 integration extensions

This project contains a set of extensions for writing JUnit 5 tests.

We currently offer:

- `reactiverse-junit5-web-client`: support for injecting the Vert.x `WebClient` and assertions using `TestRequest`
  - `reactiverse-junit5-web-client-rx-java` (RxJava bindings)
  - `reactiverse-junit5-web-client-rx-java2` (RxJava2 bindings)
  - `reactiverse-junit5-web-client-rx-java3` (RxJava3 bindings)

## Web Client assertions

To start using it, add the following dependencies:

```xml
<!-- Core dependency to the Vert.x JUnit5 integration library -->
<dependency>
  <groupId>io.vertx</groupId>
  <artifactId>vertx-junit5</artifactId>
</dependency>
<!-- Vert.x Web Client extension -->
<dependency>
  <groupId>io.reactiverse</groupId>
  <artifactId>reactiverse-junit5-web-client</artifactId>
  <version>0.4.0</version>
</dependency>
```

To send a request to `localhost` and assert the result:

```java
@ExtendWith(VertxExtension.class)
public class LocalhostTest {

  // Define the WebClient options
  @WebClientOptionsInject
  public WebClientOptions options = new WebClientOptions()
    .setDefaultHost("localhost")
    .setDefaultPort(9000);

  @Test
  public void testLocalhost(Vertx vertx, VertxTestContext testContext, WebClient client) {
    testRequest(client, HttpMethod.GET, "/path")
      .expect(statusCode(200), statusMessage("Ciao!"))
      .send(testContext);
  }
}
```

For more details on the available assertions, look at [`TestRequest` javadoc](https://www.javadoc.io/doc/io.reactiverse/reactiverse-junit5-web-client/latest/io/reactiverse/junit5/web/TestRequest.html).

## License

    Copyright (c) 2020 Red Hat, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

