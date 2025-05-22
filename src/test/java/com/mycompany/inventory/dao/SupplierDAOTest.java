// src/test/java/com/mycompany/inventory/dao/SupplierDAOTest.java
package com.mycompany.inventory.dao;

import com.mycompany.inventory.model.Supplier;
import com.mycompany.inventory.util.DBInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupplierDAOTest {

    private SupplierDAO dao;

    @BeforeEach
    void setUp() throws SQLException {
        DBInitializer.init();
        dao = new SupplierDAO();
    }

    @Test
    void testAddAndGetAll() throws SQLException {
        assertTrue(dao.getAll().isEmpty());

        Supplier s = new Supplier("Ivan", "Ivanov", "Apple");
        dao.add(s);
        assertTrue(s.getId() > 0);

        List<Supplier> all = dao.getAll();
        assertEquals(1, all.size());
        assertEquals("Ivan", all.get(0).getFirstName());
        assertEquals("Ivanov", all.get(0).getLastName());
    }

    @Test
    void testFilterAndUpdateAndDelete() throws SQLException {
        dao.add(new Supplier("Anna", "Petrova", "Apple"));
        dao.add(new Supplier("Alex", "Smirnov", "Apple"));

        List<Supplier> found = dao.filterByName("An");
        assertEquals(1, found.size());
        assertEquals("Anna", found.get(0).getFirstName());

        Supplier a = found.get(0);
        a.setLastName("Ivanova");
        dao.update(a);
        assertEquals("Ivanova", dao.getById(a.getId()).getLastName());

        dao.delete(a.getId());
        assertEquals(1, dao.getAll().size());
    }
}
