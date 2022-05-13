package net.caltona;

import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ContextExtensions {

    public static Set<RouteRole> roles(Context ctx) {
        Set<RouteRole> roles = ctx.sessionAttribute("roles");
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles;
    }

    public static void roles(Context ctx, Set<RouteRole> roles) {
        ctx.sessionAttribute("roles", roles);
    }

    public static Integer userId(Context ctx) {
        return ctx.sessionAttribute("userId");
    }

    public static void userId(Context ctx, Integer userId) {
        ctx.sessionAttribute("userId", userId);
    }

    public static String username(Context ctx) {
        return ctx.sessionAttribute("username");
    }

    public static void username(Context ctx, String username) {
        ctx.sessionAttribute("username", username);
    }

    public static void start(Context ctx, Instant start) {
        ctx.attribute("start", start);
    }

    public static Instant start(Context ctx) {
        return ctx.attribute("start");
    }

    public static void successResponse(Context ctx) {
        ctx.json(Map.of("message", "Success"));
    }

    public static void unauthorizedResponse(Context ctx) {
        ctx.status(401).json(Map.of("message", "Unauthorized"));
    }

    public static void clear(Context ctx) {
        ctx.consumeSessionAttribute("userId");
        ctx.consumeSessionAttribute("roles");
        ctx.consumeSessionAttribute("username");
    }
}
