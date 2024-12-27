package ec.com.sofka.router;

import ec.com.sofka.data.AccountRequestDTO;
import ec.com.sofka.handler.account.CreateAccountHandler;
import ec.com.sofka.handler.account.GetAllAccountsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AccountRouter {
    private final GetAllAccountsHandler getAllAccountsHandler;
    private final CreateAccountHandler createAccountHandler;

    public AccountRouter(GetAllAccountsHandler getAllAccountsHandler, CreateAccountHandler createAccountHandler) {
        this.getAllAccountsHandler = getAllAccountsHandler;
        this.createAccountHandler = createAccountHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> accountsRouters() {
        return RouterFunctions
                .route(POST("/api/accounts").and(accept(APPLICATION_JSON)), this::saveAccount)
                .andRoute(GET("/api/accounts"), this::allAccounts);
    }

    public Mono<ServerResponse> saveAccount(ServerRequest request) {
        return request.bodyToMono(AccountRequestDTO.class)
                .flatMap(createAccountHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

    public Mono<ServerResponse> allAccounts(ServerRequest request) {
        return getAllAccountsHandler.getAllAccounts()
                .collectList()
                .flatMap(list -> {
                            return ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .bodyValue(list);
                        }
                );
    }

}