package com.isagron.estore.ProductService.services.commands.interceptors;

import com.isagron.estore.ProductService.dao.ProductLookupRepository;
import com.isagron.estore.ProductService.services.commands.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>>
    handle(List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            log.info("Intercepted command: " + command.getPayloadType());

            if (Objects.equals(CreateProductCommand.class, command.getPayloadType())){
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                boolean isProductExist = productLookupRepository.existsByProductIdOrTitle(createProductCommand.getProductId(),
                        createProductCommand.getTitle());

                if (isProductExist){
                    throw new IllegalStateException("Product already exist");
                }

            }
            return command;
        };
    }
}
