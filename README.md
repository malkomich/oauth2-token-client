# OAuth2 Token Client

## Overview

OAuth2 token client, to implement OAuth2 security in our REST clients.


## Requisites
* Java 8+
* Maven 3+


## Install

**Gradle**

```
	dependencies {
	    compile 'com.github.malkomich:oauth2-token-client:0.0.1'
	}
```

**Maven**

```
	<dependency>
	    <groupId>com.github.malkomich</groupId>
	    <artifactId>oauth2-token-client</artifactId>
	    <version>0.0.1</version>
	</dependency>
```


## Usage

##### Initialize client:
```java
OAuth2Client oAuth2Client = OAuth2Client
    .withConfig(oAuth2Config) // Initialize OAuth2Config from your config files
    .build();
```

##### Generate token:
```java
String token = Optional.ofNullable(oAuth2Client.accessToken())
    .map(AccessToken::getAccessToken)
    .map("Bearer "::concat)
    .orElseThrow(() -> new AccessDeniedException());
```

##### Include the Authorization header:
```java
// Spring WebClient
clientRequest.header(HttpHeaders.AUTHORIZATION, token);
// Feign Client
requestTemplate.header(HttpHeaders.AUTHORIZATION, token);
// Vert.x WebClient
webClient.putHeader(HttpHeaders.AUTHORIZATION.toString(), token)
// or any other Framework or client you may use
```


## License
[Apache License](LICENSE)
