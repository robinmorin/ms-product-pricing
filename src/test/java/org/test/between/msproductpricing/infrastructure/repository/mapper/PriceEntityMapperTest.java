package org.test.between.msproductpricing.infrastructure.repository.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.test.between.msproductpricing.domain.model.PriceModel;
import org.test.between.msproductpricing.infrastructure.repository.entity.Brand;
import org.test.between.msproductpricing.infrastructure.repository.entity.Currency;
import org.test.between.msproductpricing.infrastructure.repository.entity.Price;
import org.test.between.msproductpricing.infrastructure.repository.entity.PricePK;
import org.test.between.msproductpricing.infrastructure.repository.entity.Product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Price Entity Mapper Unit Tests")
class PriceEntityMapperTest {

    private PriceEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PriceEntityMapper.class);
    }

    @Test
    @DisplayName("Should map Price entity to PriceModel correctly")
    void shouldMapPriceToPriceModel() {
        OffsetDateTime startDate = OffsetDateTime.now();
        OffsetDateTime endDate = startDate.plusDays(1);

        Price priceEntity = Price.builder()
                .pricePK(PricePK.builder()
                        .product(Product.builder().productId(35455L).build())
                        .brand(Brand.builder().brandId(1).build())
                        .priceList(1)
                        .build())
                .startDate(startDate)
                .endDate(endDate)
                .priority((short) 0)
                .vPrice(new BigDecimal("35.50"))
                .currency(Currency.builder().currencyIso("EUR").build())
                .build();

        PriceModel priceModel = mapper.toDomain(priceEntity);

        assertNotNull(priceModel);
        assertEquals(35455L, priceModel.getProductId());
        assertEquals(1, priceModel.getBrandId());
        assertEquals(1, priceModel.getPriceList());
        assertEquals(startDate, priceModel.getEffectiveDates().getStartDate());
        assertEquals(endDate, priceModel.getEffectiveDates().getEndDate());
        assertEquals(new BigDecimal("35.50"), priceModel.getPrice());
        assertEquals("EUR", priceModel.getCurrencyIso());
    }

    @Test
    @DisplayName("Should return null when Price entity is null in toDomain")
    void shouldReturnNullWhenPriceIsNullInToDomain() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("Should handle null nested objects in Price to PriceModel mapping")
    void shouldHandleNullNestedObjectsInToDomain() {
        Price price = new Price();
        // Test with pricePK null
        PriceModel model = mapper.toDomain(price);
        assertNotNull(model);
        assertNull(model.getProductId());
        assertNull(model.getBrandId());
        assertNull(model.getPriceList());
        assertNotNull(model.getEffectiveDates()); // rangeDate is created with null dates
        assertNull(model.getEffectiveDates().getStartDate());
        assertNull(model.getEffectiveDates().getEndDate());

        // Test with product null in pricePK
        price.setPricePK(new PricePK());
        model = mapper.toDomain(price);
        assertNotNull(model);
        assertNull(model.getProductId());

        // Test with brand null in pricePK
        assertNull(model.getBrandId());

        // Test with currency null
        assertNull(model.getCurrencyIso());
    }

    @Test
    @DisplayName("Should map PriceModel to Price entity correctly")
    void shouldMapPriceModelToPrice() {
        OffsetDateTime startDate = OffsetDateTime.now();
        OffsetDateTime endDate = startDate.plusDays(1);

        PriceModel priceModel = PriceModel.builder()
                .productId(35455L)
                .brandId(1)
                .priceList(1)
                .effectiveDates(new PriceModel.RangeDate(startDate, endDate))
                .priority((short) 0)
                .price(new BigDecimal("35.50"))
                .currencyIso("EUR")
                .build();

        Price priceEntity = mapper.toEntity(priceModel);

        assertNotNull(priceEntity);
        assertNotNull(priceEntity.getPricePK());
        assertEquals(35455L, priceEntity.getPricePK().getProduct().getProductId());
        assertEquals(1, priceEntity.getPricePK().getBrand().getBrandId());
        assertEquals(1, priceEntity.getPricePK().getPriceList());
        assertEquals(startDate, priceEntity.getStartDate());
        assertEquals(endDate, priceEntity.getEndDate());
        assertEquals(new BigDecimal("35.50"), priceEntity.getVPrice());
        assertEquals("EUR", priceEntity.getCurrency().getCurrencyIso());
    }

    @Test
    @DisplayName("Should return null when PriceModel is null in toEntity")
    void shouldReturnNullWhenPriceModelIsNullInToEntity() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Should handle null nested objects in PriceModel to Price entity mapping")
    void shouldHandleNullNestedObjectsInToEntity() {
        PriceModel model = PriceModel.builder().build();
        Price entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertNotNull(entity.getPricePK());
        assertNotNull(entity.getPricePK().getProduct());
        assertNull(entity.getPricePK().getProduct().getProductId());
        assertNotNull(entity.getPricePK().getBrand());
        assertNull(entity.getPricePK().getBrand().getBrandId());
        assertNull(entity.getStartDate());
        assertNull(entity.getEndDate());
        assertNotNull(entity.getCurrency());
        assertNull(entity.getCurrency().getCurrencyIso());
    }
}
