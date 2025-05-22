package com.mycompany.inventory.service;

import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.util.DBInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private CategoryService service;

    @BeforeEach
    void setUp() throws Exception {
        DBInitializer.init();
        service = new CategoryService();
    }

    @Test
    void testCreateCategoryAndAddToList() throws SQLException {
        Category c = service.createCategory("Тестова");
        assertNotNull(c);
        assertTrue(c.getId() > 0);
        assertEquals("Тестова", c.getName());

        List<Category> all = service.listAll();
        assertEquals(1, all.size());
        assertEquals("Тестова", all.get(0).getName());
    }

    @Test
    void testCreateDuplicateReturnsExisting() throws SQLException {
        Category first = service.createCategory("Дублікат");
        Category second = service.createCategory("Дублікат");
        assertEquals(first.getId(), second.getId(),
                "При спробі додати дублікат має повернутися попередній об'єкт");
        List<Category> all = service.listAll();
        assertEquals(1, all.size());
    }

    @Test
    void testUpdateAndDelete() throws SQLException {
        Category c = service.createCategory("Старе");
        service.updateCategory(c.getId(), "Нове");
        List<Category> all = service.listAll();
        assertEquals("Нове", all.get(0).getName());

        service.deleteCategory(c.getId());
        assertTrue(service.listAll().isEmpty());
    }
}
