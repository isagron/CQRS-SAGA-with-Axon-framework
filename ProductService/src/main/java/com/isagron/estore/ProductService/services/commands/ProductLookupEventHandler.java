package com.isagron.estore.ProductService.services.commands;

import com.isagron.estore.ProductService.dao.ProductLookupRepository;
import com.isagron.estore.ProductService.domain.ProductLookupEntity;
import com.isagron.estore.ProductService.services.events.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.stereotype.Component;

@Component
//if no name specify, package name it taken. All events in the same group handle by the same thread
@ProcessingGroup("product-group")
@RequiredArgsConstructor
@Slf4j
public class ProductLookupEventHandler {

    private final ProductLookupRepository productLookupRepository;

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception){
      log.error("Failed due to: ", exception );
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception){
        log.error("Failed due to: ", exception );
    }


    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        productLookupRepository.save(productLookupEntity);
    }

    @ResetHandler
    public void reset(){
        productLookupRepository.deleteAll();
    }
}
