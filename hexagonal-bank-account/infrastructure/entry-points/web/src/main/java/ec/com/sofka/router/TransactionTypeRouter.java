package ec.com.sofka.router;

import ec.com.sofka.data.TransactionTypeRequestDTO;
import ec.com.sofka.handler.transcationType.CreateTransactionTypeHandler;
import ec.com.sofka.handler.transcationType.GetAllTransactionTypesHandler;
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
public class TransactionTypeRouter {
    private final GetAllTransactionTypesHandler getAllTransactionTypesHandler;
    private final CreateTransactionTypeHandler createTransactionTypeHandler;

    public TransactionTypeRouter(GetAllTransactionTypesHandler getAllTransactionTypesHandler, CreateTransactionTypeHandler createTransactionTypeHandler) {
        this.getAllTransactionTypesHandler = getAllTransactionTypesHandler;
        this.createTransactionTypeHandler = createTransactionTypeHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> transactionTypesRouters() {
        return RouterFunctions
                .route(POST("/api/transaction-types").and(accept(APPLICATION_JSON)), this::saveTransactionType)
                .andRoute(GET("/api/transaction-types"), this::allTransactionTypes);
    }

    public Mono<ServerResponse> saveTransactionType(ServerRequest request) {
        return request.bodyToMono(TransactionTypeRequestDTO.class)
                .flatMap(createTransactionTypeHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

    public Mono<ServerResponse> allTransactionTypes(ServerRequest request) {
        return getAllTransactionTypesHandler.getAllTransactionTypes()
                .collectList()
                .flatMap(list -> {
                            return ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .bodyValue(list);
                        }
                );
    }

}