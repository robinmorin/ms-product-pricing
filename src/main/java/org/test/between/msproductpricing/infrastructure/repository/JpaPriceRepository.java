package org.test.between.msproductpricing.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.between.msproductpricing.infrastructure.repository.entity.Price;
import org.test.between.msproductpricing.infrastructure.repository.entity.PricePK;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPriceRepository extends JpaRepository<Price, PricePK> {

        @Query("SELECT pr FROM Price pr WHERE pr.pricePK.product.productId = :productId AND pr.pricePK.brand.brandId = :brandId AND :effectiveDate BETWEEN pr.startDate AND pr.endDate ORDER BY pr.priority DESC")
        Optional<List<Price>> findByParamsOrderPriorityDesc(@Param("productId") Long productId, @Param("brandId") Integer brandId, @Param("effectiveDate") OffsetDateTime date);

}
