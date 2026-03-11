package org.test.between.msproductpricing.domain.ports.out.repository;

import org.test.between.msproductpricing.domain.model.PriceModel;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Port for get result price from repository search
 */
public interface SearchPriceRepository {
     Optional<PriceModel> searchByMostPriority(Long productId, Integer brandId, OffsetDateTime effectiveDate);

}
