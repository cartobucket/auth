# Cartobucket Auth

## Domain Model

The Domain Model is an attempt to build a ubiquitous language for discussing the major concepts of the Application.

### Top Level

#### AuthorizationServer

The AuthorizationServer is the containing concept for the rest of the domain. All other models live within
a particular AuthorizationServer.

### Actors

#### Application

An Application is an actor that can initiate a Client Credentials Flow. The Application is meant to be a
non-person entity within the system. Applications are meant for system to system communication via a web API.

#### Client

A Client is an actor tha can initiate an Authorization Code Flow. The Client represents a system that requires
Users to authenticate. For instance a mobile application, an SPA, or a client side rendered web application.

#### User

Is the entity that an Authorization Code can be generated for. A user has a username and a password that can
be exchanged for an Authorization Code via a Client.

### Support

#### Profile

The Profile is used to hold metadata about the actor. This data can be mapped to the claims of the JWT.
The Profile is associated with an Actor via the Actors id. The Profile also has a type of either User or Application.

#### ApplicationSecret

The Application Secret is the client_secret in OAuth terms. An Application can have multiple secrets and each
secret can be associated with a different set of Scopes.

#### SigningKey

The SingingKey is a private/public keypair associated with the AuthorizationServer. The public keys will be included in
the JWKS. Multiple keys can exist for each AuthorizationServer.

#### Scope

A standard OAuth Scope. They can be associated with a Client or an ApplicationSecret and will be included in the JWT if
requested in the OAuth Code flow.

## Technical

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
