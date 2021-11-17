package com.isagron.estore.ProductService.services.queries;

import com.isagron.estore.ProductService.api.dto.ProductDto;
import com.isagron.estore.ProductService.dao.ProductRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductQueryHandler {

    @Autowired
    private ProductRepository productRepository;

    @QueryHandler
    public List<ProductDto> findProducts(FindProductQuery query){
        return productRepository.findAll()
                .stream().map(product -> {
                    ProductDto dto = new ProductDto();
                    BeanUtils.copyProperties(product, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
