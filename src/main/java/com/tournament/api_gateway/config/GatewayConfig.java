package com.tournament.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Value("${services.player-service.url}")
    private String playerServiceUrl;

    @Value("${services.tournament-service.url}")
    private String tournamentServiceUrl;

    @Value("${services.ranking-service.url}")
    private String rankingServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> playerRoutes() {
        return GatewayRouterFunctions.route("player-service")
                .route(path("/api/players/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.routeId("player-service"))
                .before(request -> {
                    request.attributes().put(MvcUtils.GATEWAY_REQUEST_URL_ATTR,
                            URI.create(playerServiceUrl + request.uri().getRawPath()));
                    return request;
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> tournamentRoutes() {
        return GatewayRouterFunctions.route("tournament-service")
                .route(path("/api/tournaments/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.routeId("tournament-service"))
                .before(request -> {
                    request.attributes().put(MvcUtils.GATEWAY_REQUEST_URL_ATTR,
                            URI.create(tournamentServiceUrl + request.uri().getRawPath()));
                    return request;
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> rankingRoutes() {
        return GatewayRouterFunctions.route("ranking-service")
                .route(path("/api/rankings/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.routeId("ranking-service"))
                .before(request -> {
                    request.attributes().put(MvcUtils.GATEWAY_REQUEST_URL_ATTR,
                            URI.create(rankingServiceUrl + request.uri().getRawPath()));
                    return request;
                })
                .build();
    }
}