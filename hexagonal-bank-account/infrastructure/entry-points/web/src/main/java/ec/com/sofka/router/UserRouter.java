package ec.com.sofka.router;

import ec.com.sofka.data.UserRequestDTO;
import ec.com.sofka.handler.user.CreateUserHandler;
import ec.com.sofka.handler.user.GetAllUsersHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserRouter {
    private final GetAllUsersHandler getAllUsersHandler;
    private final CreateUserHandler createUserHandler;

    public UserRouter(GetAllUsersHandler handler, CreateUserHandler createUserHandler) {
        this.getAllUsersHandler = handler;
        this.createUserHandler = createUserHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> usersRouters() {
        return RouterFunctions
                .route(POST("/api/users").and(accept(APPLICATION_JSON)), this::saveUser)
                .andRoute(GET("/api/users"), this::allUsers);
    }

    public Mono<ServerResponse> saveUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .flatMap(createUserHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

    public Mono<ServerResponse> allUsers(ServerRequest request) {
        return getAllUsersHandler.getAllUsers()
                .collectList()
                .flatMap(list -> {
                        return ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(list);
                    }
                );
    }

}