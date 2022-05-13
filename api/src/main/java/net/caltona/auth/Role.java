package net.caltona.auth;

import io.javalin.core.security.RouteRole;

public enum Role implements RouteRole {
    NONE,
    USER
}
