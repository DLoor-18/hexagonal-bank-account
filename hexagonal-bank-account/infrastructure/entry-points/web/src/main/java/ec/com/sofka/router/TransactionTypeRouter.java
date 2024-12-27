package ec.com.sofka.router;

import ec.com.sofka.data.TransactionTypeRequestDTO;
import ec.com.sofka.data.TransactionTypeResponseDTO;
import ec.com.sofka.exception.RequestValidator;
import ec.com.sofka.exception.model.ErrorDetails;
import ec.com.sofka.handler.transcationType.CreateTransactionTypeHandler;
import ec.com.sofka.handler.transcationType.GetAllTransactionTypesHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
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
    private final RequestValidator requestValidator;
    private final GetAllTransactionTypesHandler getAllTransactionTypesHandler;
    private final CreateTransactionTypeHandler createTransactionTypeHandler;

    public TransactionTypeRouter(RequestValidator requestValidator, GetAllTransactionTypesHandler getAllTransactionTypesHandler, CreateTransactionTypeHandler createTransactionTypeHandler) {
        this.requestValidator = requestValidator;
        this.getAllTransactionTypesHandler = getAllTransactionTypesHandler;
        this.createTransactionTypeHandler = createTransactionTypeHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/transaction-types",
                    operation = @Operation(
                            tags = {"TransactionTypes"},
                            operationId = "createTransactionType",
                            summary = "Create a new transaction type",
                            description = "Create a transaction type user from the request data.",
                            method = "POST",
                            requestBody = @RequestBody(
                                    description = "Details of the required entity.",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = TransactionTypeRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Record created successfully.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionTypeResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "422",
                                            description = "The entity has a conflict.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal application problems.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/transaction-types",
                    operation = @Operation(
                            tags = {"TransactionTypes"},
                            operationId = "getAllTransactionTypes",
                            summary = "Get all transaction types",
                            description = "Get all registered transaction types.",
                            method = "GET",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successfully obtained all registered transaction types.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionTypeResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal application problems.",
                                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> transactionTypesRouters() {
        return RouterFunctions
                .route(POST("/api/transaction-types").and(accept(APPLICATION_JSON)), this::saveTransactionType)
                .andRoute(GET("/api/transaction-types"), this::allTransactionTypes);
    }

    public Mono<ServerResponse> saveTransactionType(ServerRequest request) {
        return request.bodyToMono(TransactionTypeRequestDTO.class)
                .flatMap(requestValidator::validate)
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