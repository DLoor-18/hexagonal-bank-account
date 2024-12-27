package ec.com.sofka.handler.transcationType;

import ec.com.sofka.cases.transcationType.CreateTransactionTypeUseCase;
import ec.com.sofka.data.TransactionTypeRequestDTO;
import ec.com.sofka.data.TransactionTypeResponseDTO;
import ec.com.sofka.mapper.TransactionTypeModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CreateTransactionTypeHandler {
    private final CreateTransactionTypeUseCase createTransactionTypeUseCase;

    public CreateTransactionTypeHandler(CreateTransactionTypeUseCase createTransactionTypeUseCase) {
        this.createTransactionTypeUseCase = createTransactionTypeUseCase;
    }

    public Mono<TransactionTypeResponseDTO> save(TransactionTypeRequestDTO transactionTypeRequestDTO) {
        return createTransactionTypeUseCase.apply(TransactionTypeModelMapper.mapToModel(transactionTypeRequestDTO))
                .map(TransactionTypeModelMapper::mapToDTO);
    }

}