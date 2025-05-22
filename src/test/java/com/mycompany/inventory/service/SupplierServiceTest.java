
package com.mycompany.inventory.service;

import com.mycompany.inventory.model.Supplier;
import com.mycompany.inventory.util.DBInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupplierServiceTest {

    private SupplierService service;

    @BeforeEach
    void setUp() throws Exception {
        DBInitializer.init();
        service = new SupplierService();
    }

    @Test
    void testCreateAndList() throws SQLException {
        Supplier s = service.createSupplier("Іван", "Петров", "Apple");
        assertNotNull(s);
        assertTrue(s.getId() > 0);
        assertEquals("Іван", s.getFirstName());
        assertEquals("Петров", s.getLastName());

        List<Supplier> all = service.listAll();
        assertEquals(1, all.size());
    }

    @Test
    void testCreateDuplicate() throws SQLException {
        Supplier first = service.createSupplier("Марія", "Іванова", "Apple");
        Supplier second = service.createSupplier("Марія", "Іванова", "Apple");
        assertEquals(first.getId(), second.getId());
        assertEquals(1, service.listAll().size());
    }

    @Test
    void testUpdateAndDelete() throws SQLException {
        Supplier s = service.createSupplier("Петро", "Сидоров", "Apple");
        service.updateSupplier(s.getId(), "Петро", "Сидорович", "Apple");
        List<Supplier> all = service.listAll();
        assertEquals("Сидорович", all.get(0).getLastName());

        service.deleteSupplier(s.getId());
        assertTrue(service.listAll().isEmpty());
    }
}
