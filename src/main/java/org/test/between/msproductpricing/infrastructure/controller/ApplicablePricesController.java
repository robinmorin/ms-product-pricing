package org.test.between.msproductpricing.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.test.between.msproductpricing.api.ApplicablePricesApi;
import org.test.between.msproductpricing.application.exception.RecordNotFoundException;
import org.test.between.msproductpricing.application.mapper.PriceResponseMapper;
import org.test.between.msproductpricing.domain.ports.in.service.SearchPriceInputPort;
import org.test.between.msproductpricing.model.PriceResponse;

import java.time.OffsetDateTime;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class ApplicablePricesController implements ApplicablePricesApi {

    private final SearchPriceInputPort searchPriceInputPort;
    private final PriceResponseMapper priceResponseMapper;

    public ResponseEntity<PriceResponse> getPriceToApply(Long productId, Integer brandId, OffsetDateTime effectiveDate) {

        log.info("Requesting price for Product: {} Brand: {} EffectiveDate: {}", productId, brandId, effectiveDate);
        var response = searchPriceInputPort.searchByMostPriority(productId, brandId, effectiveDate)
                .map(priceResponseMapper::toResponse)
                .orElseThrow(() -> new RecordNotFoundException("Price not found with the given parameters"));
        log.info("Returning price found - Price List: {}, Value: {}, EffectiveDateRange: {}", response.getPriceListId(), response.getPriceToApply(), response.getEffectiveDateRange());
        return ResponseEntity.ok(response);
    }

}
