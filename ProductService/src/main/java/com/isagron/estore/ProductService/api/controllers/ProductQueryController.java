package com.isagron.estore.ProductService.api.controllers;

import com.isagron.estore.ProductService.api.dto.ProductDto;
import com.isagron.estore.ProductService.services.queries.FindProductQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/queries/products")
public class ProductQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    public List<ProductDto> getProducts(){
        FindProductQuery findProductQuery = new FindProductQuery();
        List<ProductDto> products = queryGateway.query(findProductQuery, ResponseTypes.multipleInstancesOf(ProductDto.class))
                .join();

        return products;
    }
}
