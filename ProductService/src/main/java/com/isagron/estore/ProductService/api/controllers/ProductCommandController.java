package com.isagron.estore.ProductService.api.controllers;

import com.isagron.estore.ProductService.api.dto.CreateProductReq;
import com.isagron.estore.ProductService.services.commands.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createProduct(@RequestBody CreateProductReq createProductReq){

        String returnValue = commandGateway.sendAndWait(CreateProductCommand.createFromReq(createProductReq));
        return returnValue;
    }
}
