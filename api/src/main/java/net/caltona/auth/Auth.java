package net.caltona.auth;

import com.google.inject.Inject;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.experimental.ExtensionMethod;
import net.caltona.ContextExtensions;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Set;

@ExtensionMethod({ContextExtensions.class})
public class Auth implements AccessManager {

    private NamedParameterJdbcOperations jdbcTemplate;

    @Inject
    public Auth(NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context ctx, @NotNull Set<RouteRole> routeRoles) throws Exception {
        if (routeRoles.contains(Role.NONE)) {
            handler.handle(ctx);
        } else if (ctx.roles().stream().anyMatch(routeRoles::contains)) {
            handler.handle(ctx);
        } else {
            ctx.unauthorizedResponse();
        }
    }

    public void loginHandler(Context ctx) {
        Credentials credentials = ctx.bodyAsClass(Credentials.class);
        if (true) {
            ctx.userId(1);
            ctx.roles(Set.of(Role.USER));
            ctx.successResponse();
        } else {
            ctx.unauthorizedResponse();
        }
    }

    public void logoutHandler(Context ctx) {
        ctx.clear();
        ctx.successResponse();
    }

}

