
package com.mycompany.inventory.service;

import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.model.Product;
import com.mycompany.inventory.util.DBInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private CategoryService  catService;
    private ProductService   prodService;

    @BeforeEach
    void setUp() throws Exception {
        DBInitializer.init();
        catService  = new CategoryService();
        prodService = new ProductService();
    }

    @Test
    void testCreateAndList() throws SQLException {
        Category cat = catService.createCategory("Напої");
        Product p = prodService.createProduct("Кола", "Coca-Cola", 1.5, 10, cat.getId());
        assertNotNull(p);
        assertTrue(p.getId() > 0);
        assertEquals("Кола", p.getName());

        List<Product> all = prodService.listAll();
        assertEquals(1, all.size());
        assertEquals("Coca-Cola", all.get(0).getBrand());
    }

    @Test
    void testUpdateAndDelete() throws SQLException {
        Category cat = catService.createCategory("Закуски");
        Product p = prodService.createProduct("Чіпси", "Lays", 2.0, 5, cat.getId());

        prodService.updateProduct(p.getId(), "Чіпси", "Lays", 2.5, 7, cat.getId());
        List<Product> all = prodService.listAll();
        Product upd = all.get(0);
        assertEquals(2.5, upd.getPrice());
        assertEquals(7, upd.getQuantity());

        prodService.deleteProduct(p.getId());
        assertTrue(prodService.listAll().isEmpty());
    }
}
