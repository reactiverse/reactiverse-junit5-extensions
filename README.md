![CI](https://github.com/reactiverse/reactiverse-junit5-extensions/workflows/CI/badge.svg)

# Reactiverse Vert.x JUnit 5 integration extensions

This project contains a set of extensions for writing JUnit 5 tests.

We currently offer:

- `reactiverse-junit5-web-client`: support for the Vert.x `WebClient`
  - `reactiverse-junit5-web-client-rx-java` (RxJava bindings)
  - `reactiverse-junit5-web-client-rx-java2` (RxJava2 bindings)

To start using it, add the following dependencies:

```
<!-- Core dependency to the Vert.x JUnit5 integration library -->
<dependency>
  <groupId>io.vertx</groupId>
  <artifactId>vertx-junit5</artifactId>
</dependency>
<!-- Vert.x Web Client extension -->
<dependency>
  <groupId>io.reactiverse</groupId>
  <artifactId>reactiverse-junit5-web-client</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

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

