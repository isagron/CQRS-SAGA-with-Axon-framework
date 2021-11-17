package com.isagron.estore.ProductService.dao;

import com.isagron.estore.ProductService.domain.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {

    ProductLookupEntity findByProductIdOrTitle(String productId, String title);

    boolean existsByProductIdOrTitle(String productId, String title);
}
