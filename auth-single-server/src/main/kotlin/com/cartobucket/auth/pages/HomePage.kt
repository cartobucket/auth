package com.cartobucket.auth.pages

import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/")
class HomePage {

    @Inject
    lateinit var home: Template

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun getHomePage(): TemplateInstance {
        return home
            .data("title", "Cartobucket Auth")
            .data("version", "1.0.0")
            .data("year", java.time.Year.now().value)
            .data("features", listOf(
                Feature("OAuth 2.0 & OIDC", "Full support for OAuth 2.0 and OpenID Connect protocols", "🔐"),
                Feature("Multi-Tenant", "Support multiple authorization servers and applications", "🏢"),
                Feature("Flexible Storage", "PostgreSQL backend with JSON metadata support", "💾"),
                Feature("API First", "RESTful APIs with OpenAPI documentation", "🚀"),
                Feature("Security First", "BCrypt password hashing and JWT token management", "🛡️"),
                Feature("Developer Friendly", "Easy integration with comprehensive documentation", "💻")
            ))
            .data("endpoints", listOf(
                Endpoint("/api", "REST API", "Access the authentication and authorization APIs"),
                Endpoint("/q/health", "Health Check", "Monitor application health status"),
                Endpoint("/q/swagger-ui", "API Documentation", "Interactive API documentation"),
                Endpoint("/.well-known/openid-configuration", "OIDC Discovery", "OpenID Connect discovery endpoint")
            ))
    }

    data class Feature(
        val name: String,
        val description: String,
        val icon: String
    )

    data class Endpoint(
        val path: String,
        val name: String,
        val description: String
    )
}