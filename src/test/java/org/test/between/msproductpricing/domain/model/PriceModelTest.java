package org.test.between.msproductpricing.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Price Model Domain Unit Tests")
class PriceModelTest {

    @Test
    @DisplayName("Should create PriceModel correctly using constructor and getters")
    void shouldCreatePriceModelWithConstructorAndGetters() {
        // Arrange
        Integer brandId = 1;
        Long productId = 35455L;
        Integer priceList = 1;
        Short priority = 0;
        BigDecimal priceValue = new BigDecimal("35.50");
        String currencyIso = "EUR";
        OffsetDateTime startDate = OffsetDateTime.parse("2020-06-14T00:00:00Z");
        OffsetDateTime endDate = OffsetDateTime.parse("2020-12-31T23:59:59Z");
        PriceModel.RangeDate rangeDate = new PriceModel.RangeDate(startDate, endDate);

        // Act
        PriceModel priceModel = new PriceModel(brandId, productId, priceList, rangeDate, priority, priceValue, currencyIso);

        // Assert
        assertNotNull(priceModel);
        assertEquals(brandId, priceModel.getBrandId());
        assertEquals(productId, priceModel.getProductId());
        assertEquals(priceList, priceModel.getPriceList());
        assertEquals(rangeDate, priceModel.getEffectiveDates());
        assertEquals(priority, priceModel.getPriority());
        assertEquals(priceValue, priceModel.getPrice());
        assertEquals(currencyIso, priceModel.getCurrencyIso());
    }

    @Test
    @DisplayName("Should create PriceModel correctly using Builder pattern")
    void shouldCreatePriceModelWithBuilder() {
        // Arrange
        Integer brandId = 1;
        Long productId = 35455L;
        Integer priceList = 1;
        Short priority = 1;
        BigDecimal priceValue = new BigDecimal("25.45");
        String currencyIso = "EUR";
        OffsetDateTime startDate = OffsetDateTime.parse("2020-06-14T15:00:00Z");
        OffsetDateTime endDate = OffsetDateTime.parse("2020-06-14T18:30:00Z");
        PriceModel.RangeDate rangeDate = new PriceModel.RangeDate(startDate, endDate);

        // Act
        PriceModel priceModel = PriceModel.builder()
                .brandId(brandId)
                .productId(productId)
                .priceList(priceList)
                .priority(priority)
                .price(priceValue)
                .currencyIso(currencyIso)
                .effectiveDates(rangeDate)
                .build();

        // Assert
        assertNotNull(priceModel);
        assertEquals(brandId, priceModel.getBrandId());
        assertEquals(productId, priceModel.getProductId());
        assertEquals(priceList, priceModel.getPriceList());
        assertEquals(rangeDate, priceModel.getEffectiveDates());
        assertEquals(priority, priceModel.getPriority());
        assertEquals(priceValue, priceModel.getPrice());
        assertEquals(currencyIso, priceModel.getCurrencyIso());
    }

    @Test
    @DisplayName("Should RangeDate return correct values for start and end dates")
    void shouldRangeDateReturnCorrectValues() {
        // Arrange
        OffsetDateTime startDate = OffsetDateTime.parse("2020-06-14T00:00:00Z");
        OffsetDateTime endDate = OffsetDateTime.parse("2020-12-31T23:59:59Z");

        // Act
        PriceModel.RangeDate rangeDate = new PriceModel.RangeDate(startDate, endDate);

        // Assert
        assertNotNull(rangeDate);
        assertEquals(startDate, rangeDate.getStartDate());
        assertEquals(endDate, rangeDate.getEndDate());
    }
}
