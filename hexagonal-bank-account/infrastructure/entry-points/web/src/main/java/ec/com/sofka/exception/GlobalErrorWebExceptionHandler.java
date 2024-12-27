package ec.com.sofka.exception;

import ec.com.sofka.exception.model.ErrorDetails;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private static final Map<Class<? extends Throwable>, Function<Throwable, ErrorDetails>> exceptionHandlers = Map.of(
            EmptyCollectionException.class, ex -> new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage(), new Date()),
            TransactionRejectedException.class, ex -> new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date()),
            RecordNotFoundException.class, ex -> new ErrorDetails(HttpStatus.NO_CONTENT.value(), ex.getMessage(), new Date()),
            InternalServerException.class, ex -> new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.",new Date())
    );

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        Function<Throwable, ErrorDetails> handler = exceptionHandlers.getOrDefault(ex.getClass(), exceptionHandlers.get(InternalServerException.class));

        ErrorDetails errorResponse = handler.apply(ex);

        exchange.getResponse().setStatusCode(HttpStatus.valueOf(errorResponse.getStatus()));
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);


        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(errorResponse.toString().getBytes())));

    }

}