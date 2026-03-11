package org.test.between.msproductpricing.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.test.between.msproductpricing.domain.model.PriceModel;
import org.test.between.msproductpricing.domain.ports.out.repository.SearchPriceRepository;
import org.test.between.msproductpricing.infrastructure.repository.mapper.PriceEntityMapper;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;


/**
 * SearchSearchPriceRepositoryAdapter class : Represents the adapter that uses the PriceRepository to search for prices in the database.
 */
@Slf4j
@RequiredArgsConstructor
@Component
@SuppressWarnings("java:S3864")
public class SearchPriceRepositoryAdapter implements SearchPriceRepository {

    private final JpaPriceRepository priceRepository;
    private final PriceEntityMapper priceEntityMapper;

    public Optional<PriceModel> searchByMostPriority(Long productId, Integer brandId, OffsetDateTime effectiveDate) {
        log.info("Searching in <Repository> the price for Product: {} Brand: {} EffectiveDate: {}", productId, brandId, effectiveDate);
        return priceRepository.findByParamsOrderPriorityDesc(productId, brandId, effectiveDate)
                                .stream()
                                .flatMap(Collection::stream)
                                .peek(price -> log.info("Price found in <Repository> - Price List: {}, Priority: {}, Value: {}, Currency: {}",
                                                         price.getPricePK().getPriceList(), price.getPriority(), price.getVPrice(),
                                                         price.getCurrency().getCurrencyIso()))
                                .findFirst()
                                .map(priceEntityMapper::toDomain);
    }
}
