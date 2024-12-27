package ec.com.sofka.router;

import ec.com.sofka.data.TransactionRequestDTO;
import ec.com.sofka.handler.transaction.CreateTransactionHandler;
import ec.com.sofka.handler.transaction.GetAllTransactionsHandler;
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
public class TransactionRouter {

    private final GetAllTransactionsHandler getAllTransactionsHandler;
    private final CreateTransactionHandler createTransactionHandler;

    public TransactionRouter(GetAllTransactionsHandler getAllTransactionsHandler, CreateTransactionHandler createTransactionHandler) {
        this.getAllTransactionsHandler = getAllTransactionsHandler;
        this.createTransactionHandler = createTransactionHandler;
    }


    @Bean
    public RouterFunction<ServerResponse> transactionsRouters() {
        return RouterFunctions
                    .route(POST("/api/transactions").and(accept(APPLICATION_JSON)), this::saveTransaction)
                .andRoute(GET("/api/transactions"), this::allTransactions);
    }

    public Mono<ServerResponse> saveTransaction(ServerRequest request) {
        return request.bodyToMono(TransactionRequestDTO.class)
                .flatMap(createTransactionHandler::save)
                .flatMap(response ->
                        ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(response));

    }

    public Mono<ServerResponse> allTransactions(ServerRequest request) {
        return getAllTransactionsHandler.getAllTransactions()
                .collectList()
                .flatMap(list -> {
                            return ServerResponse.ok()
                                    .contentType(APPLICATION_JSON)
                                    .bodyValue(list);
                        }
                );
    }

}