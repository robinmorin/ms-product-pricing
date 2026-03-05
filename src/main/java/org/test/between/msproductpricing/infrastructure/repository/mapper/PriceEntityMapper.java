package org.test.between.msproductpricing.infrastructure.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.test.between.msproductpricing.domain.model.PriceModel;
import org.test.between.msproductpricing.infrastructure.repository.entity.Price;

/**
 * PriceMapper class : Mapper to convert domain object PriceDomain to entity object Price
 */
@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

    @Mapping(target = "pricePK.product.productId", source = "productId")
    @Mapping(target = "pricePK.brand.brandId", source = "brandId")
    @Mapping(target = "pricePK.priceList", source = "priceList")
    @Mapping(target = "vPrice", source = "price", numberFormat = "$#.00")
    @Mapping(target = "startDate", source = "effectiveDates.startDate")
    @Mapping(target = "endDate", source = "effectiveDates.endDate")
    @Mapping(target = "currency.currencyIso", source = "currencyIso")
    Price toEntity(PriceModel priceModel);

    @Mapping(target = "productId", source = "pricePK.product.productId")
    @Mapping(target = "brandId", source = "pricePK.brand.brandId")
    @Mapping(target = "priceList", source = "pricePK.priceList")
    @Mapping(target = "price", source = "VPrice", numberFormat = "$#.00")
    @Mapping(target = "effectiveDates.startDate", source = "startDate")
    @Mapping(target = "effectiveDates.endDate", source = "endDate")
    @Mapping(target = "currencyIso", source = "currency.currencyIso")
    PriceModel toDomain(Price price);

}
