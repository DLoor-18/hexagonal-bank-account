package ec.com.sofka.exception;

import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Component;
import jakarta.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
public class RequestValidator {
    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validate(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .toList();
            return Mono.error(new RequestValidationException(errors));
        }

        return Mono.just(request);
    }

}