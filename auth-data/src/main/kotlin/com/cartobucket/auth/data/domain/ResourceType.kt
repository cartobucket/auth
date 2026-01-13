package com.cartobucket.auth.data.domain

enum class ResourceType {
    APPLICATION,
    AUTHORIZATION_SERVER,
    APPLICATION_SECRET,
    CLIENT,
    CLIENT_CODE,
    SCHEMA,
    SCOPE,
    SIGNING_KEY,
    TEMPLATE,
    USER,
    // New types added at the end to preserve ordinal compatibility
    GROUP,
    GROUP_MEMBER
}