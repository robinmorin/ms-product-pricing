package org.test.between.msproductpricing.infrastructure.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.test.between.msproductpricing.domain.model.PriceModel;
import org.test.between.msproductpricing.infrastructure.repository.entity.Brand;
import org.test.between.msproductpricing.infrastructure.repository.entity.Currency;
import org.test.between.msproductpricing.infrastructure.repository.entity.Price;
import org.test.between.msproductpricing.infrastructure.repository.entity.PricePK;
import org.test.between.msproductpricing.infrastructure.repository.entity.Product;
import org.test.between.msproductpricing.infrastructure.repository.mapper.PriceEntityMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Search Price Repository Adapter Unit Tests")
class SearchPriceRepositoryAdapterTest {

    @Mock
    private JpaPriceRepository priceRepository;

    @Mock
    private PriceEntityMapper priceEntityMapper;

    @InjectMocks
    private SearchPriceRepositoryAdapter searchPriceRepositoryAdapter;

    private Long productId;
    private Integer brandId;
    private OffsetDateTime effectiveDate;

    @BeforeEach
    void setUp() {
        productId = 35455L;
        brandId = 1;
        effectiveDate = OffsetDateTime.now();
    }

    @Test
    @DisplayName("Should return PriceModel when price is found in repository")
    void whenPriceFound_thenReturnPriceModel() {
        // Arrange
        Brand brand = Brand.builder().brandId(brandId).brandName("ZARA").build();
        Product product = Product.builder().productId(productId).productName("T-shirt").build();
        PricePK pricePK = PricePK.builder().brand(brand).product(product).priceList(1).build();
        Currency currency = Currency.builder().currencyIso("EUR").currencyName("Euro").build();
        
        Price price = Price.builder()
                .pricePK(pricePK)
                .startDate(effectiveDate.minusDays(1))
                .endDate(effectiveDate.plusDays(1))
                .priority((short) 1)
                .vPrice(new BigDecimal("35.50"))
                .currency(currency)
                .build();

        PriceModel priceModel = PriceModel.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(1)
                .price(new BigDecimal("35.50"))
                .currencyIso("EUR")
                .build();

        when(priceRepository.findByParamsOrderPriorityDesc(productId, brandId, effectiveDate))
                .thenReturn(Optional.of(List.of(price)));
        when(priceEntityMapper.toDomain(price)).thenReturn(priceModel);

        // Act
        Optional<PriceModel> result = searchPriceRepositoryAdapter.searchByMostPriority(productId, brandId, effectiveDate);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(priceModel, result.get());
        verify(priceRepository).findByParamsOrderPriorityDesc(productId, brandId, effectiveDate);
        verify(priceEntityMapper).toDomain(price);
    }

    @Test
    @DisplayName("Should return empty Optional when no price is found in repository")
    void whenNoPriceFound_thenReturnEmptyOptional() {
        // Arrange
        when(priceRepository.findByParamsOrderPriorityDesc(productId, brandId, effectiveDate))
                .thenReturn(Optional.empty());

        // Act
        Optional<PriceModel> result = searchPriceRepositoryAdapter.searchByMostPriority(productId, brandId, effectiveDate);

        // Assert
        assertTrue(result.isEmpty());
        verify(priceRepository).findByParamsOrderPriorityDesc(productId, brandId, effectiveDate);
    }

    @Test
    @DisplayName("Should return empty Optional when repository returns empty list")
    void whenRepositoryReturnsEmptyList_thenReturnEmptyOptional() {
        // Arrange
        when(priceRepository.findByParamsOrderPriorityDesc(productId, brandId, effectiveDate))
                .thenReturn(Optional.of(Collections.emptyList()));

        // Act
        Optional<PriceModel> result = searchPriceRepositoryAdapter.searchByMostPriority(productId, brandId, effectiveDate);

        // Assert
        assertTrue(result.isEmpty());
        verify(priceRepository).findByParamsOrderPriorityDesc(productId, brandId, effectiveDate);
    }
}
