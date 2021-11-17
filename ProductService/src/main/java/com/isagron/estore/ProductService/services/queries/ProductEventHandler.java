package com.isagron.estore.ProductService.services.queries;

import com.isagron.estore.CoreCommon.evnets.ProductReservationCancelledEvent;
import com.isagron.estore.CoreCommon.evnets.ProductReservedEvent;
import com.isagron.estore.ProductService.dao.ProductRepository;
import com.isagron.estore.ProductService.domain.ProductEntity;
import com.isagron.estore.ProductService.services.events.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-group")
@Slf4j
public class ProductEventHandler {

    private final ProductRepository productRepository;

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){
        log.info("ProductReservedEvent handled for orderId in event handler: " + productReservedEvent.getOrderId());

        ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity()-productReservedEvent.getQuantity());
        productRepository.save(productEntity);
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent event){
        log.info("ProductReservationCancelledEvent handled for product in event handler: " + event.getProductId());
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());
        productEntity.setQuantity(productEntity.getQuantity()+event.getQuantity());
        productRepository.save(productEntity);
    }

    @ResetHandler
    public void reset(){
        productRepository.deleteAll();
    }
}
