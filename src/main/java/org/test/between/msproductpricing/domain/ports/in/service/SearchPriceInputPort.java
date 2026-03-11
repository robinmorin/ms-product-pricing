package org.test.between.msproductpricing.domain.ports.in.service;

import org.test.between.msproductpricing.domain.model.PriceModel;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Port for call repository and pass parameter to search prices
 */
public interface SearchPriceInputPort {

     Optional<PriceModel> searchByMostPriority(Long productId, Integer brandId, OffsetDateTime effectiveDate);

}
