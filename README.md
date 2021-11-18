# CQRS-SAGA-with-Axon-framework
Managed microservices using CQRS &amp; SAGA pattern with AXON framework


## Requirements
* Axon server - download from Axon framework website https://axoniq.io/download, you can run it as a jar or in docker.

## Configuration
The Product MS and the Order MS configured to work with Eureka service discovery. 
### Eureka server
https://github.com/isagron/eureka-server

### Disable eureka client
* Remove @EnableDiscoveryClient annotation from main class
* Remove dependency
* Remove related properties from application.yaml

The projects are set with H2 database

## How to run it
1. Download all the projects, CoreCommon, OrderService, ProductService.
2. In CoreCommon project run the following gradle tasks
   * Clean
   * Build
   * PublishToMavenLocal

3. Run the Axon server
4. Run the microservices

## Features
1. Command flow - see https://github.com/isagron/CQRS-SAGA-with-Axon-framework/blob/main/OrderService/src/main/java/com/isagron/estore/OrderService/service/commands/OrderAggregate.java
2. Query flow - see https://github.com/isagron/CQRS-SAGA-with-Axon-framework/blob/main/ProductService/src/main/java/com/isagron/estore/ProductService/services/queries/ProductQueryHandler.java
3. Saga - https://github.com/isagron/CQRS-SAGA-with-Axon-framework/tree/main/OrderService/src/main/java/com/isagron/estore/OrderService/service/saga
4. Snapshots
