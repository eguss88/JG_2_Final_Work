package com.javaguru.onlineshop.cartAndProduct;

import com.javaguru.onlineshop.exceptions.NotFoundException;
import com.javaguru.onlineshop.product.Product;
import com.javaguru.onlineshop.product.ProductRepository;
import com.javaguru.onlineshop.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(value = "/scripts/carts_products/before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/scripts/carts_products/after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CartProductControllerIT {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ProductRepository victim;
    @Autowired
    private ShoppingCartRepository victim2;

    @Test
    public void shouldSaveProductInShoppingCart() throws Exception {
        mock.perform(post("/api/v1/baskets/product/2/shopping-cart/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindAllProductsInCart() throws Exception {
        List<Product> list = victim.findAllProductsInCart(1L);
        mock.perform(get("/api/v1/baskets/1")).andExpect(status().isOk());
        assertThat(list)
                .extracting(Product::getId,
                        Product::getName,
                        Product::getRegularPrice,
                        Product::getDescription,
                        Product::getCategory,
                        Product::getDiscount,
                        Product::getActualPrice,
                        Product::getProductAvailability,
                        Product::getWarehouseID)
                .containsExactly(tuple(1L,
                        "Test name 1",
                        new BigDecimal("1.00"),
                        "test description 1",
                        "test category 1",
                        new BigDecimal("0.00"),
                        new BigDecimal("1.00"),
                        0,
                        null));

    }

    @Test
    void shouldGetSumOfProductsInCart() throws Exception {
        BigDecimal expected = victim.getTotalSumOfProductsInCart(1L).orElseThrow(() -> new NotFoundException("Not found cart with ID - " + 1L));
        mock.perform(get("/api/v1/baskets/total/1"))
                .andExpect(status().isOk());
        assertEquals(new BigDecimal("1.00"), expected);
    }

    @Test
    void shouldRemoveProductFromShoppingCart() throws Exception {
        mock.perform(delete("/api/v1/baskets/remove/product/1/shopping-cart/1"))
                .andExpect(status().isOk());
    }

}