package org.test.between.msproductpricing.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.test.between.msproductpricing.domain.model.PriceModel;
import org.test.between.msproductpricing.domain.ports.in.service.SearchPriceInputPort;
import org.test.between.msproductpricing.domain.ports.out.repository.SearchPriceRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * This class is the use case that interact with the ports to search the price by most priority
 */
@Service
@RequiredArgsConstructor
public class SearchPriceUseCase implements SearchPriceInputPort {

    private final SearchPriceRepository searchPriceRepository;

    public Optional<PriceModel> searchByMostPriority(Long productId, Integer brandId, OffsetDateTime effectiveDate) {
        return searchPriceRepository.searchByMostPriority(productId, brandId, effectiveDate);
    }

}
