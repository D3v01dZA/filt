package net.caltona;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.javalin.Javalin;
import lombok.experimental.ExtensionMethod;
import net.caltona.auth.Auth;
import net.caltona.auth.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

@ExtensionMethod({ContextExtensions.class})
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module());
        Auth auth = injector.getInstance(Auth.class);
        Javalin
                .create(
                        config -> config.accessManager(auth)
                )
                .before((ctx) -> {
                    ctx.start(Instant.now());
                    LOGGER.info("Request {} {}", ctx.method(), ctx.fullUrl());
                })
                .after((ctx) -> {
                    Instant start = ctx.start();
                    LOGGER.info("Response {} {} in {}ms with {}", ctx.method(), ctx.fullUrl(), Duration.between(start, Instant.now()).toMillis(), ctx.status());
                })
                .exception(Exception.class, (e, ctx) -> {
                    LOGGER.error("{}", ctx.fullUrl(), e);
                    ctx.status(500).json(Map.of("error", "Error occurred"));
                })
                .error(404, ctx -> ctx.json(Map.of("error", "Not found")))
                .routes(() -> {
                    path("api", () -> {
                        path("auth", () -> {
                            path("login", () -> {
                                post(auth::loginHandler, Role.NONE);
                            });
                            path("logout", () -> {
                                get(auth::logoutHandler, Role.NONE);
                            });
                        });
                    });
                })
                .start(7000);
    }
}
