package com.cartobucket.auth.services;

import com.cartobucket.auth.models.Profile;
import com.cartobucket.auth.models.Schema;
import com.cartobucket.auth.repositories.SchemaRepository;
import com.cartobucket.auth.services.impls.SchemaServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class SchemaServiceTests {

    @Inject
    SchemaRepository schemaRepository;

    @Test
    void testValidation() throws JsonProcessingException {
        var profile = new Profile();
        Map<String, Object> profileMap = Map.of(
                "name", "Test User"
        );
        profile.setProfile(profileMap);

        var schema = new Schema();
        schema.setSchema(new ObjectMapper().readValue("""
            {
              "$schema": "https://json-schema.org/draft/2020-12/schema",
              "title": "OpenID Connect Standard Claims",
              "type": "object",
              "additionalProperties": true,
              "properties": {
                "name": {
                  "type": "string",
                  "description": "The full name of the end-user"
                },
                "given_name": {
                  "type": "string",
                  "description": "The given name of the end-user"
                },
                "family_name": {
                  "type": "string",
                  "description": "The family name of the end-user"
                },
                "middle_name": {
                  "type": "string",
                  "description": "The middle name of the end-user"
                },
                "nickname": {
                  "type": "string",
                  "description": "The nickname of the end-user"
                },
                "preferred_username": {
                  "type": "string",
                  "description": "The preferred username of the end-user"
                },
                "profile": {
                  "type": "string",
                  "description": "The URL of the end-user's profile page"
                },
                "picture": {
                  "type": "string",
                  "description": "The URL of the end-user's profile picture"
                },
                "website": {
                  "type": "string",
                  "description": "The URL of the end-user's website"
                },
                "email": {
                  "type": "string",
                  "description": "The email address of the end-user"
                },
                "email_verified": {
                  "type": "boolean",
                  "description": "Whether the end-user's email address has been verified"
                },
                "gender": {
                  "type": "string",
                  "description": "The end-user's gender"
                },
                "birthdate": {
                  "type": "string",
                  "format": "date",
                  "description": "The end-user's birthdate"
                },
                "zoneinfo": {
                  "type": "string",
                  "description": "The end-user's time zone"
                },
                "locale": {
                  "type": "string",
                  "description": "The end-user's locale"
                },
                "phone_number": {
                  "type": "string",
                  "description": "The end-user's phone number"
                },
                "phone_number_verified": {
                  "type": "boolean",
                  "description": "Whether the end-user's phone number has been verified"
                },
                "address": {
                  "type": "object",
                  "description": "The end-user's address",
                  "additionalProperties": true,
                  "properties": {
                    "formatted": {
                      "type": "string",
                      "description": "The full address, formatted for display"
                    },
                    "street_address": {
                      "type": "string",
                      "description": "The street address"
                    },
                    "locality": {
                      "type": "string",
                      "description": "The city or locality"
                    },
                    "region": {
                      "type": "string",
                      "description": "The state or region"
                    },
                    "postal_code": {
                      "type": "string",
                      "description": "The postal code"
                    },
                    "country": {
                      "type": "string",
                      "description": "The country"
                    }
                  }
                },
                "updated_at": {
                  "type": "integer",
                  "description": "When the end-user's information was last updated"
                }
              },
              "required": ["name", "given_name", "address.street_address"]
            }
            """, Map.class));

        final var schemaService = new SchemaServiceImpl(schemaRepository);
        var errors = schemaService.validateProfileAgainstSchema(profile, schema);

        System.out.println(errors);
    }

}
