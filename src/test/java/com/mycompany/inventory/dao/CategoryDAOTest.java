package com.mycompany.inventory.dao;

import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.util.DBInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest {

    private CategoryDAO dao;

    @BeforeEach
    void setUp() throws SQLException {
        DBInitializer.init();
        dao = new CategoryDAO();
    }

    @Test
    void testAddAndGetAll() throws SQLException {
        assertTrue(dao.getAll().isEmpty(), "Спочатку таблиця повинна бути порожньою");

        Category cat = new Category("Тест");
        dao.add(cat);
        assertTrue(cat.getId() > 0, "Після add() має бути встановлено id");

        List<Category> all = dao.getAll();
        assertEquals(1, all.size());
        assertEquals("Тест", all.get(0).getName());
    }

    @Test
    void testFilterByName() throws SQLException {
        dao.add(new Category("Apple"));
        dao.add(new Category("Banana"));
        dao.add(new Category("Apricot"));

        List<Category> filtered = dao.filterByName("Ap");
        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().anyMatch(c -> c.getName().equals("Apple")));
        assertTrue(filtered.stream().anyMatch(c -> c.getName().equals("Apricot")));
    }

    @Test
    void testUpdateAndDelete() throws SQLException {
        Category c = new Category("Old");
        dao.add(c);

        c.setName("New");
        dao.update(c);

        Category fromDb = dao.getById(c.getId());
        assertEquals("New", fromDb.getName());

        dao.delete(c.getId());
        assertTrue(dao.getAll().isEmpty());
    }
}
