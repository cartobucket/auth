package com.cartobucket.auth.authorization.server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
    info = @Info(
        title = "Cartobucket Auth Authorization Server API",
        version = "1.0.0",
        description = "OAuth2/OpenID Connect Authorization Server API",
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
    ),
    servers = {
        @Server(url = "/{authorizationServerId}", description = "Authorization Server endpoint")
    }
)
public class OpenApiConfig extends Application {
}