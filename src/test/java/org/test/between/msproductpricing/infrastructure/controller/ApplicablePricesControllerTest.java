package org.test.between.msproductpricing.infrastructure.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DisplayName("Applicable Prices Controller Integration Tests")
class ApplicablePricesControllerTest {
    
    private static final String URL_TEMPLATE = "/api/v1/products/{productId}/brands/{brandId}/applicable-price";
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return 404 when no price is found for given date")
    void testPriceQueryAt10AmOn14th_thenReturnResourceNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE, "35455", "1")
                        .param("effectiveDate", "2023-06-14T10:00:00Z"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Test 1: Request at 10:00 on day 14th for product 35455 and brand 1 (ZARA)")
    void testPriceQueryAt10AmOn14th() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE, "35455", "1")
                        .param("effectiveDate", "2020-06-14T10:00:00Z"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("{" +
                        "'productId': 35455," +
                        "'brandId': 1," +
                        "'priceListId': 1," +
                        "'effectiveDateRange': {" +
                        "  'from': '2020-06-14T00:00:00Z'," +
                        "  'to': '2020-12-31T23:59:59Z'" +
                        "}," +
                        "'priceToApply': 35.50" +
                        "}"));

    }

    @Test
    @DisplayName("Test 2: Request at 16:00 on day 14th for product 35455 and brand 1 (ZARA)")
    void testPriceQueryAt4PmOn14th() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE, "35455", "1")
                        .param("effectiveDate", "2020-06-14T16:00:00Z"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("{" +
                        "'productId': 35455," +
                        "'brandId': 1," +
                        "'priceListId': 2," +
                        "'effectiveDateRange': {" +
                        "  'from': '2020-06-14T15:00:00Z'," +
                        "  'to': '2020-06-14T18:30:00Z'" +
                        "}," +
                        "'priceToApply': 25.45" +
                        "}"));
    }

    @Test
    @DisplayName("Test 3: Request at 21:00 on day 14th for product 35455 and brand 1 (ZARA)")
    void testPriceQueryAt9PmOn14th() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE, "35455", "1")
                        .param("effectiveDate", "2020-06-14T21:00:00Z"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("{" +
                        "'productId': 35455," +
                        "'brandId': 1," +
                        "'priceListId': 1," +
                        "'effectiveDateRange': {" +
                        "  'from': '2020-06-14T00:00:00Z'," +
                        "  'to': '2020-12-31T23:59:59Z'" +
                        "}," +
                        "'priceToApply': 35.50" +
                        "}"));
    }

    @Test
    @DisplayName("Test 4: Request at 10:00 on day 15th for product 35455 and brand 1 (ZARA)")
    void testPriceQueryAt10AmOn15th() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE, "35455", "1")
                        .param("effectiveDate", "2020-06-15T10:00:00Z"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("{" +
                        "'productId': 35455," +
                        "'brandId': 1," +
                        "'priceListId': 3," +
                        "'effectiveDateRange': {" +
                        "  'from': '2020-06-15T00:00:00Z'," +
                        "  'to': '2020-06-15T11:00:00Z'" +
                        "}," +
                        "'priceToApply': 30.50" +
                        "}"));
    }

    @Test
    @DisplayName("Test 5: Request at 21:00 on day 16th for product 35455 and brand 1 (ZARA)")
    void testPriceQueryAt9PmOn16th() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE, "35455", "1")
                        .param("effectiveDate", "2020-06-16T21:00:00Z"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json("{" +
                        "'productId': 35455," +
                        "'brandId': 1," +
                        "'priceListId': 4," +
                        "'effectiveDateRange': {" +
                        "  'from': '2020-06-15T16:00:00Z'," +
                        "  'to': '2020-12-31T23:59:59Z'" +
                        "}," +
                        "'priceToApply': 38.95" +
                        "}"));
    }


}