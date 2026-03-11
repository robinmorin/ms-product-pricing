package org.test.between.msproductpricing.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.test.between.msproductpricing.domain.model.PriceModel;
import org.test.between.msproductpricing.domain.ports.out.repository.SearchPriceRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Search Price Use Case Unit Tests")
class SearchPriceUseCaseTest {

    @Mock
    private SearchPriceRepository searchPriceRepository;

    @InjectMocks
    private SearchPriceUseCase searchPriceUseCase;

    @Test
    @DisplayName("Should invoke repository and return mapped result when searching price")
    void whenInvokeSearchByMostPriority_thenCallRepositoryAndReturnResult() {
        // Arrange
        Long productId = 35455L;
        Integer brandId = 1;
        OffsetDateTime effectiveDate = OffsetDateTime.now();
        PriceModel priceModel = PriceModel.builder().productId(productId).brandId(brandId).build();
        
        when(searchPriceRepository.searchByMostPriority(productId, brandId, effectiveDate))
                .thenReturn(Optional.of(priceModel));

        // Act
        Optional<PriceModel> result = searchPriceUseCase.searchByMostPriority(productId, brandId, effectiveDate);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(priceModel, result.get());
        verify(searchPriceRepository).searchByMostPriority(productId, brandId, effectiveDate);
    }
}
