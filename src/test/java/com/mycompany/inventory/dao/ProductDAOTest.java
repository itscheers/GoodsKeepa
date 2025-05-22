package com.mycompany.inventory.dao;

import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.model.Product;
import com.mycompany.inventory.util.DBInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest {

    private CategoryDAO catDao;
    private ProductDAO  prodDao;
    private int         catId;

    @BeforeEach
    void setUp() throws SQLException {
        DBInitializer.init();
        catDao  = new CategoryDAO();
        prodDao = new ProductDAO();

        Category cat = new Category("Напої");
        catDao.add(cat);
        catId = cat.getId();
    }

    @Test
    void testAddAndGetAll() throws SQLException {
        assertTrue(prodDao.getAll().isEmpty());

        Product p = new Product("Кола", "Coca-Cola", 1.5, 100, catId);
        prodDao.add(p);

        assertTrue(p.getId() > 0);
        List<Product> all = prodDao.getAll();
        assertEquals(1, all.size());
        assertEquals("Кола", all.get(0).getName());
    }

    @Test
    void testFilterAndUpdateAndDelete() throws SQLException {
        prodDao.add(new Product("Чай", "Lipton", 0.9, 50, catId));
        prodDao.add(new Product("Кава", "Nescafé", 2.0, 30, catId));

        List<Product> tea = prodDao.filterByName("Ч");
        assertEquals(1, tea.size());
        assertEquals("Чай", tea.get(0).getName());

        Product c = tea.get(0);
        c.setPrice(1.1);
        prodDao.update(c);
        assertEquals(1.1, prodDao.getById(c.getId()).getPrice());

        prodDao.delete(c.getId());
        List<Product> remaining = prodDao.getAll();
        assertEquals(1, remaining.size());
        assertEquals("Кава", remaining.get(0).getName());
    }
}
