package org.test.between.msproductpricing.application.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.test.between.msproductpricing.domain.model.PriceModel;
import org.test.between.msproductpricing.model.PriceResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Price Response Mapper Unit Tests")
class PriceResponseMapperTest {

    private final PriceResponseMapper mapper = Mappers.getMapper(PriceResponseMapper.class);

    @Test
    @DisplayName("Should map PriceModel to PriceResponse correctly")
    void shouldMapPriceModelToPriceResponse() {
        // Arrange
        Long productId = 35455L;
        Integer brandId = 1;
        Integer priceList = 1;
        BigDecimal priceValue = new BigDecimal("35.50");
        OffsetDateTime startDate = OffsetDateTime.parse("2020-06-14T00:00:00Z");
        OffsetDateTime endDate = OffsetDateTime.parse("2020-12-31T23:59:59Z");

        PriceModel priceModel = PriceModel.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(priceList)
                .price(priceValue)
                .currencyIso("EUR")
                .effectiveDates(new PriceModel.RangeDate(startDate, endDate))
                .build();

        // Act
        PriceResponse response = mapper.toResponse(priceModel);

        // Assert
        assertNotNull(response);
        assertEquals(productId, response.getProductId());
        assertEquals(brandId, response.getBrandId());
        assertEquals(priceList, response.getPriceListId());
        assertEquals(priceValue, response.getPriceToApply());
        assertNotNull(response.getEffectiveDateRange());
        assertEquals(startDate, response.getEffectiveDateRange().getFrom());
        assertEquals(endDate, response.getEffectiveDateRange().getTo());
    }

    @Test
    @DisplayName("Should return null when PriceModel is null")
    void whenPriceModelIsNull_thenReturnNull() {
        // Act
        PriceResponse response = mapper.toResponse(null);

        // Assert
        assertNull(response);
    }
}
